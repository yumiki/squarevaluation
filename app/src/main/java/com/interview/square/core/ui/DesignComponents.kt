package com.interview.square.core.ui

import androidx.compose.animation.*
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
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