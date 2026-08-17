package com.aesguard.app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aesguard.app.ui.theme.AESGuardTheme

class MainActivity : ComponentActivity() {

    companion object {
        init {
            System.loadLibrary("aesguard")
        }
    }

    external fun stringFromJNI(): String
    external fun testFileIO(path: String): String

    private var cppMessage: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cppMessage = stringFromJNI()
        setContent {
            AESGuardTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(cppMessage)
                }
            }
        }
    }
}

@Composable
fun MainScreen(cppMessage: String) {
    val context = LocalContext.current
    var selectedFilePath by remember { mutableStateOf("No file selected") }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            selectedFilePath = "File: ${it.lastPathSegment}"
            Toast.makeText(context, "File selected!", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "🛡️", fontSize = 64.sp)
        Text(text = "AESGuard", fontSize = 32.sp)
        Text(text = "Secure File Encryption", fontSize = 14.sp)
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = "✅ $cppMessage", fontSize = 12.sp)
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = selectedFilePath, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                filePickerLauncher.launch("*/*")
            }
        ) {
            Text("📁 Pick File")
        }
    }
}