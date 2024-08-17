package com.example.myapplication.screen.main

sealed class MainViewEffect {
    data class ShowError(val message: String) : MainViewEffect()
    object NavigateBack : MainViewEffect()
}