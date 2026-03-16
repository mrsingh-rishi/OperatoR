package com.rishi.operater

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import com.rishi.operater.navigation.OperatoRNavHost
import com.rishi.operater.ui.theme.OperatoRTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OperatoRTheme {
                Surface {
                    OperatoRNavHost()
                }
            }
        }
    }
}
