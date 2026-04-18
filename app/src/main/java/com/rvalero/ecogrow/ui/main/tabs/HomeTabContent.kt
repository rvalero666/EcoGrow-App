package com.rvalero.ecogrow.ui.main.tabs

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rvalero.ecogrow.ui.homeScreen.HomeScreenViewModel

@Composable
fun HomeTabContent(
    onNavigateToProductDetail: (Long) -> Unit = {},
    modifier: Modifier = Modifier
) {
    HomeScreenViewModel(
        onNavigateToProductDetail = onNavigateToProductDetail,
        modifier = modifier
    )
}
