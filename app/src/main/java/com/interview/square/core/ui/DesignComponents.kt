package com.interview.square.core.ui

import android.app.Activity
import androidx.compose.animation.*
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.godaddy.android.colorpicker.harmony.ColorHarmonyMode
import com.godaddy.android.colorpicker.harmony.HarmonyColorPicker
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.interview.square.extensions.compareTo
import com.interview.square.extensions.roundTo

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedNumberFields(
    modifier: Modifier = Modifier,
    value: Number,
    style: TextStyle = LocalTextStyle.current
) {
    AnimatedContent(
        targetState = value,
        transitionSpec = {
            if (targetState > initialState) {
                slideInVertically { height -> height } + scaleIn() with
                        slideOutVertically { height -> -height } + scaleOut()
            } else {
                slideInVertically { height -> -height } + scaleIn() with
                        slideOutVertically { height -> height } + scaleOut()
            }.using(
                SizeTransform(clip = false)
            )
        }
    ) {
        val number = if (it is Float) it.roundTo(2) else it
        Text(
            modifier = modifier,
            text = number.toString(),
            style = style
        )
    }
}

@Composable
fun FullScreenComponent(content: @Composable () -> Unit) {
    val activity = (LocalContext.current as? Activity)
    val systemUiController = rememberSystemUiController()

    DisposableEffect(true) {
        activity?.window?.run {
            WindowCompat.setDecorFitsSystemWindows(this, false)
        }
        systemUiController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        systemUiController.isSystemBarsVisible = false
        onDispose {
            activity?.window?.run {
                WindowCompat.setDecorFitsSystemWindows(this, true)
            }
            systemUiController.isSystemBarsVisible = true
        }
    }
    content()
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
