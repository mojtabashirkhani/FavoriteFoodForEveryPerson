package com.example.myapplication.screen.personList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.database.dao.PersonFoodDao
import com.example.myapplication.database.model.FoodEntity
import com.example.myapplication.database.model.PersonFoodCrossRef
import com.example.myapplication.database.model.PersonWithFoods
import com.example.myapplication.screen.main.MainViewIntent
import com.example.myapplication.screen.main.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
fun PersonListScreen() {
    val mainViewModel: MainViewModel = hiltViewModel()

    val viewState by mainViewModel.viewState.collectAsState()

    LaunchedEffect(Unit) {
        mainViewModel.processIntent(MainViewIntent.LoadPersonsWithFoods)
        mainViewModel.processIntent(MainViewIntent.LoadAllFoods)
    }

    LazyColumn {
        items(viewState.personsWithFoods) { personWithFoods ->
            PersonItem(personWithFoods, mainViewModel)
        }
    }
}

@Composable
fun MultiSelectDropdown(
    label: String,
    options: List<FoodEntity>,
    selectedOptions: List<FoodEntity>,
    onSelectionChange: (FoodEntity, Boolean) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        TextField(
            value = selectedOptions.joinToString { it.name },
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    Modifier.clickable { expanded = !expanded }
                )
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                val isSelected = selectedOptions.contains(option)
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = isSelected,
                                onCheckedChange = null // null recommended for non-clickable checkboxes
                            )
                            Text(text = option.name)
                        }
                    },
                    onClick = {
                        onSelectionChange(option, !isSelected)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun PersonItem(personWithFoods: PersonWithFoods, mainViewModel: MainViewModel) {
    val viewState by mainViewModel.viewState.collectAsState()
    var selectedFoods by remember { mutableStateOf(personWithFoods.favoriteFoods) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = personWithFoods.person.name, style = MaterialTheme.typography.bodySmall)

        MultiSelectDropdown(
            label = "Select Favorite Foods",
            options = viewState.allFoods,
            selectedOptions = selectedFoods,
            onSelectionChange = { food, isSelected ->
                selectedFoods = if (isSelected) {
                    selectedFoods + food
                } else {
                    selectedFoods - food
                }
            }
        )

        Button(onClick = {
            mainViewModel.processIntent(MainViewIntent.UpdateFavoriteFoods(personWithFoods.person.id, selectedFoods))
        }) {
            Text("Update Favorites")
        }

        Text("Favorite Foods:", style = MaterialTheme.typography.bodySmall)
        selectedFoods.forEach { food ->
            Text(text = food.name)
        }
    }
}


