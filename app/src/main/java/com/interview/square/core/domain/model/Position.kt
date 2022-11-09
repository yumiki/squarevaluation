package com.interview.square.core.domain.model

import java.text.SimpleDateFormat
import java.util.*

sealed class Position {
    data class TwoDimensionPosition(val x: Int, val y: Int): Position()
}

data class PositionHistory(
    val position: Position,
    val date: Date = Date()
) {
    override fun toString(): String {
        val sdf = SimpleDateFormat("hh:mm:ss", Locale.getDefault())

        return when (position) {
            is Position.TwoDimensionPosition -> "x:${position.x}, y: ${position.y} date: ${sdf.format(date)}"
        }
    }
}
