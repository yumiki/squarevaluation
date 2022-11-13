package com.interview.square.feature_square_move.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.interview.square.DEFAULT_SQUARE_SIZE_IN_PIXEL
import com.interview.square.core.domain.model.*
import com.interview.square.core.ui.theme.SquareTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@Preview
@Composable
fun SquareMovementScreenPreview() {
    SquareTheme {
        SquareMovementScreen(object : ISquareMovementViewModel{
            private val _square =  MutableStateFlow(
                Square.TwoDimensionSquare(DEFAULT_SQUARE_SIZE_IN_PIXEL)
            )
            override val recordId: String = "1"
            override val square: StateFlow<Square> = _square.asStateFlow()
            private val _bounds = MutableStateFlow(Bounds(0,0,0,0))
            override val bounds: StateFlow<Bounds> = _bounds.asStateFlow()
            override val positionHistory: StateFlow<List<PositionHistory>> = MutableStateFlow(
                emptyList()
            )

            override fun updateSquarePosition(newPosition: Position, initialPosition: Boolean) {
                when(val square = square.value) {
                    is Square.TwoDimensionSquare -> {
                        _square.value = square.copy(currentPosition = newPosition as TwoDimensionPosition)
                    }
                }
            }

            override fun setBounds(bounds: Bounds) {
                _bounds.value = bounds
            }

            override fun putInTheMiddle() {
                TODO("Not yet implemented")
            }

            override fun updateSquareSize(newSize: Int) {
                TODO("Not yet implemented")
            }

        }) {

        }
    }
}