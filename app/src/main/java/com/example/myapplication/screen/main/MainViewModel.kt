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
import com.example.myapplication.domain.GetAllFoodsUseCase
import com.example.myapplication.domain.GetAllPersonsWithFoodsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    private val _personsWithFoods = MutableStateFlow<List<PersonWithFoods>>(emptyList())
    val personsWithFoods: StateFlow<List<PersonWithFoods>> = _personsWithFoods

    private val _allFoods = MutableStateFlow<List<FoodEntity>>(emptyList())
    val allFoods: StateFlow<List<FoodEntity>> = _allFoods

    init {
        viewModelScope.launch {
            getAllPersonsWithFoodsUseCase().collect {
                _personsWithFoods.value = it
            }
        }
        viewModelScope.launch {
            getAllFoodsUseCase().collect {
                _allFoods.value = it
            }
        }
    }

    fun addPerson(name: String) {
        if (name.isNotEmpty()) {
            viewModelScope.launch {
                val person = PersonEntity(name = name)
                addPersonUseCase(person)
                refreshPersonsWithFoods()
            }
        }
    }

    fun addFood(name: String) {
        if (name.isNotEmpty()) {
            viewModelScope.launch {
                val food = FoodEntity(name = name)
                addFoodUseCase(food)
                refreshAllFoods()
            }
        }
    }

     fun refreshPersonsWithFoods() {
        viewModelScope.launch {
            getAllPersonsWithFoodsUseCase().collect {
                _personsWithFoods.value = it
            }
        }
    }

     fun refreshAllFoods() {
        viewModelScope.launch {
            getAllFoodsUseCase().collect {
                _allFoods.value = it
            }
        }
    }

    fun addFavoriteFood(personId: Long, foodId: Long) {
        viewModelScope.launch {
            addFavoriteFoodUseCase(personId, foodId)
            refreshPersonsWithFoods()
        }
    }

    fun removeFavoriteFood(personId: Long, foodId: Long) {
        viewModelScope.launch {
            deleteFavoriteFoodUseCase(personId, foodId)
            refreshPersonsWithFoods()
        }
    }

    fun updateFavoriteFoods(personId: Long, selectedFoods: List<FoodEntity>) {
        viewModelScope.launch {
            val personWithFoods = _personsWithFoods.value.firstOrNull { it.person.id == personId }
            personWithFoods?.favoriteFoods?.forEach { food ->
                if (!selectedFoods.contains(food)) {
                    // Remove food that is no longer selected
                    removeFavoriteFood(personId, food.id)
                }
            }
            selectedFoods.forEach { food ->
                if (personWithFoods?.favoriteFoods?.contains(food) != true) {
                    addFavoriteFood(personId, food.id)
                }
            }
            refreshPersonsWithFoods()
        }
    }
}