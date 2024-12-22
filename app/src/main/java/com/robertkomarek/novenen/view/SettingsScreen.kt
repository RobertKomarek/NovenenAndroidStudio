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
import android.net.Uri
import android.widget.Toast
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext

@Composable
fun SettingsScreen() {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("N O V E N E N", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Entwickelt von", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        val annotatedString = buildAnnotatedString {
            // Add the email address as a link
            withStyle(style = SpanStyle(color = Color.Blue)) {
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
            text = annotatedString,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.clickable {
                // Handle email click
                val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:robert.komarek98@gmail.com")
                }
                context.startActivity(emailIntent)
            }
        )


        Spacer(modifier = Modifier.height(16.dp))
        Text("Die in der App verwendeten Bilder sind gemeinfrei.", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Image(
            painter = painterResource(id = R.drawable.public_domain), // Replace with your icon
            contentDescription = "Public Domain Icon",
            modifier = Modifier.size(48.dp)
        )
    }
}