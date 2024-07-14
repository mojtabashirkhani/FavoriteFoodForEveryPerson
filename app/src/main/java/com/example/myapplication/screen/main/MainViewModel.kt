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

    private val _personsWithFoods = MutableLiveData<List<PersonWithFoods>>()
    val personsWithFoods: LiveData<List<PersonWithFoods>> get() = _personsWithFoods

    private val _allFoods = MutableLiveData<List<FoodEntity>>()
    val allFoods: LiveData<List<FoodEntity>> get() = _allFoods

    fun addPerson(name: String) {
        if (name.isNotEmpty()) {
            viewModelScope.launch {
                val person = PersonEntity(name = name)
                addPersonUseCase(person)
                loadPersonsWithFoods()
            }
        }
    }

    fun addFood(name: String) {
        if (name.isNotEmpty()) {
            viewModelScope.launch {
                val food = FoodEntity(name = name)
                addFoodUseCase(food)
                loadAllFoods()
            }
        }
    }

    fun loadPersonsWithFoods() {
        viewModelScope.launch {
            _personsWithFoods.value = getAllPersonsWithFoodsUseCase()
        }
    }

    fun loadAllFoods() {
        viewModelScope.launch {
            _allFoods.value = getAllFoodsUseCase()
        }
    }

    fun addFavoriteFood(personId: Long, foodId: Long) {
        viewModelScope.launch {
            addFavoriteFoodUseCase(personId, foodId)
            loadPersonsWithFoods()
        }
    }

    fun removeFavoriteFood(personId: Long, foodId: Long) {
        viewModelScope.launch {
            deleteFavoriteFoodUseCase(personId, foodId)
            loadPersonsWithFoods()
        }
    }

    fun updateFavoriteFoods(personId: Long, selectedFoods: List<FoodEntity>) {
        viewModelScope.launch {
            // Get current favorite foods
            val personWithFoods = getAllPersonsWithFoodsUseCase().firstOrNull { it.person.id == personId }
            personWithFoods?.favoriteFoods?.forEach { food ->
                if (!selectedFoods.contains(food)) {
                    // Remove food that is no longer selected
                    removeFavoriteFood(personId, food.id)
                }
            }

            // Add new selected foods
            selectedFoods.forEach { food ->
                if (personWithFoods?.favoriteFoods?.contains(food) == true) {
                    addFavoriteFood(personId, food.id)
                }
            }
            loadPersonsWithFoods()
        }
    }
}