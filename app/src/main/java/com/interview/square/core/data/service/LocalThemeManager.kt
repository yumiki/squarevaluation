package com.interview.square.core.data.service

import androidx.compose.runtime.compositionLocalOf
import com.interview.square.core.domain.service.DefaultThemeManager
import com.interview.square.core.domain.service.IThemeManager

val LocalThemeManager = compositionLocalOf<IThemeManager> {
    DefaultThemeManager
}