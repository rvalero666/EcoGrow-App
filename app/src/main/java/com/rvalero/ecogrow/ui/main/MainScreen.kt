package com.rvalero.ecogrow.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.rvalero.ecogrow.ui.components.EcoGrowBottomBar
import com.rvalero.ecogrow.ui.components.EcoGrowTopBar
import com.rvalero.ecogrow.ui.main.tabs.ExploreTabContent
import com.rvalero.ecogrow.ui.main.tabs.HomeTabContent
import com.rvalero.ecogrow.ui.main.tabs.OrdersTabContent
import com.rvalero.ecogrow.ui.main.tabs.ProfileTabContent
import com.rvalero.ecogrow.ui.main.tabs.ShareTabContent

@Composable
fun MainScreen() {
    var selectedTab by rememberSaveable { mutableStateOf(BottomTab.HOME) }
    val snackbarHostState = remember { SnackbarHostState() }
    val title = stringResource(selectedTab.labelResId)

    Scaffold(
        topBar = {
            EcoGrowTopBar(
                title = title,
                canNavigateBack = false
            )
        },
        bottomBar = {
            EcoGrowBottomBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        when (selectedTab) {
            BottomTab.HOME -> HomeTabContent(Modifier.padding(paddingValues))
            BottomTab.EXPLORE -> ExploreTabContent(Modifier.padding(paddingValues))
            BottomTab.SHARE -> ShareTabContent(Modifier.padding(paddingValues))
            BottomTab.ORDERS -> OrdersTabContent(Modifier.padding(paddingValues))
            BottomTab.PROFILE -> ProfileTabContent(Modifier.padding(paddingValues))
        }
    }
}
