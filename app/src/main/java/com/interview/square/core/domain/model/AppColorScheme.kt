package com.interview.square.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AppColorScheme(
    val primary: AppColor,
    val onPrimary: AppColor,
    val primaryContainer: AppColor,
    val onPrimaryContainer: AppColor,
    val inversePrimary: AppColor,
    val secondary: AppColor,
    val onSecondary: AppColor,
    val secondaryContainer: AppColor,
    val onSecondaryContainer: AppColor,
    val tertiary: AppColor,
    val onTertiary: AppColor,
    val tertiaryContainer: AppColor,
    val onTertiaryContainer: AppColor,
    val background: AppColor,
    val onBackground: AppColor,
    val surface: AppColor,
    val onSurface: AppColor,
    val surfaceVariant: AppColor,
    val onSurfaceVariant: AppColor,
    val surfaceTint: AppColor,
    val inverseSurface: AppColor,
    val inverseOnSurface: AppColor,
    val error: AppColor,
    val onError: AppColor,
    val errorContainer: AppColor,
    val onErrorContainer: AppColor,
    val outline: AppColor,
    val outlineVariant: AppColor,
    val scrim: AppColor,
)
