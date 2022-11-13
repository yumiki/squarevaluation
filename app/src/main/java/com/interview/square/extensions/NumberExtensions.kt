package com.interview.square.extensions

import android.content.res.Resources
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.util.*

val Int.toDp get() = (this / Resources.getSystem().displayMetrics.density).toInt().dp
val Dp.toPx get() = (this * Resources.getSystem().displayMetrics.density).value.toInt()

fun Number.roundTo(
    numFractionDigits: Int
) = "%.${numFractionDigits}f".format(this, Locale.getDefault())

operator fun Number.compareTo(initialState: Number): Int {
    return when(this) {
        is Int -> this.compareTo(initialState.toInt())
        else -> this.toFloat().compareTo(initialState.toFloat())
    }
}
