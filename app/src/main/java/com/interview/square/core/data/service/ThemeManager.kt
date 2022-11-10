package com.interview.square.core.data.service

import android.os.Build
import android.util.Log
import androidx.compose.ui.graphics.Color
import com.interview.square.core.domain.service.IThemeManager
import com.interview.square.core.domain.service.ThemeType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn

class ThemeManager(
    externalScope: CoroutineScope
) : IThemeManager {
    init {
        Log.v("ThemeManager","new instance $this")
    }

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

    override fun setTheme(themeType: ThemeType) {
        Log.v("ThemeManager","From instance $this")
        _currentTheme.value = themeType
    }

    override fun setPrimaryColorForUserTheme(color: Color) {
        TODO("Not yet implemented")
    }
}