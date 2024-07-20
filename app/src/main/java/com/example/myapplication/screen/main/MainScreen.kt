package com.example.myapplication.screen.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
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


    Column(modifier = Modifier.padding(0.dp, 32.dp)) {
        Row {
            Button(onClick = { currentScreen = "AddPerson" }) {
                Text("Add Person")
            }
            Button(onClick = { currentScreen = "AddFood" }) {
                Text("Add Food")
            }
            Button(onClick = { currentScreen = "PersonList" }) {
                Text("Persons")
            }
        }

        when (currentScreen) {
            "AddPerson" -> AddPersonScreen()
            "AddFood" -> AddFoodScreen()
            "PersonList" -> PersonListScreen()
        }
    }
}