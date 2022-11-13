package com.interview.square.screens

import androidx.navigation.*

const val NAV_ARGS_HISTORY_ID = "historyId"

sealed class Screen(val route: String,val args: List<NamedNavArgument> = emptyList()) {
    object Home: Screen("home")
    object History: Screen("history", listOf(
        navArgument(NAV_ARGS_HISTORY_ID) {
            type = NavType.StringType
        }
    )) {
        fun navigate(navController: NavController, id: String) {
            navController.navigate("$route/$id")
        }
    }

    fun buildRouteWithArgs(): String = buildString {
        append(route)
        args.forEach {
            append("/{${it.name}}")
        }
    }
}
