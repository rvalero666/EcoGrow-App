package com.rvalero.ecogrow.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.compose.EcoGrowTheme
import com.rvalero.ecogrow.ui.main.BottomTab

@Composable
fun EcoGrowBottomBar(
    selectedTab: BottomTab,
    onTabSelected: (BottomTab) -> Unit
) {
    NavigationBar {
        BottomTab.entries.forEach { tab ->
            val selected = tab == selectedTab
            NavigationBarItem(
                selected = selected,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(
                        imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                        contentDescription = stringResource(tab.labelResId)
                    )
                },
                label = {
                    Text(text = stringResource(tab.labelResId))
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EcoGrowBottomBarPreview() {
    EcoGrowTheme {
        EcoGrowBottomBar(
            selectedTab = BottomTab.HOME,
            onTabSelected = {}
        )
    }
}
