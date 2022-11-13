package com.interview.square.feature_square_move.ui

import android.app.Activity
import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.godaddy.android.colorpicker.harmony.ColorHarmonyMode
import com.godaddy.android.colorpicker.harmony.HarmonyColorPicker
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.interview.square.DEFAULT_SQUARE_SIZE_IN_PIXEL
import com.interview.square.R
import com.interview.square.core.data.service.LocalThemeManager
import com.interview.square.core.domain.model.*
import com.interview.square.core.domain.service.ThemeType
import com.interview.square.extensions.toDp
import com.interview.square.extensions.toPx
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SquareMovementScreen(
    viewModel: ISquareMovementViewModel = viewModel<SquareViewModel>(),
) {
    val activity = (LocalContext.current as? Activity)
    val themeManager = LocalThemeManager.current
    val coroutineScope = rememberCoroutineScope()

    val square by viewModel.square.collectAsState()
    val history by viewModel.positionHistory.collectAsState()
    val currentTheme by themeManager.currentTheme.collectAsState(initial = LocalThemeManager.current.defaultTheme)

    var settingDialogIsVisible by rememberSaveable { mutableStateOf(false) }

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

    AnimatedVisibility(settingDialogIsVisible) {
        AlertDialog(
            properties = DialogProperties(usePlatformDefaultWidth = false),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .wrapContentHeight(), onDismissRequest = {
            settingDialogIsVisible = false
        }, text = {
            val squareSize by remember(square) {
                derivedStateOf {
                    square.size
                }
            }
            Column(modifier = Modifier.verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Square properties", style = MaterialTheme.typography.titleLarge)
                Divider()
                Text(text = "Change size")
                Slider(value = squareSize.toFloat(), onValueChange = {
                    viewModel.updateSquareSize(it.roundToInt())
                }, steps = 25, valueRange = DEFAULT_SQUARE_SIZE_IN_PIXEL.toFloat()..200f)
                Divider()
                Box(Modifier.fillMaxWidth()) {
                    Button(modifier = Modifier.align(Alignment.Center), onClick = {
                        viewModel.putInTheMiddle()
                    }) {
                        Text(text = "Put in the middle")
                    }
                }
                Spacer(modifier = Modifier.size(16.dp))
                Text("Change theme", style = MaterialTheme.typography.titleLarge)
                Divider()
                LazyRow {
                    items(items = themeManager.availableThemes.toList(), key = {
                        it
                    }) { themeType ->
                        val isSelectedTheme = currentTheme == themeType
                        val index = themeManager.availableThemes.indexOf(themeType)
                        val shape = when (index) {
                            0 -> RoundedCornerShape(
                                topStartPercent = 50,
                                bottomStartPercent = 50
                            )
                            themeManager.availableThemes.size - 1 -> RoundedCornerShape(
                                topEndPercent = 50,
                                bottomEndPercent = 50
                            )
                            else -> RoundedCornerShape(0)
                        }
                        OutlinedButton(
                            modifier = Modifier.offset((-1 * index).dp, 0.dp),
                            onClick = {
                                coroutineScope.launch {
                                    themeManager.setTheme(themeType)
                                }
                            },
                            shape = shape,
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (isSelectedTheme) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent
                            )
                        ) {
                            Text(
                                text = themeType.name,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
                AnimatedVisibility(visible = currentTheme == ThemeType.User) {
                    Column {
                        ColorPicker { color, variants ->
                            themeManager.setPrimaryColorForUserTheme(
                                color.toAppColor(),
                                variants.first().toAppColor()
                            )
                        }
                        Row {
                            Box(
                                Modifier
                                    .clip(CircleShape)
                                    .size(28.dp)
                                    .background(MaterialTheme.colorScheme.primary))
                            Box(
                                Modifier
                                    .clip(CircleShape)
                                    .size(28.dp)
                                    .background(MaterialTheme.colorScheme.primaryContainer))
                        }
                    }
                }
            }
        }, title = {
            Text(text = "Settings")
        }, confirmButton = {
            TextButton(onClick = {
                settingDialogIsVisible = false
            }) {
                Text(text = "Close")
            }
        })
    }
}

@Composable
fun ColorPicker(value: Color = MaterialTheme.colorScheme.primary, onColorSelected: (Color, List<Color>) -> Unit) {
    HarmonyColorPicker(
        modifier = Modifier.size(250.dp),
        harmonyMode = ColorHarmonyMode.MONOCHROMATIC,
        color = value
    ) {
        onColorSelected(it.toColor(), it.getMonochromaticColors().map { color -> color.toColor() })
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
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                        0.5f
                    )
                )
            ) {
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
        }
        else -> {
            Card(
                Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .heightIn(max = 300.dp)
                    .displayCutoutPadding(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                        0.5f
                    )
                )
            ) {
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
        }
    }

}