package com.example.myapplication.screen.main

import com.example.myapplication.domain.model.Food

sealed class MainViewIntent {
    object LoadPersonsWithFoods : MainViewIntent()
    object LoadAllFoods : MainViewIntent()
    data class AddPerson(val name: String) : MainViewIntent()
    data class AddFood(val name: String) : MainViewIntent()
    data class AddFavoriteFood(val personId: Long, val foodId: Long) : MainViewIntent()
    data class RemoveFavoriteFood(val personId: Long, val foodId: Long) : MainViewIntent()
    data class UpdateFavoriteFoods(val personId: Long, val selectedFoods: List<Food>) : MainViewIntent()
}