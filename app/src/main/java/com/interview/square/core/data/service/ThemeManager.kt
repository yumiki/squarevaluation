package com.interview.square.core.data.service

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import com.interview.square.core.domain.model.AppColor
import com.interview.square.core.domain.repository.IThemeRepository
import com.interview.square.core.domain.service.IThemeManager
import com.interview.square.core.domain.service.ThemeType
import com.interview.square.core.ui.theme.DarkColorScheme
import com.interview.square.core.ui.theme.LightColorScheme
import com.interview.square.core.ui.theme.UserColorScheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ThemeManager(
    private val context: Context,
    private val themeRepository: IThemeRepository,
    private val externalScope: CoroutineScope
) : IThemeManager {
    init {
        Log.v("ThemeManager", "new instance $this")
    }

    private val _isDarkThemeActive =
        MutableStateFlow(context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES)
    override val isDarkThemeActive: StateFlow<Boolean> = _isDarkThemeActive.asStateFlow()

    override val defaultTheme: ThemeType =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) ThemeType.Dynamic else ThemeType.Default

    private val _currentTheme = MutableStateFlow(defaultTheme)

    override val availableThemes: Set<ThemeType> = buildSet {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            add(ThemeType.Dynamic)
        }
        add(ThemeType.Default)
        add(ThemeType.User)
    }

    override val currentTheme: SharedFlow<ThemeType> =
        _currentTheme.shareIn(externalScope, SharingStarted.WhileSubscribed())

    override val currentColorScheme: StateFlow<ColorScheme> =
        combine(isDarkThemeActive, currentTheme) { darkTheme, theme ->
            Log.v("ThemeManager(current)", "$darkTheme, $theme")
            when (theme) {
                ThemeType.Dynamic -> {
                    if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(
                        context
                    )
                }
                ThemeType.User -> UserColorScheme
                else -> LightColorScheme
            }
        }.stateIn(
            externalScope,
            SharingStarted.Eagerly,
            if (isDarkThemeActive.value) DarkColorScheme else LightColorScheme
        )

    override fun setTheme(themeType: ThemeType) {
        Log.v("ThemeManager", "From instance $this")
        _currentTheme.value = themeType
    }

    override fun setPrimaryColorForUserTheme(color: AppColor) {
        externalScope.launch {
            themeRepository.updatePrimaryUserColorScheme(color)
        }
    }

    override fun notifyNightModeChange(darkTheme: Boolean) {
        _isDarkThemeActive.value = darkTheme
    }
}