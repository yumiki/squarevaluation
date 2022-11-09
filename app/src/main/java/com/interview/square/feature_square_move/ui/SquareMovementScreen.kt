package com.interview.square.feature_square_move.ui

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.interview.square.core.domain.model.Bounds
import com.interview.square.core.domain.model.Position
import com.interview.square.core.domain.model.Square
import com.interview.square.extensions.toDp
import com.interview.square.extensions.toPx
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SquareMovementScreen(viewModel: ISquareMovementViewModel) {
    val activity = (LocalContext.current as? Activity)

    val square by viewModel.square.collectAsState()

    val systemUiController = rememberSystemUiController()

    DisposableEffect(true) {
        activity?.window?.run {
            WindowCompat.setDecorFitsSystemWindows(this, false)
        }
        systemUiController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        systemUiController.isSystemBarsVisible = false
        onDispose {}
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
        BoxWithConstraints(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            LaunchedEffect(key1 = true) {
                // Initialization block
                viewModel.setBounds(Bounds(0, maxWidth.toPx, 0, maxHeight.toPx))
                viewModel.updateSquarePosition(
                    Position.TwoDimensionPosition(
                        maxWidth.div(2).toPx,
                        maxHeight.div(2).toPx
                    )
                )
            }

            SquareComponent(square = square) {
                when (it) {
                    is Position.TwoDimensionPosition -> viewModel.updateSquarePosition(
                        Position.TwoDimensionPosition(
                            minOf(
                                (square.currentPosition as Position.TwoDimensionPosition).x + it.x,
                                maxWidth.toPx
                            ),
                            minOf(
                                (square.currentPosition as Position.TwoDimensionPosition).y + it.y,
                                maxHeight.toPx
                            )
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun SquareComponent(
    modifier: Modifier = Modifier,
    square: Square,
    onDrag: (position: Position) -> Unit
) {
    val offsetX by remember(square) {
        derivedStateOf {
            (square.currentPosition as Position.TwoDimensionPosition).x
        }
    }
    val offsetY by remember(square) {
        derivedStateOf {
            (square.currentPosition as Position.TwoDimensionPosition).y
        }
    }

    Box(modifier = modifier
        .offset {
            IntOffset(offsetX, offsetY)
        }
        .size(square.size.toDp)
        .background(MaterialTheme.colorScheme.primary)
        .pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
                val x = dragAmount.x.roundToInt()
                val y = dragAmount.y.roundToInt()
                change.consume()
                onDrag(
                    Position.TwoDimensionPosition(
                        x = x,
                        y = y
                    )
                )
            }
        }
    )
}