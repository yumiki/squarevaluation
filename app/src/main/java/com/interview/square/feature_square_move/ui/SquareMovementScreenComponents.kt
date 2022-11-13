package com.interview.square.feature_square_move.ui

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.interview.square.core.domain.model.Position
import com.interview.square.core.domain.model.PositionHistory
import com.interview.square.core.domain.model.Square
import com.interview.square.core.domain.model.TwoDimensionPosition
import com.interview.square.core.ui.AnimatedNumberFields
import com.interview.square.extensions.toDp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun SquareComponent(
    modifier: Modifier = Modifier,
    square: Square,
    onDrag: (position: Position) -> Unit
) {
    val offsetX by remember(square) {
        derivedStateOf {
            (square.currentPosition as TwoDimensionPosition).x
        }
    }
    val offsetY by remember(square) {
        derivedStateOf {
            (square.currentPosition as TwoDimensionPosition).y
        }
    }

    Box(modifier = modifier
        .offset {
            IntOffset(offsetX, offsetY)
        }
        .size(square.size.toDp)
        .clip(RoundedCornerShape(5.dp))
        .background(MaterialTheme.colorScheme.primaryContainer)
        .pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
                val x = dragAmount.x.roundToInt()
                val y = dragAmount.y.roundToInt()
                change.consume()
                onDrag(
                    TwoDimensionPosition(
                        x = x,
                        y = y
                    )
                )
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PositionHistoryCard(
    history: List<PositionHistory>,
    compact: Boolean = true,
    onClick: () -> Unit
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val lastMovement by remember(history) {
        derivedStateOf {
            history.last()
        }
    }

    val lastPosition by remember(lastMovement) {
        derivedStateOf {
            history.last().position
        }
    }

    val lastMovementDate by remember(lastMovement) {
        derivedStateOf {
            history.last().getFormattedDate()
        }
    }

    LaunchedEffect(key1 = history) {
        coroutineScope.launch {
            if (history.isNotEmpty()) {
                listState.animateScrollToItem(history.lastIndex)
            }
        }
    }

    val cardModifier =
        Modifier
            .run {
                when (LocalConfiguration.current.orientation) {
                    Configuration.ORIENTATION_LANDSCAPE -> {
                        padding(top = 16.dp)
                    }
                    else -> {
                        heightIn(max = 300.dp)
                        displayCutoutPadding()
                    }
                }
            }
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(CardDefaults.shape)
            .clickable {
                onClick()
            }

    Card(
        cardModifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                0.5f
            )
        )
    ) {
        if (compact) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row {
                    val textAlignmentModifier = Modifier.alignByBaseline()
                    Text(modifier = textAlignmentModifier, text = "Last movement at: ", style = MaterialTheme.typography.titleLarge)
                    Text(modifier = textAlignmentModifier, text = lastMovementDate, style = MaterialTheme.typography.titleMedium)
                }
                Divider()
                Text(
                    text = "Current position :",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    when(lastPosition) {
                        is TwoDimensionPosition -> {
                            val textAlignmentModifier = Modifier.alignByBaseline()
                            Text(modifier = textAlignmentModifier, text = "x:", style = MaterialTheme.typography.titleMedium)
                            AnimatedNumberFields(
                                modifier = textAlignmentModifier,
                                value = (lastPosition as TwoDimensionPosition).x,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(modifier = textAlignmentModifier,text = "y:", style = MaterialTheme.typography.titleMedium)
                            AnimatedNumberFields(
                                modifier = textAlignmentModifier,
                                value = (lastPosition as TwoDimensionPosition).y,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }

                }
            }
        } else {
            LazyColumn(contentPadding = PaddingValues(16.dp), state = listState) {
                items(history.size) { index ->
                    val positionRecord = history[index]
                    Text(
                        modifier = Modifier.animateItemPlacement(),
                        text = positionRecord.toString()
                    )
                }
            }
        }
        Divider()
        TextButton(
            modifier = Modifier.align(Alignment.End),
            onClick = { onClick() }
        ) {
            Text(text = "Expand")
        }
    }
}