package com.erencol.sermon.view.about

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erencol.sermon.R

class AboutActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = stringResource(id = R.string.sermons),
                                    color = Color.White
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = { onBackPressedDispatcher.onBackPressed() }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back",
                                        tint = Color.White
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color(0xFF000000)
                            )
                        )
                    }
                ) { innerPadding ->
                    AboutScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun AboutScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // Sermons Title
        Text(
            text = stringResource(id = R.string.sermons),
            fontSize = 32.sp,
            modifier = Modifier.padding(start = 34.dp, top = 34.dp, bottom = 12.dp)
        )

        // About Text
        Text(
            text = stringResource(id = R.string.about_text),
            modifier = Modifier.padding(horizontal = 34.dp, vertical = 0.dp).padding(bottom = 24.dp)
        )

        // Other Apps Header
        Text(
            text = stringResource(id = R.string.other_apps),
            fontSize = 24.sp,
            modifier = Modifier.padding(start = 34.dp, bottom = 32.dp)
        )

        // Drawing App Button
        AppLinkButton(
            imageRes = R.drawable.drawing_app_logo,
            appName = stringResource(id = R.string.app1_name),
            url = "https://play.google.com/store/apps/details?id=com.mobizoe.drawing"
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Voice Notes App Button
        AppLinkButton(
            imageRes = R.drawable.senote,
            appName = stringResource(id = R.string.app2_name), // Using app2_name based on XML logic
            url = "https://play.google.com/store/apps/details?id=com.mobizoe.senote",
            contentDescription = stringResource(id = R.string.app2_name)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Donate Section
        Column(modifier = Modifier.padding(start = 34.dp)) {
            Text(
                text = stringResource(id = R.string.donate),
                fontSize = 18.sp
            )
            Text(
                text = stringResource(id = R.string.donate_explain),
                modifier = Modifier.padding(top = 8.dp, end = 34.dp)
            )
            
            // IBAN
            val ibanText = stringResource(id = R.string.iban)
            val ibanCopyText = stringResource(id = R.string.iban_copy_text)
            Text(
                text = ibanText,
                modifier = Modifier
                    .padding(top = 34.dp, end = 34.dp)
                    .fillMaxWidth() // To allow center gravity if needed, but existing was weird. "gravity center" in Linear? No, TextView had wrap_content and layout_marginRight=34dp. But let's check gravity. 
                    // XML: android:layout_width="wrap_content", android:gravity="center". Gravity center in wrap_content does nothing unless single line?
                    // But it has a click listener.
                    .clickable {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Hutbeler Iban", ibanCopyText)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "Kopyalandı.", Toast.LENGTH_SHORT).show()
                    },
                textAlign = TextAlign.Center // Just in case
            )
        }
        
        Spacer(modifier = Modifier.height(34.dp)) // Extra padding at bottom
    }
}

@Composable
fun AppLinkButton(
    imageRes: Int,
    appName: String,
    url: String,
    contentDescription: String? = null
) {
    val context = LocalContext.current
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 34.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFAAAAAA)) // gray_bg
            .clickable {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = contentDescription,
            modifier = Modifier.size(64.dp),
            contentScale = ContentScale.Fit
        )
        
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
             Text(
                 text = appName,
                 fontSize = 18.sp,
                 fontWeight = FontWeight.Bold,
                 modifier = Modifier
                     .padding(start = 12.dp)
                     .weight(3f)
             )
             
             Image(
                 painter = painterResource(id = R.drawable.play_image),
                 contentDescription = null,
                 modifier = Modifier
                     .width(20.dp)
                     .height(40.dp)
                     .padding(start = 12.dp)
                     .weight(1f),
                 contentScale = ContentScale.Fit
             )
        }
    }
}
