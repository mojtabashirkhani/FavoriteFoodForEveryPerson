@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.myapplication.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.database.dao.PersonFoodDao
import com.example.myapplication.screen.addFood.AddFoodScreen
import com.example.myapplication.screen.addPerson.AddPersonScreen
import com.example.myapplication.screen.personList.PersonListScreen
@Composable
fun MainScreen() {
    var currentScreen by remember { mutableStateOf("AddPerson") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Home") },
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                // Navigation Row
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    NavigationButton(
                        text = "Add Person",
                        isSelected = currentScreen == "AddPerson",
                        onClick = { currentScreen = "AddPerson" }
                    )
                    NavigationButton(
                        text = "Add Food",
                        isSelected = currentScreen == "AddFood",
                        onClick = { currentScreen = "AddFood" }
                    )
                    NavigationButton(
                        text = "Persons",
                        isSelected = currentScreen == "PersonList",
                        onClick = { currentScreen = "PersonList" }
                    )
                }

                // Screen Content
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    when (currentScreen) {
                        "AddPerson" -> AddPersonScreen()
                        "AddFood" -> AddFoodScreen()
                        "PersonList" -> PersonListScreen()
                    }
                }
            }
        }
    )
}

@Composable
fun NavigationButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
        ),
        shape = RoundedCornerShape(50),
        modifier = Modifier
            .height(48.dp)
            .padding(horizontal = 8.dp)
    ) {
        Text(text, style = MaterialTheme.typography.bodyMedium)
    }
}