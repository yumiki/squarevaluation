package com.interview.square.extensions

import android.content.res.Resources
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val Int.toDp get() = (this / Resources.getSystem().displayMetrics.density).toInt().dp
val Dp.toPx get() = (this * Resources.getSystem().displayMetrics.density).value.toInt()