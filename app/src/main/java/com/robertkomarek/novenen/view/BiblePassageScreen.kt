package com.robertkomarek.novenen.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.robertkomarek.novenen.model.Bibelstelle
import com.robertkomarek.novenen.repository.RepositoryBibelstelle
import java.io.IOException


@Composable
fun BiblePassageImage(bibelstelle: Bibelstelle, context: Context) {
    val bitmap = remember(bibelstelle.imagePath) {
        bibelstelle.imagePath?.let { loadBitmapFromPath(context, it) }
    }

    if (bitmap != null) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Adjust content scale as needed
        )
    } else {
        // ... (Existing code for "No Image" placeholder) ...
    }
}


@Composable
fun BiblePassageCard(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Text(
            text = "Hier Bibelstelle ziehen",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Center

        )
    }
}

@Composable
fun BiblePassageDetails(bibelstelle: Bibelstelle, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = bibelstelle.Kapitel, style = MaterialTheme.typography.titleMedium)
        Text(text = bibelstelle.Buch, style = MaterialTheme.typography.titleMedium)
        Text(text = bibelstelle.Buchname, style = MaterialTheme.typography.titleSmall)
        Text(text = bibelstelle.Kapiteltext, style = MaterialTheme.typography.bodyLarge)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BiblePassageScreen() {
    val context = LocalContext.current
    val repository = RepositoryBibelstelle(context)
    val randomBiblePassage = repository.loadBibelstelle()
    var showDetails by remember { mutableStateOf(false) }// State for card visibility

    Box(modifier = Modifier.fillMaxSize())
    {
        if (randomBiblePassage != null) {
            Box(modifier = Modifier.fillMaxSize()) { // Corrected modifier and fillMaxSize
                BiblePassageImage(randomBiblePassage, context)
                if (!showDetails) {
                    BiblePassageCard(
                        onClick = { showDetails = true },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(16.dp)
                    )
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
    }
}

    @Composable
    fun BiblePassageItem(
        bibelstelle: Bibelstelle,
        context: Context,
        contentPadding: PaddingValues // Receive padding parameter
    ) {
        val bitmap = remember(bibelstelle.imagePath) {
            bibelstelle.imagePath?.let { loadBitmapFromPath(context, it) }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Display the image
            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.size(64.dp)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(Color.Gray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No Image", color = Color.White, textAlign = TextAlign.Center)
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Display the text
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
            )
            {
                Text(text = bibelstelle.Kapitel, style = MaterialTheme.typography.titleMedium)
                Text(text = bibelstelle.Buch, style = MaterialTheme.typography.titleMedium)
                Text(text = bibelstelle.Buchname, style = MaterialTheme.typography.titleSmall)
                Text(text = bibelstelle.Kapiteltext, style = MaterialTheme.typography.bodyLarge)
            }
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

