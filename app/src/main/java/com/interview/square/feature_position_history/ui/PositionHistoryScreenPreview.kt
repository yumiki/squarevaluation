package com.interview.square.feature_position_history.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.interview.square.core.domain.model.PositionHistory
import com.interview.square.core.domain.model.TwoDimensionPosition
import com.interview.square.core.ui.theme.SquareTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*

@Preview
@Composable
fun PositionHistoryScreenPreview() {
    SquareTheme {
        PositionHistoryScreen(object : IPositionHistoryViewModel {
            override val history: StateFlow<List<PositionHistory>> = MutableStateFlow(
                List(100) {
                    PositionHistory(TwoDimensionPosition(it, it),Date().time + it)
                }
            )
        }) {

        }
    }
}