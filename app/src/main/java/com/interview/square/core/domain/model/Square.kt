package com.interview.square.core.domain.model

sealed class Square(open val size: Int, open val currentPosition: Position) {
    data class TwoDimensionSquare(
        override val size: Int,
        override val currentPosition: Position.TwoDimensionPosition
    ) : Square(size, currentPosition)
}


