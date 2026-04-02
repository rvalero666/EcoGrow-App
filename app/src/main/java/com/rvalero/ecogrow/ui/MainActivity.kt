package com.rvalero.ecogrow.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.compose.EcoGrowTheme
import com.rvalero.ecogrow.ui.registerScreen.RegisterViewModel
import com.rvalero.ecogrow.ui.registerScreen.RegisterViewModelScreen
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EcoGrowTheme {
                val registerViewModel: RegisterViewModel = koinViewModel()
                RegisterViewModelScreen(
                    viewModel = registerViewModel,
                    onNavigateToLogin = { }
                )
            }
        }
    }
}

