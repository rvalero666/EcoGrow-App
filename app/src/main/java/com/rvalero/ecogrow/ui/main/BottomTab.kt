package com.rvalero.ecogrow.ui.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.ui.graphics.vector.ImageVector
import com.rvalero.ecogrow.R

enum class BottomTab(
    val labelResId: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
) {
    HOME(R.string.tab_home, Icons.Filled.Home, Icons.Outlined.Home),
    EXPLORE(R.string.tab_explore, Icons.Filled.Search, Icons.Outlined.Search),
    SHARE(R.string.tab_share, Icons.Filled.Add, Icons.Outlined.Add),

    ORDERS(R.string.tab_orders, Icons.Filled.ShoppingBag, Icons.Outlined.ShoppingBag),
    PROFILE(R.string.tab_profile, Icons.Filled.Person, Icons.Outlined.Person)
}
