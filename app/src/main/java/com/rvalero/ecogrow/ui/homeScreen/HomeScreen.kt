package com.rvalero.ecogrow.ui.homeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.compose.EcoGrowTheme
import com.rvalero.ecogrow.R
import com.rvalero.ecogrow.ui.components.EcoGrowProducerCard
import com.rvalero.ecogrow.ui.components.EcoGrowProductCard
import com.rvalero.ecogrow.ui.components.EcoGrowSearchBar
import com.rvalero.ecogrow.ui.components.EcoGrowSectionHeader
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreenViewModel(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val lifecycleOwner = LocalLifecycleOwner.current

    HomeScreen(
        state = uiState,
        snackbarHostState = snackbarHostState,
        onIntent = viewModel::handleIntent,
        modifier = modifier
    )
}

@Composable
fun HomeScreen(
    state: HomeState,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onIntent: (HomeIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        GreetingHeader(userName = "Ramón")

        EcoGrowSearchBar(
            value = state.searchQuery,
            onValueChange = { onIntent(HomeIntent.SearchQueryChanged(it)) },
            hint = stringResource(R.string.home_search_hint),
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(bottom = 16.dp)
        )

        CategoryChips()

        SeasonBanner()

        EcoGrowSectionHeader(
            title = stringResource(R.string.home_nearby_producers),
            actionText = stringResource(R.string.home_see_all),
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(bottom = 12.dp)
        )
        ProducerCardsRow()

        EcoGrowSectionHeader(
            title = stringResource(R.string.home_featured_products),
            actionText = stringResource(R.string.home_see_all),
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(bottom = 12.dp)
        )
        ProductCardsGrid()

        Spacer(modifier = Modifier.height(16.dp))
    }
}


@Composable
private fun GreetingHeader(userName: String) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Text(
            text = stringResource(R.string.home_greeting, userName),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = stringResource(R.string.home_question),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}



@Composable
private fun CategoryChips() {
    val categories = listOf(
        R.string.category_all,
        R.string.category_vegetables,
        R.string.category_fruits,
        R.string.category_crafts,
        R.string.category_honey
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEachIndexed { index, resId ->
            val selected = index == 0
            FilterChip(
                selected = selected,
                onClick = { },
                label = {
                    Text(
                        text = stringResource(resId),
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    }
}

@Composable
private fun SeasonBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 20.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(20.dp)
    ) {
        Column {
            Text(
                text = stringResource(R.string.home_season_label),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primaryContainer,
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.home_season_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.onPrimary)
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = stringResource(R.string.home_season_button),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun ProducerCardsRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(bottom = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        EcoGrowProducerCard(name = "Finca La Encina", location = "2.3 km · Úbeda", rating = "4.8")
        EcoGrowProducerCard(name = "Miel del Sur", location = "5.1 km · Baeza", rating = "4.6")
        EcoGrowProducerCard(name = "Taller Rústico", location = "8.7 km · Jaén", rating = "4.9")
    }
}

@Composable
private fun ProductCardsGrid() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        EcoGrowProductCard(
            name = "Tomates cherry",
            producerName = "Finca La Encina",
            price = "2,50 €/kg",
            badge = "Bio",
            modifier = Modifier.weight(1f)
        )
        EcoGrowProductCard(
            name = "Miel de azahar",
            producerName = "Miel del Sur",
            price = "8,00 €/500g",
            badge = "Artesanal",
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    EcoGrowTheme {
        HomeScreen(
            state = HomeState(),
            onIntent = {}
        )
    }
}
