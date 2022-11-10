package com.interview.square.core.domain.service

import androidx.compose.material3.ColorScheme
import com.interview.square.core.domain.model.AppColor
import com.interview.square.core.ui.theme.LightColorScheme
import kotlinx.coroutines.flow.MutableStateFlow
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

object DefaultThemeManager: IThemeManager {
    override val isDarkThemeActive: StateFlow<Boolean> = MutableStateFlow(false)
    override val defaultTheme: ThemeType = ThemeType.Default
    override val availableThemes: Set<ThemeType> = setOf(ThemeType.Dynamic, ThemeType.Default, ThemeType.User)
    override val currentTheme: SharedFlow<ThemeType> = MutableStateFlow(ThemeType.Dynamic)
    override val currentColorScheme: StateFlow<ColorScheme> = MutableStateFlow(LightColorScheme)

    override fun setTheme(themeType: ThemeType) {
        TODO("Not yet implemented")
    }

    override fun setPrimaryColorForUserTheme(color: AppColor) {
        TODO("Not yet implemented")
    }

    override fun notifyNightModeChange(darkTheme: Boolean) {
        TODO("Not yet implemented")
    }
}

enum class ThemeType {
    Default,
    Dynamic,
    User
}