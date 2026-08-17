package com.aesguard.app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
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
    external fun aesEncrypt(input: String, password: String): String
    external fun aesDecrypt(input: String, password: String): String
    external fun aesEncryptFile(inputPath: String, outputPath: String, password: String): String

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
                    MainScreen(cppMessage, this)
                }
            }
        }
    }
}

@Composable
fun MainScreen(cppMessage: String, activity: MainActivity) {
    val context = LocalContext.current
    var selectedFilePath by remember { mutableStateOf("No file selected") }
    var selectedFileUri by remember { mutableStateOf<android.net.Uri?>(null) }
    var password by remember { mutableStateOf("") }
    var isEncryptMode by remember { mutableStateOf(true) }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            selectedFileUri = it
            selectedFilePath = "File: ${it.lastPathSegment}"
            Toast.makeText(context, "File selected!", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "🛡️", fontSize = 64.sp)
        Text(text = "AESGuard", fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Text(text = "Secure File Encryption", fontSize = 14.sp)

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "✅ $cppMessage", fontSize = 11.sp)

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { isEncryptMode = true },
                modifier = Modifier.weight(1f).padding(end = 4.dp)
            ) {
                Text("🔒 Encrypt")
            }
            Button(
                onClick = { isEncryptMode = false },
                modifier = Modifier.weight(1f).padding(start = 4.dp)
            ) {
                Text("🔓 Decrypt")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = selectedFilePath, fontSize = 12.sp)

        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = { filePickerLauncher.launch("*/*") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("📁 Pick File")
        }

        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (password.isNotEmpty()) {
                    try {
                        if (selectedFileUri != null) {
                            val inputPath = selectedFileUri.toString()
                            val outputPath = inputPath + ".enc"
                            val mode = if (isEncryptMode) "🔒 Encrypt" else "🔓 Decrypt"
                            val result = if (isEncryptMode) {
                                activity.aesEncryptFile(inputPath, outputPath, password)
                            } else {
                                activity.aesEncryptFile(outputPath, inputPath, password)
                            }
                            Toast.makeText(context, "$mode: $result", Toast.LENGTH_LONG).show()
                        } else {
                            val testText = "Hello AESGuard!"
                            val mode = if (isEncryptMode) "🔒 Encrypt" else "🔓 Decrypt"
                            val result = if (isEncryptMode) {
                                activity.aesEncrypt(testText, password)
                            } else {
                                activity.aesDecrypt(testText, password)
                            }
                            Toast.makeText(context, "$mode: $result", Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Enter password first!", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                if (isEncryptMode) "🔒 Encrypt File" else "🔓 Decrypt File",
                fontSize = 18.sp
            )
        }
    }
}