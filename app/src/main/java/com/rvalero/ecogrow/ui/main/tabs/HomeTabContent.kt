package com.rvalero.ecogrow.ui.main.tabs

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rvalero.ecogrow.ui.homeScreen.HomeScreenViewModel

@Composable
fun HomeTabContent(modifier: Modifier = Modifier) {
    HomeScreenViewModel(modifier = modifier)
}
