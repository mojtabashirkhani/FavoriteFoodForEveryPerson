package com.example.myapplication.screen.main

import com.example.myapplication.domain.model.Food
import com.example.myapplication.domain.model.PersonWithFoods

data class MainViewState(
    val personsWithFoods: List<PersonWithFoods> = emptyList(),
    val allFoods: List<Food> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
