package com.interview.square.feature_square_move.ui

import androidx.lifecycle.ViewModel
import com.interview.square.core.domain.model.Bounds
import com.interview.square.core.domain.model.Position
import com.interview.square.core.domain.model.PositionHistory
import com.interview.square.core.domain.model.Square
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class SquareViewModel : ISquareMovementViewModel, ViewModel() {
    private val _square = MutableStateFlow(Square.TwoDimensionSquare(70, Position.TwoDimensionPosition(0, 0)))
    override val square: StateFlow<Square> = _square.asStateFlow()
    private val _bounds = MutableStateFlow(Bounds(0,0, 0, 0))
    override val bounds: StateFlow<Bounds> = _bounds.asStateFlow()
    private val _positionHistory = MutableStateFlow(emptyList<PositionHistory>())
    override val positionHistory: StateFlow<List<PositionHistory>> = _positionHistory.asStateFlow()

    override fun updateSquarePosition(newPosition: Position) {
        val currentPosition = square.value.currentPosition
        val updatedPosition = constraintToBounds(newPosition)

        _positionHistory.update {
            it.plus(PositionHistory(updatedPosition))
        }

        if (currentPosition == updatedPosition) {
            return
        }

        when(square.value) {
            is Square.TwoDimensionSquare -> _square.value = _square.value.copy(currentPosition = updatedPosition as Position.TwoDimensionPosition)
        }
    }

    private fun constraintToBounds(newPosition: Position): Position = when(newPosition) {
        is Position.TwoDimensionPosition -> Position.TwoDimensionPosition(
            maxOf(bounds.value.minX, minOf(newPosition.x, bounds.value.maxX)),
            maxOf(bounds.value.minY, minOf(newPosition.y, bounds.value.maxY))
        )
    }


    override fun setBounds(bounds: Bounds) {
        _bounds.value = bounds.copy(
            minX = maxOf(0, bounds.minX - square.value.size),
            maxX = bounds.maxX - square.value.size,
            minY = maxOf(0, bounds.minY - square.value.size),
            maxY = bounds.maxY - square.value.size
        )
    }
}

interface ISquareMovementViewModel {
    val square: StateFlow<Square>
    val bounds: StateFlow<Bounds>

    val positionHistory: StateFlow<List<PositionHistory>>

    fun updateSquarePosition(newPosition: Position)
    fun setBounds(bounds: Bounds)
}
