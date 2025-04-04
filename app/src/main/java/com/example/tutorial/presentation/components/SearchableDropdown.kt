package com.example.tutorial.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tutorial.data.local.entities.Person
import com.example.tutorial.data.local.entities.Role

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchableDropdown(
    label: String,
    people: List<Person>,
    selectedPeople: List<Person>,
    onSelect: (Person) -> Unit,
    onRemove: (Person) -> Unit,
    singleSelect: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredPeople = people.filter { it.name.contains(searchQuery, ignoreCase = true) }
    val selectedText = when {
        selectedPeople.isEmpty() -> "Choose"
        singleSelect -> selectedPeople.first().name
        else -> "${selectedPeople.size} selected"
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, style = MaterialTheme.typography.labelLarge)

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                label = { Text("Search by name") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                // Поисковая строка внутри выпадающего меню
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Type to search...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                filteredPeople.forEach { person ->
                    val isSelected = selectedPeople.contains(person)

                    DropdownMenuItem(
                        text = {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(person.name)
                                if (isSelected) Text("✔")
                            }
                        },
                        onClick = {
                            if (isSelected) {
                                onRemove(person)
                            } else {
                                if (singleSelect) {
                                    selectedPeople.firstOrNull()?.let { onRemove(it) }
                                }
                                onSelect(person)
                            }
                        }
                    )
                }

                if (filteredPeople.isEmpty()) {
                    DropdownMenuItem(
                        text = { Text("No results") },
                        onClick = {},
                        enabled = false
                    )
                }
            }
        }

        // Фильтруем только студентов
        val students = selectedPeople.filter { it.role == Role.STUDENT }

        // Отображение выбранных студентов с прокруткой
        if (students.isNotEmpty()) {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                // Ограничиваем количество видимых элементов
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 200.dp) // Ограничиваем высоту списка студентов
                        .padding(vertical = 4.dp)
                ) {
                    items(students.size) { index ->
                        val person = students[index]

                        // Карточка для каждого выбранного студента
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(person.name, style = MaterialTheme.typography.bodyLarge)

                                IconButton(
                                    onClick = { onRemove(person) }
                                ) {
                                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Remove")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}