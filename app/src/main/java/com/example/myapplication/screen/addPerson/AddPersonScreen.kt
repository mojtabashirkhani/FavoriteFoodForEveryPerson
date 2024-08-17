package com.example.myapplication.screen.addPerson

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.database.dao.PersonFoodDao
import com.example.myapplication.database.model.PersonEntity
import com.example.myapplication.screen.main.MainViewIntent
import com.example.myapplication.screen.main.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
fun AddPersonScreen() {
    val mainViewModel: MainViewModel = hiltViewModel()

    val viewState by mainViewModel.viewState.collectAsState()
    var name by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Person Name") }
        )
        Button(onClick = {
            mainViewModel.processIntent(MainViewIntent.AddPerson(name))
            name = ""
        }) {
            Text("Add Person")
        }
        viewState.error?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }
    }
}