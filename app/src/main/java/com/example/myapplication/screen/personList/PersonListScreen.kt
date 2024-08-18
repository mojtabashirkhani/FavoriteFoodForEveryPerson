package com.example.myapplication.screen.personList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.domain.model.Food
import com.example.myapplication.domain.model.PersonWithFoods
import com.example.myapplication.screen.main.MainViewIntent
import com.example.myapplication.screen.main.MainViewModel

@Composable
fun PersonListScreen() {
    val mainViewModel: MainViewModel = hiltViewModel()
    val viewState by mainViewModel.viewState.collectAsState()

    LaunchedEffect(Unit) {
        mainViewModel.processIntent(MainViewIntent.LoadPersonsWithFoods)
        mainViewModel.processIntent(MainViewIntent.LoadAllFoods)
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .padding(16.dp)) {

        Text(
            text = "Person List",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (viewState.personsWithFoods.isEmpty()) {
            Text(
                text = "No persons found. Add some to get started!",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(viewState.personsWithFoods) { personWithFoods ->
                    PersonItem(personWithFoods, mainViewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiSelectDropdown(
    label: String,
    options: List<Food>,
    selectedOptions: List<Food>,
    onSelectionChange: (Food, Boolean) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        OutlinedTextField(
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
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .clip(RoundedCornerShape(8.dp))
        ) {
            options.forEach { option ->
                val isSelected = selectedOptions.contains(option)
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Checkbox(
                                checked = isSelected,
                                onCheckedChange = null, // Prevents toggling via Checkbox click
                                colors = CheckboxDefaults.colors(MaterialTheme.colorScheme.primary)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = option.name)
                        }
                    },
                    onClick = {
                        onSelectionChange(option, !isSelected)
                    }
                )
            }
        }
    }
}

@Composable
fun PersonItem(personWithFoods: PersonWithFoods, mainViewModel: MainViewModel) {
    val viewState by mainViewModel.viewState.collectAsState()
    var selectedFoods by remember { mutableStateOf(personWithFoods.foods) }

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = personWithFoods.person.name,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

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

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    mainViewModel.processIntent(
                        MainViewIntent.UpdateFavoriteFoods(
                            personWithFoods.person.id ?: -1L, selectedFoods
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(),
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = "Update Favorites", color = MaterialTheme.colorScheme.onPrimary)
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (selectedFoods.isNotEmpty()) {
                Text(
                    text = "Favorite Foods:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                selectedFoods.forEach { food ->
                    Text(text = "• ${food.name}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}


