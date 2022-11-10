package com.interview.square.core.data.service

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalContext
import com.interview.square.core.domain.repository.StubThemeRepository
import com.interview.square.core.domain.service.IThemeManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

val LocalThemeManager = compositionLocalOf<IThemeManager> {
    ThemeManager(
        isSystemInDarkTheme()
        LocalContext.current,
        StubThemeRepository,
        CoroutineScope(SupervisorJob() + Dispatchers.Default)
    )
}