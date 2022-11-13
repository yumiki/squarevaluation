package com.interview.square.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
sealed class Square(open val size: Int, open val currentPosition: Position) {
    @Parcelize
    data class TwoDimensionSquare(
        override val size: Int,
        override val currentPosition: TwoDimensionPosition
    ) : Square(size, currentPosition), Parcelable
}


