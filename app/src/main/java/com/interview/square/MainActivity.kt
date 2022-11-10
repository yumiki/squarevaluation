package com.interview.square

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.interview.square.core.data.service.LocalThemeManager
import com.interview.square.core.data.service.ThemeManager
import com.interview.square.core.ui.theme.SquareTheme
import com.interview.square.feature_square_move.ui.SquareMovementScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class MainActivity : ComponentActivity() {
    private val themeManager = ThemeManager(CoroutineScope(SupervisorJob() + Dispatchers.Default))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val currentTheme by themeManager.currentTheme.collectAsState(themeManager.defaultTheme)
            CompositionLocalProvider(
                LocalThemeManager provides themeManager
            ) {
                SquareTheme(currentTheme = currentTheme) {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        SquareMovementScreen()
                    }
                }
            }

        }
    }
}