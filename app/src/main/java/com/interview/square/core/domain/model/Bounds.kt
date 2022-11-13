package com.interview.square.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Bounds(
    val minX: Int,
    val maxX: Int,
    val minY: Int,
    val maxY: Int
) : Parcelable