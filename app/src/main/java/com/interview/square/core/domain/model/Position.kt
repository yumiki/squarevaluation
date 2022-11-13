package com.interview.square.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class TwoDimensionPosition(val x: Int, val y: Int): Position

interface Position: Parcelable

@Parcelize
data class PositionHistory(
    val position: Position,
    val timestamp: Long = Date().time
) : Parcelable {
    override fun toString(): String {
        val sdf = SimpleDateFormat("hh:mm:ss", Locale.getDefault())

        return when (position) {
            is TwoDimensionPosition -> "x:${position.x}, y: ${position.y} date: ${sdf.format(Date(timestamp))}"
            else -> "Unknown position"
        }
    }
}
