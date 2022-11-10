package com.interview.square.core.domain.service

import androidx.compose.material3.ColorScheme
import com.interview.square.core.domain.model.AppColor
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface IThemeManager {
    val isDarkThemeActive: StateFlow<Boolean>
    val defaultTheme: ThemeType
    val availableThemes: Set<ThemeType>
    val currentTheme: SharedFlow<ThemeType>
    val currentColorScheme: StateFlow<ColorScheme>

    fun setTheme(themeType: ThemeType)
    fun setPrimaryColorForUserTheme(color: AppColor)
    fun notifyNightModeChange(darkTheme: Boolean)
}

enum class ThemeType {
    Default,
    Dynamic,
    User
}