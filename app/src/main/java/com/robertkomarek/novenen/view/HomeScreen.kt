package com.robertkomarek.novenen.view


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.robertkomarek.novenen.R
import com.robertkomarek.novenen.annotation.DrawableResource
import com.robertkomarek.novenen.model.Novene
import com.robertkomarek.novenen.repository.RepositoryNovenen
import kotlin.jvm.java
import androidx.compose.ui.text.font.Font
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun HomeScreen(navController: NavHostController) {
    val context = LocalContext.current
    val repository = RepositoryNovenen(context)
    val novenenList = remember { mutableStateListOf<Novene>() }

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

    LazyColumn {
        items(novenenList) { novene ->
            NoveneItem(novene) {
                navController.navigate("details/${novene.Novenenname}")
            }
        }
    }
}

@Composable
fun NoveneItem(novene: Novene, onClick: () -> Unit) {
    val imageId = getDrawableResId(novene, "Bild")
    val titleFont = FontFamily(Font(R.font.tt_ramillas_initials_trialblack, FontWeight.Normal))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        if (imageId != null) {
            Row(
                modifier = Modifier
                    .padding(16.dp), // Add padding inside the card
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Image with contentScale and padding to shift the image down
                AsyncImage(
                    model = imageId,
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = novene.Novenenname,
                        fontWeight = FontWeight.Bold,
                        style = TextStyle(fontFamily = titleFont, fontSize = MaterialTheme.typography.titleLarge.fontSize)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = novene.Zeitraum,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        } else {
            // Handle resource not found, e.g., display a placeholder image
            Image(
                painter = painterResource(id = R.drawable.fatima),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}

fun getDrawableResId(novene: Novene, fieldName: String): Int? {
    return try {
        val field = novene::class.java.getDeclaredField(fieldName)
        field.isAccessible = true  // Allow access to private field

        val annotation = field.getAnnotation(DrawableResource::class.java)

        if (annotation != null) {
            val resourceClass = annotation.value.java

            // Safely get the field's value as a String
            val resourceNameWithExtension = field.get(novene) as? String
            if (resourceNameWithExtension.isNullOrEmpty()) {
                // Log or handle the case where the resource name is missing
                println("Resource name is null or empty for field: $fieldName")
                return null
            }

            // Remove extension if present
            val resourceName = resourceNameWithExtension.substringBeforeLast(".")

            // Get the ID field using reflection
            val idField = resourceClass.getDeclaredField(resourceName)
            idField.getInt(null) // Get the resource ID
        } else {
            // Log or handle the case where no annotation was found
            println("No DrawableResource annotation found for field: $fieldName")
            null
        }
    } catch (e: NoSuchFieldException) {
        println("Field not found: ${e.message}")
        null
    } catch (e: IllegalAccessException) {
        println("Illegal access to field: ${e.message}")
        null
    } catch (e: Exception) {
        println("Error retrieving drawable resource ID: ${e.message}")
        null
    }
}