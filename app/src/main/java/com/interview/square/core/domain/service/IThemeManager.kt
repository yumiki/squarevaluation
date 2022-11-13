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
    val defaultColorScheme: ColorScheme
    val availableThemes: Set<ThemeType>
    val currentTheme: SharedFlow<ThemeType>
    val currentColorScheme: SharedFlow<ColorScheme>

    suspend fun setTheme(themeType: ThemeType)
    fun setPrimaryColorForUserTheme(color: AppColor, variantColor: AppColor? = null)
    fun notifyNightModeChange(darkTheme: Boolean)
}

object DefaultThemeManager: IThemeManager {
    override val isDarkThemeActive: StateFlow<Boolean> = MutableStateFlow(false)
    override val defaultTheme: ThemeType = ThemeType.Default
    override val defaultColorScheme: ColorScheme = LightColorScheme
    override val availableThemes: Set<ThemeType> = setOf(ThemeType.Dynamic, ThemeType.Default, ThemeType.User)
    override val currentTheme: SharedFlow<ThemeType> = MutableStateFlow(ThemeType.Dynamic)
    override val currentColorScheme: SharedFlow<ColorScheme> = MutableStateFlow(defaultColorScheme)

    override suspend fun setTheme(themeType: ThemeType) {
        TODO("Not yet implemented")
    }

    override fun setPrimaryColorForUserTheme(color: AppColor, variantColor: AppColor?) {
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