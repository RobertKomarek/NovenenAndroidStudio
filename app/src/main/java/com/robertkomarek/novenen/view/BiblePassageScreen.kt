package com.robertkomarek.novenen.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.robertkomarek.novenen.R
import com.robertkomarek.novenen.model.Bibelstelle
import com.robertkomarek.novenen.model.Biblepanorama
import com.robertkomarek.novenen.repository.RepositoryBibelstelle
import java.io.IOException
import com.robertkomarek.novenen.ui.theme.PurpleGrey40
import com.robertkomarek.novenen.ui.theme.PaperColor


@Composable
fun BiblePassageScreen() {
    val context = LocalContext.current
    val repository = RepositoryBibelstelle(context)
    val randomBiblePassage = repository.loadBibelstelle()
    val randomBiblepanorama = loadRandomBiblepanoramaFromJson(context)
    var showDetails by remember { mutableStateOf(false) }// State for card visibility

    // LaunchedEffect to reset showDetails when navigating back
    HandleBackPress (showDetails) {
        showDetails = false
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(PaperColor) // Papercolor
        .padding(4.dp) // outer padding
    ){
        if (randomBiblePassage != null) {
            Box(modifier = Modifier
                .fillMaxSize()
                .weight(1f) // Ensures the image gets appropriate space
            ) { // Corrected modifier and fillMaxSize
                if (!showDetails) {
                    BiblePassageImage(randomBiblepanorama, context)
                    //BiblePassageImage(randomBiblePassage, context)

                } else {
                    BiblePassageDetails(
                        bibelstelle = randomBiblePassage,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                    )
                }
            }
        } else {
            Text("No Bibelstelle found", modifier = Modifier.padding(16.dp))
        }

        Spacer(modifier = Modifier.height(4.dp)) // Space between content and button
        if (!showDetails) {
                BiblePassageButtonCard(
                    onClick = { showDetails = true },
                    modifier = Modifier.fillMaxWidth()
                )
        }
    }
}

// Separate composable function for BackHandler
@Composable
fun HandleBackPress(showDetails: Boolean, onBack: () -> Unit) {
    BackHandler(enabled = showDetails) { onBack()
    }
}

@Composable
fun BiblePassageImage(randomBiblepanorama: Biblepanorama?, context: Context) {
    val myFont = FontFamily(Font(R.font.sacramento, FontWeight.Normal))
    val bitmap = remember(randomBiblepanorama?.Image) {
        randomBiblepanorama?.Image?.let {
            loadBitmapFromPath(context, "images_biblepanorama/$it")
        }
    }

    if (bitmap != null) {
        Box {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(3.dp, Color.LightGray, RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop // Adjust content scale as needed
            )

            // Overlapping label
            Text(
                text = randomBiblepanorama?.Description ?: "", // Use Description or empty string if null
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(20.dp)
                    .background(Color.White.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
                    .padding(3.dp),
                style = TextStyle(
                    fontFamily = myFont,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    textAlign = TextAlign.Center,
                    color = Color.Black
                )
            )
        }

    } else {
        // ... (Existing code for "No Image" placeholder) ...
    }
}

fun loadRandomBiblepanoramaFromJson(context: Context): Biblepanorama? {
    val jsonString = context.assets.open("Biblepanorama.json").bufferedReader().use { it.readText() }
    val gson = Gson()
    val listBiblepanoramaType = object : TypeToken<List<Biblepanorama>>() {}.type
    val biblepanoramaList = gson.fromJson<List<Biblepanorama>>(jsonString, listBiblepanoramaType)

    return if (biblepanoramaList.isNotEmpty()) {
        biblepanoramaList.random() // Pick a random item
    } else {
        null // Return null if the list is empty
    }
}

// Function to load a Bitmap from an image path
fun loadBitmapFromPath(context: Context, path: String): Bitmap? {
    return try {
        context.assets.open(path).use { BitmapFactory.decodeStream(it) }
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

@Composable
fun BiblePassageButtonCard(onClick: () -> Unit, modifier: Modifier = Modifier) {
    val fontRamillas = FontFamily(Font(R.font.tt_ramillas_trial_black, FontWeight.Normal))
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(top = 8.dp, bottom = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = PurpleGrey40),
        shape = RoundedCornerShape(32.dp)
    ) {
        Box(
            Modifier
                .wrapContentSize()
                .padding(18.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Hier Bibelstelle ziehen".uppercase(),
                style = TextStyle(fontFamily = fontRamillas,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize),
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }
    }
}

@Composable
fun BiblePassageDetails(bibelstelle: Bibelstelle, modifier: Modifier = Modifier) {

    val fontRamillas = FontFamily(Font(R.font.tt_ramillas_trial_black, FontWeight.Normal))
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .background(PaperColor)
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally // Center-aligns all children
    ) {
        Text(
            text = bibelstelle.Buchname.uppercase(),
            style = TextStyle(
                fontFamily = fontRamillas,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                color = Color.DarkGray
            ),
            textAlign = TextAlign.Center // Ensures text itself is centered
        )
        Text(
            text = bibelstelle.Buch,
            style = TextStyle(
                fontFamily = fontRamillas,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                color = Color.DarkGray
            ),
            textAlign = TextAlign.Center // Ensures text itself is centered
        )
        Text(
            text = bibelstelle.Kapitel,
            style = TextStyle(
                fontFamily = fontRamillas,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                color = Color.DarkGray
            ),
            textAlign = TextAlign.Center // Ensures text itself is centered
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = bibelstelle.Kapiteltext,
            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
            textAlign = TextAlign.Justify // Ensures text itself is centered
        )
    }
}





