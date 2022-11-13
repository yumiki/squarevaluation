package com.interview.square.feature_square_move.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.interview.square.DEFAULT_SQUARE_SIZE_IN_PIXEL
import com.interview.square.core.data.service.LocalThemeManager
import com.interview.square.core.domain.model.Square
import com.interview.square.core.domain.model.toAppColor
import com.interview.square.core.domain.service.ThemeType
import com.interview.square.core.ui.ColorPicker
import kotlinx.coroutines.launch


/**
 * Setting square properties and app theme
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SettingsDialog(
    visibility: Boolean,
    square: Square,
    onDismiss: () -> Unit,
    onSliderValueChange: (Float) -> Unit,
    onResetSquarePositionRequest: () -> Unit
) {
    val themeManager = LocalThemeManager.current
    val coroutineScope = rememberCoroutineScope()

    val currentTheme by themeManager.currentTheme.collectAsState(initial = LocalThemeManager.current.defaultTheme)

    AnimatedVisibility(visibility) {
        AlertDialog(
            properties = DialogProperties(usePlatformDefaultWidth = false),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .wrapContentHeight(), onDismissRequest = onDismiss, text = {
                val squareSize by remember(square) {
                    derivedStateOf {
                        square.size
                    }
                }
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Square properties", style = MaterialTheme.typography.titleLarge)
                    Divider()
                    Text(text = "Change size")
                    Slider(
                        value = squareSize.toFloat(),
                        onValueChange = onSliderValueChange,
                        steps = 25,
                        valueRange = DEFAULT_SQUARE_SIZE_IN_PIXEL.toFloat()..200f
                    )
                    Divider()
                    Box(Modifier.fillMaxWidth()) {
                        Button(
                            modifier = Modifier.align(Alignment.Center),
                            onClick = onResetSquarePositionRequest
                        ) {
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
                                if (isSelectedTheme) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected theme"
                                    )
                                }
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
                                        .background(MaterialTheme.colorScheme.primary)
                                )
                                Box(
                                    Modifier
                                        .clip(CircleShape)
                                        .size(28.dp)
                                        .background(MaterialTheme.colorScheme.primaryContainer)
                                )
                            }
                        }
                    }
                }
            }, title = {
                Text(text = "Settings")
            }, confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text(text = "Close")
                }
            })
    }
}