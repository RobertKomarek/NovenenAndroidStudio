package com.robertkomarek.novenen.view

import BookmarkDataStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.robertkomarek.novenen.R
import com.robertkomarek.novenen.annotation.DrawableResource
import com.robertkomarek.novenen.model.Novene
import com.robertkomarek.novenen.repository.RepositoryNovenen

@Composable
fun DetailScreenNovene(novenenname: String) {
    val context = LocalContext.current
    val repository = RepositoryNovenen(context)
    val noveneDetails = remember { mutableStateListOf<Novene>() }
    val bookmarkDataStore = remember { BookmarkDataStore(context) }
    val bookmarkedNoveneTag = remember { mutableStateOf(bookmarkDataStore.getBookmarkedNoveneTag(novenenname)) }

    LaunchedEffect(novenenname) {
        val loadedDetails = repository.loadNovenenData(context)
        noveneDetails.addAll(loadedDetails.filter { it.Novenenname == novenenname })
    }

    val expandedStatesNovene = remember { mutableStateMapOf<Int, Boolean>() } //state for unfolding

    LazyColumn {
        item {
            val imageId = getDrawableResIdentification(noveneDetails, "Bild")
            // Add Image as header
            if (imageId != null) {
                Image(
                    painter = painterResource(id = imageId),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(350.dp),
                    contentScale = ContentScale.Crop
                )
            }
            //Add distance between Image and following text items
            Spacer(modifier = Modifier.height(8.dp))// Add spacer below Image
        }

        // List items Novene
        itemsIndexed(noveneDetails) { index, details ->
            val isExpanded = expandedStatesNovene[index] ?: false
            val isBookmarked = details.Tag == bookmarkedNoveneTag.value // Use Tag for bookmarking

            DetailsItem(
                details,
                isExpanded,
                isBookmarked,
                index,
                onClick = { expandedStatesNovene[index] = !isExpanded },
                onBookmarkClick = {
                    if (isBookmarked) {
                        bookmarkedNoveneTag.value = null
                    } else {
                        bookmarkedNoveneTag.value = details.Tag // Set the new bookmark
                        //clear any previous bookmarks
                    }
                    bookmarkDataStore.setBookmarkedNoveneTag(novenenname, bookmarkedNoveneTag.value)
                }
            )
        }
    }
}


@Composable
fun DetailsItem(
    novene: Novene,
    isExpanded: Boolean,
    isBookmarked: Boolean,
    index: Int,
    onClick: () -> Unit,
    onBookmarkClick: () -> Unit
) {

    val titleFont = FontFamily(Font(R.font.tt_ramillas_trial_black, FontWeight.Normal))
    val titleFontInitials = FontFamily(Font(R.font.tt_ramillas_initials_trialblack, FontWeight.Normal))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .clickable { onClick() }
            //Apply background color conditionally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = novene.Tag,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        fontFamily = titleFont,
                        fontSize = MaterialTheme.typography.headlineSmall.fontSize
                    )
                )

                // Bookmark icon
                if (index > 0) {
                    Icon(
                        painter = painterResource(
                            id = if (isBookmarked) R.drawable.baseline_bookmark_24 else
                                R.drawable.baseline_bookmark_border_24
                        ),
                        contentDescription = "Bookmark",
                        tint = if (isBookmarked) Color.Red else LocalContentColor.current,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onBookmarkClick() }
                    )
                }
            }

                Spacer(modifier = Modifier.height(8.dp))

                // Conditionally display Tagestext based on state in DetailScreenNovene
                if (isExpanded) { // Access isExpanded from details object
                    Text(
                        text = novene.Tagesueberschrift,
                        style = TextStyle(fontSize = MaterialTheme.typography.headlineSmall.fontSize)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = novene.Tagestext,
                        style = TextStyle(fontSize = MaterialTheme.typography.bodyLarge.fontSize)
                    )
                    //Damit die  Karte in der Listview durch den Spacer keinen Gap unten aufweist, wenn es keine Litanei gibt
                    if(novene.Litaneiueberschrift.isNotEmpty()) {

                        Spacer(modifier = Modifier.height(28.dp))

                        Text(
                            text = novene.Litaneiueberschrift,
                            style = TextStyle(fontSize = MaterialTheme.typography.headlineSmall.fontSize, fontFamily = titleFontInitials )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = novene.Litanei,
                            style = TextStyle(fontSize = MaterialTheme.typography.bodyLarge.fontSize)
                        )
                    }
                }
            }
        }
    }



fun getDrawableResIdentification(noveneList: List<Novene>, fieldName: String): Int? {
    val novene = noveneList.firstOrNull() ?: return null

    return try {
        // Use the 'novene' object for reflection
        val field = novene::class.java.getDeclaredField(fieldName)
        field.isAccessible = true

        val annotation = field.getAnnotation(DrawableResource::class.java)

        if (annotation != null) {
            val resourceClass = annotation.value.java

            val resourceNameWithExtension = field.get(novene) as? String
            if (resourceNameWithExtension.isNullOrEmpty()) {
                println("Resource name is null or empty for field: $fieldName")
                return null
            }

            val resourceName = resourceNameWithExtension.substringBeforeLast(".")

            val idField = resourceClass.getDeclaredField(resourceName)
            idField.getInt(null)
        } else {
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


