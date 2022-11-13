package com.interview.square.core.domain.model

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable

@Serializable
data class AppColor(
    val red: Float,
    val blue: Float,
    val green: Float,
    val alpha: Float
) {
    fun toColor(): Color = Color(red, green, blue, alpha)
}

fun Color.toAppColor() = AppColor(red, blue, green, alpha)