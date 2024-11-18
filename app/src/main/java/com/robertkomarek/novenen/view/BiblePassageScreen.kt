package com.robertkomarek.novenen.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PageSize.Fill.calculateMainAxisPageSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.robertkomarek.novenen.R
import com.robertkomarek.novenen.model.Bibelstelle
import com.robertkomarek.novenen.repository.RepositoryBibelstelle
import java.io.IOException
import com.robertkomarek.novenen.ui.theme.PurpleGrey40
import com.robertkomarek.novenen.ui.theme.PaperColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BiblePassageScreen() {
    val context = LocalContext.current
    val repository = RepositoryBibelstelle(context)
    val randomBiblePassage = repository.loadBibelstelle()
    var showDetails by remember { mutableStateOf(false) }// State for card visibility

    Box(modifier = Modifier
        .fillMaxSize()
        .background(PaperColor) // Papercolor
    ){
        if (randomBiblePassage != null) {
            Box(modifier = Modifier.fillMaxSize()) { // Corrected modifier and fillMaxSize
                BiblePassageImage(randomBiblePassage, context)
                if (!showDetails) {
                    BiblePassageButtonCard(
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
fun BiblePassageImage(bibelstelle: Bibelstelle, context: Context) {
    val bitmap = remember(bibelstelle.imagePath) {
        bibelstelle.imagePath?.let { loadBitmapFromPath(context, it) }
    }

    if (bitmap != null) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(3.dp, Color.LightGray, RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop // Adjust content scale as needed
        )
    } else {
        // ... (Existing code for "No Image" placeholder) ...
    }
}


@Composable
fun BiblePassageButtonCard(onClick: () -> Unit, modifier: Modifier = Modifier) {

    val fontRamillas = FontFamily(Font(R.font.tt_ramillas_trial_black, FontWeight.Normal))

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = PurpleGrey40) // Change background color of object 'Hier Bibelstelle ziehen'
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Hier Bibelstelle ziehen".uppercase(),
                style = TextStyle(fontFamily = fontRamillas, fontSize = MaterialTheme.typography.titleLarge.fontSize),
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }
    }
}

@Composable
fun BiblePassageDetails(bibelstelle: Bibelstelle, modifier: Modifier = Modifier) {

    val fontRamillas = FontFamily(Font(R.font.tt_ramillas_trial_black, FontWeight.Normal))
    val initialFontSize = MaterialTheme.typography.bodyLarge.fontSize

    // State to track text size for pinch-to-zoom
    var textSize by remember { mutableStateOf(initialFontSize) }

    Column(
        modifier = modifier
            .background(PaperColor)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .pointerInput(Unit) {
                detectTransformGestures { _, _, zoom, _ ->
                    // Adjust text size dynamically within a reasonable range
                    textSize = (textSize.value * zoom).coerceIn(12f,40f).sp
                }
            },
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = bibelstelle.Buchname.uppercase(), style = TextStyle(fontFamily = fontRamillas,
            fontSize = MaterialTheme.typography.titleLarge.fontSize, color = Color.DarkGray))
        Text(text = bibelstelle.Buch, style = TextStyle(fontFamily = fontRamillas,
            fontSize = MaterialTheme.typography.titleLarge.fontSize, color = Color.DarkGray))
        Text(text = bibelstelle.Kapitel, style = TextStyle(fontFamily = fontRamillas,
            fontSize = MaterialTheme.typography.titleLarge.fontSize, color = Color.DarkGray))
        Spacer(Modifier.height(8.dp))
        Text(text = bibelstelle.Kapiteltext, fontSize = textSize)
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



