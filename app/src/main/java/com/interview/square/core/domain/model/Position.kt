package com.interview.square.core.domain.model

sealed class Position {
    data class TwoDimensionPosition(val x: Int, val y: Int): Position()
}