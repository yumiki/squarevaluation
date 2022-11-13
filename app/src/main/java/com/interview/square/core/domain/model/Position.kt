package com.interview.square.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.text.DateFormat
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

    @IgnoredOnParcel
    private val sdf = SimpleDateFormat("hh:mm:ss.SSS", Locale.getDefault())

    fun getFormattedDate(format: DateFormat = sdf): String = format.format(Date(timestamp))

    override fun toString(): String {
        return when (position) {
            is TwoDimensionPosition -> "x:${position.x}, y: ${position.y} date: ${getFormattedDate()}"
            else -> "Unknown position"
        }
    }
}
