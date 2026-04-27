package com.rvalero.ecogrow.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rvalero.ecogrow.domain.model.UserRole
import com.rvalero.ecogrow.ui.components.EcoGrowBottomBar
import com.rvalero.ecogrow.ui.main.tabs.ExploreTabContent
import com.rvalero.ecogrow.ui.main.tabs.HomeTabContent
import com.rvalero.ecogrow.ui.main.tabs.OrdersTabContent
import com.rvalero.ecogrow.ui.profileScreen.ProfileViewModelScreen
import com.rvalero.ecogrow.ui.publishProductScreen.PublishProductViewModelScreen
import com.rvalero.ecogrow.ui.util.LocalSnackbarHostState
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(
    onNavigateToProductDetail: (Long) -> Unit = {},
    onNavigateToBecomeProducer: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    viewModel: MainViewModel = koinViewModel()
) {
    val userRole by viewModel.userRole.collectAsStateWithLifecycle()
    val tabs = remember(userRole) {
        if (userRole == UserRole.PRODUCER) {
            BottomTab.entries.toList()
        } else {
            BottomTab.entries.filter { it != BottomTab.SHARE }
        }
    }

    var selectedTab by rememberSaveable { mutableStateOf(BottomTab.HOME) }

    LaunchedEffect(userRole) {
        if (userRole == UserRole.CONSUMER && selectedTab == BottomTab.SHARE) {
            selectedTab = BottomTab.HOME
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        bottomBar = {
            EcoGrowBottomBar(
                selectedTab = selectedTab,
                tabs = tabs,
                onTabSelected = { selectedTab = it }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
            when (selectedTab) {
                BottomTab.HOME -> HomeTabContent(
                    onNavigateToProductDetail = onNavigateToProductDetail,
                    modifier = Modifier.padding(paddingValues)
                )
                BottomTab.EXPLORE -> ExploreTabContent(Modifier.padding(paddingValues))
                BottomTab.SHARE -> PublishProductViewModelScreen(modifier = Modifier.padding(paddingValues))
                BottomTab.ORDERS -> OrdersTabContent(Modifier.padding(paddingValues))
                BottomTab.PROFILE -> ProfileViewModelScreen(
                    onNavigateToBecomeProducer = onNavigateToBecomeProducer,
                    onNavigateToLogin = onNavigateToLogin,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}
