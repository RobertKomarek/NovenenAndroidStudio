package com.robertkomarek.novenen.view

import android.app.TimePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import java.time.format.DateTimeFormatter
import com.robertkomarek.novenen.repository.RepositoryNovenen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.robertkomarek.novenen.model.Novene
import com.robertkomarek.novenen.R
import java.time.LocalDate
import java.time.LocalTime
import java.util.Locale
import com.robertkomarek.novenen.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen() {
    val repository = RepositoryNovenen(LocalContext.current)
    val novenenList = remember { mutableStateListOf<Novene>() }
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    var selectedNovenenname by remember { mutableStateOf("Fatima") } // Default Novenenname
    var selectedNovene by remember { mutableStateOf<Novene?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedTime by remember { mutableStateOf(LocalTime.now()) }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    val dateFormatter = DateTimeFormatter.ofPattern("dd.MMMM yyyy", Locale.GERMANY)
    var imageResource by remember { mutableStateOf(R.drawable.fatima) } //Default image
    val titleFont = FontFamily(Font(R.font.tt_ramillas_trial_black, FontWeight.Normal))

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Purple80, Color.LightGray, Color.White),
                    start = Offset(0f, 0f),
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                )
            )
    ) {
        Column {
            Image(
                painter = painterResource(id = R.drawable.bell),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(vertical = 32.dp)
            )
//        Box(
//            modifier = Modifier.fillMaxWidth(),
//            contentAlignment = Alignment.Center
//        ){
//            Text(  modifier = Modifier.padding(horizontal = 16.dp),
//                text = "Speichern Sie eine Erinnerung über eine Novene!",
//                style = TextStyle(fontFamily = titleFont, fontSize = MaterialTheme.typography.headlineMedium.fontSize),
//            )
//        }
            val imageWidth =
                LocalConfiguration.current.screenWidthDp.dp - (38.dp * 2) // Caluclate width
            Image(
                painter = painterResource(id = imageResource),
                contentDescription = null,
                modifier = Modifier
                    .height(imageWidth)
                    .padding(horizontal = 48.dp, vertical = 16.dp)
                    .fillMaxWidth()
                    .clip(CircleShape)
                    .border(3.dp, Color.LightGray, CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

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
                        .padding(horizontal = 16.dp)
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

                                // Update imageResource based on selectedNovene
                                val imageName = novene.Bild.removeSuffix(".jpg")
                                imageResource = context.resources.getIdentifier(
                                    imageName,
                                    "drawable",
                                    context.packageName
                                )
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            } ?: TextField( // Provide default TextField when selectedNovene is null
                value = "4. bis 12. Mai", // Replace with your desired default value
                onValueChange = { },
                readOnly = true,
                label = { Text("Zeitraum") },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Date Picker
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = { showDatePicker = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PurpleGrey40, // Background color
                        contentColor = Color.White // Text color
                    ))
                {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_calendar_month_24),
                        contentDescription = null,
                        modifier = Modifier
                            .size(48.dp)
                            .zIndex(1f)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Kalender aufrufen")
                    //Text("Erinnerung am (hier tippen): ${selectedDate.format(dateFormatter)}")
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
                        // Show TimePickerDialog after date selection
                        showTimePicker = true
                    },
                    onDismiss = { showDatePicker = false }
                )
            }
            // Time Picker
            if (showTimePicker) {
                val timePickerDialog = TimePickerDialog(
                    context,
                    { _, hourOfDay, minute ->
                        selectedTime = LocalTime.of(hourOfDay, minute)
                        showTimePicker = false
                        // Show confirmation dialog
                        showConfirmationDialog = true
                    },
                    selectedTime.hour,
                    selectedTime.minute,
                    true
                )
                timePickerDialog.show()
            }
        } // END OF COLUMN
    } // END OF BOX

    if (showConfirmationDialog) {
        val formattedDateTime = selectedDate.format(dateFormatter) + " " + selectedTime.format(DateTimeFormatter.ofPattern("HH:mm"))
        AlertDialog(
            onDismissRequest = { showConfirmationDialog = false },
            title = { Text("ERINNERUNG") },
            text = { Text("Sie erhalten eine Erinnerung für die Novene $selectedNovenenname am: $formattedDateTime Uhr?") },
            confirmButton = {
                TextButton(onClick = {
                    showConfirmationDialog = false
                }) {
                    Text("Bestätigen")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmationDialog = false }) {
                    Text("Abbrechen")
                }
            }
        )
    }
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



