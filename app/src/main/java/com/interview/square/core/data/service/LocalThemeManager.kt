package com.interview.square.core.data.service

import androidx.compose.runtime.compositionLocalOf
import com.interview.square.core.domain.service.IThemeManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

val LocalThemeManager = compositionLocalOf<IThemeManager> { ThemeManager(CoroutineScope(SupervisorJob() + Dispatchers.Default)) }