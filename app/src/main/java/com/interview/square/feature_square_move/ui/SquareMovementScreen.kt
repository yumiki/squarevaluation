package com.interview.square.feature_square_move.ui

import android.app.Activity
import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.interview.square.R
import com.interview.square.extensions.toDp
import com.interview.square.extensions.toPx
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import androidx.lifecycle.viewmodel.compose.viewModel
import com.interview.square.core.domain.model.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SquareMovementScreen(viewModel: ISquareMovementViewModel = viewModel<SquareViewModel>()) {
    val activity = (LocalContext.current as? Activity)

    val square by viewModel.square.collectAsState()
    val history by viewModel.positionHistory.collectAsState()

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

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = stringResource(id = R.string.edit_app_settings_content_description)
                )
            }
        }
    ) { padding ->
        PositionHistoryCard(history = history)

        BoxWithConstraints(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            LaunchedEffect(key1 = true) {
                // Initialization block
                viewModel.setBounds(Bounds(0, maxWidth.toPx, 0, maxHeight.toPx))
                viewModel.updateSquarePosition(
                    TwoDimensionPosition(
                        maxWidth.div(2).toPx,
                        maxHeight.div(2).toPx
                    ),
                    true
                )
            }

            SquareComponent(square = square) {
                when (it) {
                    is TwoDimensionPosition -> viewModel.updateSquarePosition(
                        TwoDimensionPosition(
                            (square.currentPosition as TwoDimensionPosition).x + it.x,
                            (square.currentPosition as TwoDimensionPosition).y + it.y
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
        .background(MaterialTheme.colorScheme.primary)
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
    history: List<PositionHistory>
) {
    val listState = rememberLazyListState()
    // Remember a CoroutineScope to be able to launch
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = history) {
        coroutineScope.launch {
            if (history.isNotEmpty()) {
                listState.animateScrollToItem(history.lastIndex)
            }
        }
    }

    when (LocalConfiguration.current.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            Card(
                Modifier
                    .padding(top = 16.dp)
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .heightIn(max = 150.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(0.5f))
            ) {
                LazyColumn(contentPadding = PaddingValues(16.dp), state = listState) {
                    items(history.size) { index ->
                        val positionRecord = history[index]
                        Text(modifier = Modifier.animateItemPlacement(), text = positionRecord.toString())
                    }
                }
            }        }
        else -> {
            Card(
                Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .heightIn(max = 300.dp)
                    .displayCutoutPadding(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(0.5f))
            ) {
                LazyColumn(contentPadding = PaddingValues(16.dp), state = listState) {
                    items(history.size) { index ->
                        val positionRecord = history[index]
                        Text(modifier = Modifier.animateItemPlacement(), text = positionRecord.toString())
                    }
                }
            }
        }
    }

}