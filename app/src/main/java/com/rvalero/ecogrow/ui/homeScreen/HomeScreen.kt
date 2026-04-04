package com.rvalero.ecogrow.ui.homeScreen

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.compose.EcoGrowTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreenViewModel(
    viewModel: HomeViewModel = koinViewModel()
){
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val lifecycleOwner = LocalLifecycleOwner.current

    HomeScreen(
        state = uiState,
        snackbarHostState = snackbarHostState,
        onIntent = { }
    )
}

@Composable
fun HomeScreen(
    state: HomeState,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onIntent: (HomeIntent) -> Unit
){

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