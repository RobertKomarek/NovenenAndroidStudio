package com.robertkomarek.novenen.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.robertkomarek.novenen.model.Bibelstelle
import com.robertkomarek.novenen.repository.RepositoryBibelstelle
import java.io.IOException


@Composable
fun ImpressumScreen() {
    val context = LocalContext.current
    val repository = RepositoryBibelstelle(context)
    val randomBibelstelle = repository.loadBibelstelle()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Bibelstellen") })
        }
    ) { paddingValues -> // Capture padding from Scaffold
        if (randomBibelstelle != null) {
            // Display the single random Bibelstelle
            BibelstelleItem(
                bibelstelle = randomBibelstelle,
                context = context,
                contentPadding = paddingValues // Pass the padding to the item
            )
        } else {
            // Handle the case where there is no Bibelstelle (e.g., display a placeholder)
            Text("No Bibelstelle found", modifier = Modifier.padding(paddingValues))
        }
    }
}

@Composable
fun BibelstelleItem(
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
        Column {
            Text(text = bibelstelle.Kapiteltext, style = MaterialTheme.typography.body1)
            Text(text = bibelstelle.Buchname, style = MaterialTheme.typography.caption)
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
