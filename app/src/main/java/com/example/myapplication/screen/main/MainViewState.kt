package com.example.myapplication.screen.main

import com.example.myapplication.database.model.FoodEntity
import com.example.myapplication.database.model.PersonWithFoods

data class MainViewState(
    val personsWithFoods: List<PersonWithFoods> = emptyList(),
    val allFoods: List<FoodEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
