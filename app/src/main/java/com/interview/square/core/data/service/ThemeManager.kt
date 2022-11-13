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

    override val defaultColorScheme: ColorScheme = when (defaultTheme) {
        ThemeType.Dynamic -> {
            if (_isDarkThemeActive.value) dynamicDarkColorScheme(context) else dynamicLightColorScheme(
                context
            )
        }
        else -> if (_isDarkThemeActive.value) DarkColorScheme else LightColorScheme
    }

    private val _currentTheme = MutableSharedFlow<ThemeType>()

    override val availableThemes: Set<ThemeType> = buildSet {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            add(ThemeType.Dynamic)
        }
        add(ThemeType.Default)
        add(ThemeType.User)
    }

    override val currentTheme: SharedFlow<ThemeType> = _currentTheme.asSharedFlow()

    override val currentColorScheme: SharedFlow<ColorScheme> = currentTheme.combine(isDarkThemeActive) { theme, darkTheme ->
            Log.v("ThemeManager(current)", "$darkTheme, $theme")
            when (theme) {
                ThemeType.Dynamic -> {
                    if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(
                        context
                    )
                }
                ThemeType.User -> themeRepository.getUserColorScheme().toColorScheme()
                else -> if (darkTheme) DarkColorScheme else LightColorScheme
            }
        }.shareIn(
            externalScope,
            SharingStarted.WhileSubscribed()
        )

    override suspend fun setTheme(themeType: ThemeType) {
        Log.v("ThemeManager", "From instance $this")
        _currentTheme.emit(themeType)
    }

    override fun setPrimaryColorForUserTheme(color: AppColor, variantColor: AppColor?) {
        externalScope.launch {
            themeRepository.updatePrimaryUserColorScheme(color, variantColor)
            setTheme(ThemeType.User)
        }
    }

    override fun notifyNightModeChange(darkTheme: Boolean) {
        _isDarkThemeActive.value = darkTheme
    }
}