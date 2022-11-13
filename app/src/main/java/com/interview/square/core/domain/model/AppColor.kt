package com.interview.square.core.domain.model

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable

/**
 * Class to avoid using directly android libraries, helpful in case where we need to make a multiplaform library
 * //TODO remove the mappers !
 */
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