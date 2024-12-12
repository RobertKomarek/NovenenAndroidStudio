package com.robertkomarek.novenen.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import com.robertkomarek.novenen.model.Novene
import com.robertkomarek.novenen.repository.RepositoryNovenen

@Composable
fun SettingsScreen() {
    val repository = RepositoryNovenen(LocalContext.current)
    val novenenList = remember { mutableStateListOf<Novene>() }
    var selectedNovenen by remember { mutableStateOf<Novene?>(novenenList.firstOrNull()) }
    val loadedNovenen = repository.loadNovenenData(LocalContext.current)
        .distinctBy { it.Novenenname }
        .sortedBy { it.Novenenname }
    novenenList.addAll(loadedNovenen)
    val novenenNames = novenenList.map { it.Novenenname }

    Spinner(
        items = novenenNames,
        selectedItem = selectedNovenen?.Novenenname ?: "", // Display selected Novenenname
        onItemSelected = { selectedNovenenname ->
            selectedNovenen = novenenList.find { it.Novenenname ==
                    selectedNovenenname } // Find selected Novene object
        }
    )
}

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Spinner(
        items: List<String>,
        selectedItem: String,
        onItemSelected: (String) -> Unit
    ) {
        var expanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedItem,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .clickable { expanded = true } // Add clickable to modifier to re-expand
                    .onFocusChanged { focusState ->
                        //Log.d("Spinner", "Focus changed: ${focusState.isFocused}")
                        if (focusState.isFocused) {
                            expanded = true
                        }
                    }
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item) },
                        onClick = {
                            onItemSelected(item)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
