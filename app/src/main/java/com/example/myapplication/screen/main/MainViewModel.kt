package com.example.myapplication.screen.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.database.model.FoodEntity
import com.example.myapplication.database.model.PersonEntity
import com.example.myapplication.database.model.PersonWithFoods
import com.example.myapplication.domain.AddFavoriteFoodUseCase
import com.example.myapplication.domain.AddFoodUseCase
import com.example.myapplication.domain.AddPersonUseCase
import com.example.myapplication.domain.DeleteFavoriteFoodUseCase
import com.example.myapplication.domain.Food
import com.example.myapplication.domain.GetAllFoodsUseCase
import com.example.myapplication.domain.GetAllPersonsWithFoodsUseCase
import com.example.myapplication.domain.Person
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val addPersonUseCase: AddPersonUseCase,
    private val addFoodUseCase: AddFoodUseCase,
    private val getAllPersonsWithFoodsUseCase: GetAllPersonsWithFoodsUseCase,
    private val getAllFoodsUseCase: GetAllFoodsUseCase,
    private val addFavoriteFoodUseCase: AddFavoriteFoodUseCase,
    private val deleteFavoriteFoodUseCase: DeleteFavoriteFoodUseCase

) : ViewModel() {

    private val _viewState = MutableStateFlow(MainViewState())
    val viewState: StateFlow<MainViewState> = _viewState

    private val _viewEffect = Channel<MainViewEffect>()
    val viewEffect = _viewEffect.receiveAsFlow()

    init {
        processIntent(MainViewIntent.LoadPersonsWithFoods)
        processIntent(MainViewIntent.LoadAllFoods)
    }

    fun processIntent(intent: MainViewIntent) {
        when (intent) {
            is MainViewIntent.LoadPersonsWithFoods -> loadPersonsWithFoods()
            is MainViewIntent.LoadAllFoods -> loadAllFoods()
            is MainViewIntent.AddPerson -> addPerson(intent.name)
            is MainViewIntent.AddFood -> addFood(intent.name)
            is MainViewIntent.AddFavoriteFood -> addFavoriteFood(intent.personId, intent.foodId)
            is MainViewIntent.RemoveFavoriteFood -> removeFavoriteFood(intent.personId, intent.foodId)
            is MainViewIntent.UpdateFavoriteFoods -> updateFavoriteFoods(intent.personId, intent.selectedFoods)
        }
    }

    private fun loadPersonsWithFoods() {
        viewModelScope.launch {
            _viewState.value = _viewState.value.copy(isLoading = true)
            try {
                getAllPersonsWithFoodsUseCase().collect { personsWithFoods ->
                    _viewState.value = _viewState.value.copy(personsWithFoods = personsWithFoods, isLoading = false)
                }
            } catch (e: Exception) {
                _viewState.value = _viewState.value.copy(isLoading = false, error = e.message)
                _viewEffect.send(MainViewEffect.ShowError("Failed to load persons with foods"))
            }
        }
    }

    private fun loadAllFoods() {
        viewModelScope.launch {
            _viewState.value = _viewState.value.copy(isLoading = true)
            try {
                getAllFoodsUseCase().collect { foods ->
                    _viewState.value = _viewState.value.copy(allFoods = foods, isLoading = false)
                }
            } catch (e: Exception) {
                _viewState.value = _viewState.value.copy(isLoading = false, error = e.message)
                _viewEffect.send(MainViewEffect.ShowError("Failed to load foods"))
            }
        }
    }

    private fun addPerson(name: String) {
        if (name.isNotEmpty()) {
            viewModelScope.launch {
                try {
                    val person = Person(name = name)
                    addPersonUseCase(person)
                    refreshPersonsWithFoods()
                } catch (e: Exception) {
                    _viewEffect.send(MainViewEffect.ShowError("Failed to add person"))
                }
            }
        }
    }

    private fun addFood(name: String) {
        if (name.isNotEmpty()) {
            viewModelScope.launch {
                try {
                    val food = Food(name = name)
                    addFoodUseCase(food)
                    refreshAllFoods()
                } catch (e: Exception) {
                    _viewEffect.send(MainViewEffect.ShowError("Failed to add food"))
                }
            }
        }
    }

    private fun refreshPersonsWithFoods() {
        processIntent(MainViewIntent.LoadPersonsWithFoods)
    }

    private fun refreshAllFoods() {
        processIntent(MainViewIntent.LoadAllFoods)
    }

    private fun addFavoriteFood(personId: Long, foodId: Long) {
        viewModelScope.launch {
            try {
                addFavoriteFoodUseCase(personId, foodId)
                refreshPersonsWithFoods()
            } catch (e: Exception) {
                _viewEffect.send(MainViewEffect.ShowError("Failed to add favorite food"))
            }
        }
    }

    private fun removeFavoriteFood(personId: Long, foodId: Long) {
        viewModelScope.launch {
            try {
                deleteFavoriteFoodUseCase(personId, foodId)
                refreshPersonsWithFoods()
            } catch (e: Exception) {
                _viewEffect.send(MainViewEffect.ShowError("Failed to remove favorite food"))
            }
        }
    }

    private fun updateFavoriteFoods(personId: Long, selectedFoods: List<Food>) {
        viewModelScope.launch {
            try {
                val personWithFoods = _viewState.value.personsWithFoods.firstOrNull { it.person.id == personId }
                personWithFoods?.foods?.forEach { food ->
                    if (!selectedFoods.contains(food)) {
                        removeFavoriteFood(personId, food.id ?: -1L)
                    }
                }
                selectedFoods.forEach { food ->
                    if (personWithFoods?.foods?.contains(food) != true) {
                        addFavoriteFood(personId, food.id ?: -1L)
                    }
                }
                refreshPersonsWithFoods()
            } catch (e: Exception) {
                _viewEffect.send(MainViewEffect.ShowError("Failed to update favorite foods"))
            }
        }
    }
}