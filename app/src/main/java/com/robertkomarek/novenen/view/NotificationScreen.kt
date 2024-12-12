package com.robertkomarek.novenen.view


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import java.time.format.DateTimeFormatter
import com.google.android.material.datepicker.MaterialDatePicker
import com.robertkomarek.novenen.repository.RepositoryNovenen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.robertkomarek.novenen.model.Novene
import com.robertkomarek.novenen.R
import java.time.LocalDate
import java.time.LocalTime
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen() {
    val repository = RepositoryNovenen(LocalContext.current)
    val novenenList = remember { mutableStateListOf<Novene>() }
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    var selectedNovenenname by remember { mutableStateOf("Fatima") }
    var selectedNovene by remember { mutableStateOf<Novene?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedTime by remember { mutableStateOf(LocalTime.now()) }
    val datePicker = remember { MaterialDatePicker.Builder.datePicker().build() }
    //var buttonText by remember { mutableStateOf("Datum auswählen") }
    val dateFormatter = DateTimeFormatter.ofPattern("dd.MMMM yyyy", Locale.GERMANY)

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val loadedNovenen = repository.loadNovenenData(context)
            val processedNovenen = loadedNovenen
                .distinctBy { it.Novenenname }
                .sortedBy { it.Novenenname }
            withContext(Dispatchers.Main) {
                novenenList.addAll(processedNovenen)
            }
        }
    }

    Column {
        Image(
            painter = painterResource(id = R.drawable.notification_icon),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(38.dp)
        )
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                readOnly = true,
                value = selectedNovenenname,
                onValueChange = { },
                label = { Text("Novene auswählen") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                novenenList.forEach { novene ->
                    DropdownMenuItem(
                        text = { Text(novene.Novenenname) },
                        onClick = {
                            selectedNovenenname = novene.Novenenname
                            selectedNovene = novene
                            expanded = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        selectedNovene?.let {
            TextField(
                value = it.Zeitraum,
                onValueChange = {},
                readOnly = true,
                label = { Text("Zeitraum") },
                modifier = Modifier.fillMaxWidth()
            )
        } ?: TextField( // Provide default TextField when selectedNovene is null
            value = "4. bis 12. Mai", // Replace with your desired default value
            onValueChange = { },
            readOnly = true,
            label = { Text("Zeitraum") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        // Date Picker
        Box(
            modifier = Modifier
                .fillMaxWidth().padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = { showDatePicker = true }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_calendar_month_24),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .zIndex(1f)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Erinnerung am (hier tippen): ${selectedDate.format(dateFormatter)}")
            }
        }

        // Show the DatePickerDialog
        if (showDatePicker) {
            AndroidDatePickerDialog(
                context = context,
                initialDate = selectedDate,
                onDateSelected = { date ->
                    selectedDate = date
                    showDatePicker = false
                },
                onDismiss = { showDatePicker = false }
            )
        }


    } // END OF COLUMN
}


@Composable
fun AndroidDatePickerDialog(
    context: android.content.Context,
    initialDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    DisposableEffect(Unit) {
        val datePickerDialog = android.app.DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                onDateSelected(LocalDate.of(year, month + 1, dayOfMonth))
            },
            initialDate.year,
            initialDate.monthValue - 1,
            initialDate.dayOfMonth
        )
        datePickerDialog.setOnDismissListener { onDismiss() }
        datePickerDialog.show()

        onDispose {
            datePickerDialog.dismiss()
        }
    }
}



