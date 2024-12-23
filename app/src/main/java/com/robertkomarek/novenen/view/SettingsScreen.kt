package com.robertkomarek.novenen.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.robertkomarek.novenen.R
import android.content.Intent
//import android.graphics.fonts.FontStyle
import android.net.Uri
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration

@Composable
fun SettingsScreen() {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val titleFont = FontFamily(Font(R.font.sacramento, FontWeight.Bold))
    val iconSize = 55.dp
    val iconSpacing = 16.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.novenen_icon), // Replace with your icon
            contentDescription = "Dove Icon",
            modifier = Modifier
                .size(130.dp)
                .clip(CircleShape)
                .border(
                    2.dp,
                    Color.Black,
                    CircleShape)
                 // Add border
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("N O V E N E N",
            style = TextStyle(
                fontFamily = titleFont,
                fontSize = MaterialTheme.typography.displayMedium.fontSize,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("entwickelt von", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        val annotatedStringMail = buildAnnotatedString {
            // Add the email address as a link
            withStyle(style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {
                append("robert.komarek98@gmail.com")
                addStringAnnotation(
                    tag = "URL",
                    annotation = "mailto:robert.komarek98@gmail.com",
                    start = 0,
                    end = "robert.komarek98@gmail.com".length
                )
            }
        }

        Text(
            text = annotatedStringMail,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.clickable {
                // Handle email click
                val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:robert.komarek98@gmail.com")
                }
                context.startActivity(emailIntent)
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        val annotatedLinkString = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {
                append("GitHub Repository")
                addStringAnnotation(
                    tag = "URL",
                    annotation = "https://github.com/RobertKomarek/NovenenAndroidStudio.git",
                    start = 0,
                    end = "GitHub Repository".length
                )
            }
        }
        Text(
            text = annotatedLinkString,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.clickable {
                // Handle link click (e.g., open in browser)
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/RobertKomarek/NovenenAndroidStudio.git"))
                context.startActivity(intent)
            }
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text("Die in der App verwendeten Bilder sind gemeinfrei (Public Domain) und daher nicht urheberrecthlich geschützt, sodass sie frei genutzt, kopiert, verändert und verbreitet werden können.", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.public_domain), // Replace with your icon
                contentDescription = "Public Domain Icon",
                modifier = Modifier.size(iconSize)
            )
            Spacer(modifier = Modifier.width(iconSpacing))
            Image(
                painter = painterResource(id = R.drawable.copyright), // Replace with your icon
                contentDescription = "Copyright Icon",
                modifier = Modifier.size(iconSize)
            )
            Spacer(modifier = Modifier.width(iconSpacing))
            Image(
                painter = painterResource(id = R.drawable.public_domain_0), // Replace with your icon
                contentDescription = "Public Domain Icon 0",
                modifier = Modifier.size(iconSize)
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        Text("LOURDES-NOVENE mit freundlicher Genehmigung von Deutsche Hospitalité e.v. - Notre Dame de Lourdes e.V.",
            style = TextStyle (
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                fontStyle = FontStyle.Italic
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("MEDJUGORJE-NOVENE mit freundlicher Genehmigung von Medjugorje Deutschland e.V.",
            style = TextStyle (
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                fontStyle = FontStyle.Italic
            )
        )
    }
}