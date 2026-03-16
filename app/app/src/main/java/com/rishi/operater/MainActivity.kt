package com.rishi.operater

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Surface
import com.rishi.operater.core.OperatoRRuntime
import com.rishi.operater.navigation.OperatoRNavHost
import com.rishi.operater.ui.theme.OperatoRTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        OperatoRRuntime.initialize(applicationContext)

        val screenCapturePermissionLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                OperatoRRuntime.screenCaptureController.onPermissionResult(
                    resultCode = result.resultCode,
                    data = result.data,
                )
            }

        enableEdgeToEdge()
        setContent {
            OperatoRTheme {
                Surface {
                    OperatoRNavHost(
                        onRequestScreenCapturePermission = {
                            val permissionIntent =
                                OperatoRRuntime.screenCaptureController.createPermissionIntent()
                                    ?: return@OperatoRNavHost

                            screenCapturePermissionLauncher.launch(permissionIntent)
                        },
                    )
                }
            }
        }
    }
}
