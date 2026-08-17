package com.aesguard.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AESGuardTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Text(
                        text = "🛡️ AESGuard\n${stringFromJNI()}",
                        fontSize = 24.sp
                    )
                }
            }
        }
    }
}