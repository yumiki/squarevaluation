package com.interview.square.core.domain.service

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.SharedFlow

interface IThemeManager {
    val defaultTheme: ThemeType
    val availableThemes: Set<ThemeType>
    val currentTheme: SharedFlow<ThemeType>

    fun setTheme(themeType: ThemeType)
    fun setPrimaryColorForUserTheme(color: Color)
}

enum class ThemeType {
    Default,
    Dynamic,
    User
}