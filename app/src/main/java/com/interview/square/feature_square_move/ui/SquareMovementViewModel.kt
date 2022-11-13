package com.interview.square.feature_square_move.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.interview.square.core.domain.model.*
import com.interview.square.feature_position_history.domain.IPositionHistoryRepository
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

class SquareViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val repository: IPositionHistoryRepository
) : ISquareMovementViewModel,
    ViewModel() {
    @OptIn(SavedStateHandleSaveableApi::class)
    override val recordId = savedStateHandle.saveable("recordId") {
        UUID.randomUUID().toString()
    }

    override val square: StateFlow<Square> = savedStateHandle.getStateFlow(
        "square",
        Square.TwoDimensionSquare(70, TwoDimensionPosition(0, 0))
    )
    override val bounds: StateFlow<Bounds> = savedStateHandle.getStateFlow(
        "bounds",
        Bounds(0, 0, 0, 0)
    )
    override val positionHistory: StateFlow<List<PositionHistory>> = savedStateHandle.getStateFlow(
        "history",
        emptyList()
    )

    override fun updateSquarePosition(newPosition: Position, initialPosition: Boolean) {
        val currentPosition = square.value.currentPosition
        val updatedPosition = constraintToBounds(newPosition)

        if (currentPosition == updatedPosition) {
            return
        }

        if (initialPosition && positionHistory.value.isNotEmpty()) {
            val rotationPosition = constraintToBounds(when(currentPosition) {
                is TwoDimensionPosition -> TwoDimensionPosition(currentPosition.y, currentPosition.x)
                else -> currentPosition
            })
            recordPosition(positionHistory.value.plus(PositionHistory(rotationPosition)))
            return
        }

        when (val square = square.value) {
            is Square.TwoDimensionSquare -> savedStateHandle["square"] =
                square.copy(currentPosition = updatedPosition as TwoDimensionPosition)
        }
        recordPosition(positionHistory.value.plus(PositionHistory(updatedPosition)))

    }

    private fun recordPosition(positions: List<PositionHistory>) {
        repository.persistRecord(PositionRecord(recordId, positions))
        savedStateHandle["history"] = positions
    }

    private fun constraintToBounds(newPosition: Position): Position = when (newPosition) {
        is TwoDimensionPosition -> TwoDimensionPosition(
            maxOf(bounds.value.minX, minOf(newPosition.x, bounds.value.maxX)),
            maxOf(bounds.value.minY, minOf(newPosition.y, bounds.value.maxY))
        )
        else -> newPosition
    }


    override fun setBounds(bounds: Bounds) {
        savedStateHandle["bounds"] = bounds.copy(
            minX = maxOf(0, bounds.minX - square.value.size),
            maxX = bounds.maxX - square.value.size,
            minY = maxOf(0, bounds.minY - square.value.size),
            maxY = bounds.maxY - square.value.size
        )
    }

    override fun putInTheMiddle() {
        when(square.value) {
            is Square.TwoDimensionSquare -> updateSquarePosition(TwoDimensionPosition(bounds.value.maxX/2, bounds.value.maxY/2))
        }
    }

    override fun updateSquareSize(newSize: Int) {
        when (val square = square.value) {
            is Square.TwoDimensionSquare -> savedStateHandle["square"] =
                square.copy(size = newSize)
        }
    }
}

interface ISquareMovementViewModel {
    val recordId: String
    val square: StateFlow<Square>
    val bounds: StateFlow<Bounds>

    val positionHistory: StateFlow<List<PositionHistory>>

    fun updateSquarePosition(newPosition: Position, initialPosition: Boolean = false)
    fun setBounds(bounds: Bounds)
    fun putInTheMiddle()
    fun updateSquareSize(newSize: Int)
}
