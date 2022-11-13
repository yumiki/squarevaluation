package com.interview.square.feature_square_move.ui

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.interview.square.R
import com.interview.square.core.domain.model.*
import com.interview.square.core.ui.FullScreenComponent
import com.interview.square.extensions.toPx
import org.koin.androidx.compose.getViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SquareMovementScreen(
    viewModel: ISquareMovementViewModel = getViewModel<SquareViewModel>(),
    navigateToHistoryScreen: (String) -> Unit
) {
    val square by viewModel.square.collectAsState()
    val history by viewModel.positionHistory.collectAsState()

    var settingDialogIsVisible by rememberSaveable { mutableStateOf(false) }

    FullScreenComponent {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            floatingActionButton = {
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    SmallFloatingActionButton(
                        onClick = {
                            viewModel.putInTheMiddle()
                        },
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(0.8f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Put in the middle"
                        )
                    }
                    FloatingActionButton(onClick = {
                        settingDialogIsVisible = true
                    }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(id = R.string.edit_app_settings_content_description)
                        )
                    }
                }
            }
        ) { padding ->
            if (history.isNotEmpty()) {
                PositionHistoryCard(history = history) {
                    navigateToHistoryScreen(viewModel.recordId)
                }
            }

            BoxWithConstraints(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                // Initialization block
                LaunchedEffect(key1 = true) {
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

    SettingsDialog(
        visibility = settingDialogIsVisible,
        square = square,
        onDismiss = { settingDialogIsVisible = false },
        onSliderValueChange = {
            viewModel.updateSquareSize(it.roundToInt())
        },
        onResetSquarePositionRequest = {
            viewModel.putInTheMiddle()
        }
    )
}