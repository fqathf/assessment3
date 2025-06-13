package com.faiqathifnurrahimhadiko607062330082.assessment3.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.faiqathifnurrahimhadiko607062330082.assessment3.R


sealed class Screen(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    object Home : Screen("home", R.string.home, Icons.Default.Home)
    object Main : Screen("main", R.string.main, Icons.Default.Person)
}