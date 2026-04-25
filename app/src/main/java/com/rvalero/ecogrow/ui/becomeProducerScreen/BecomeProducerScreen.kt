package com.rvalero.ecogrow.ui.becomeProducerScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.example.compose.EcoGrowTheme
import com.rvalero.ecogrow.R
import com.rvalero.ecogrow.ui.components.EcoGrowButton
import com.rvalero.ecogrow.ui.components.EcoGrowTextField
import com.rvalero.ecogrow.ui.components.EcoGrowTopBar
import com.rvalero.ecogrow.ui.util.UiEvent
import org.koin.androidx.compose.koinViewModel

@Composable
fun BecomeProducerViewModelScreen(
    viewModel: BecomeProducerViewModel = koinViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val lifecycleOwner = LocalLifecycleOwner.current
    val successMessage = stringResource(R.string.become_producer_success)

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is UiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
                    is UiEvent.NavigateBack -> {
                        snackbarHostState.showSnackbar(successMessage)
                        onNavigateBack()
                    }
                    else -> Unit
                }
            }
        }
    }

    BecomeProducerScreen(
        state = uiState,
        snackbarHostState = snackbarHostState,
        onIntent = viewModel::handleIntent
    )
}

@Composable
fun BecomeProducerScreen(
    state: BecomeProducerState,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onIntent: (BecomeProducerIntent) -> Unit
) {
    Scaffold(
        topBar = {
            EcoGrowTopBar(
                title = stringResource(R.string.become_producer_title),
                canNavigateBack = true,
                onNavigateBack = { onIntent(BecomeProducerIntent.NavigateBack) }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.surface
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(R.string.become_producer_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            EcoGrowTextField(
                value = state.nombreNegocio,
                onValueChange = { onIntent(BecomeProducerIntent.NombreNegocioChanged(it)) },
                label = stringResource(R.string.field_nombre_negocio),
                modifier = Modifier.fillMaxWidth()
            )

            EcoGrowTextField(
                value = state.descripcion,
                onValueChange = { onIntent(BecomeProducerIntent.DescripcionChanged(it)) },
                label = stringResource(R.string.field_descripcion),
                modifier = Modifier.fillMaxWidth()
            )

            EcoGrowTextField(
                value = state.localidad,
                onValueChange = { onIntent(BecomeProducerIntent.LocalidadChanged(it)) },
                label = stringResource(R.string.field_localidad),
                modifier = Modifier.fillMaxWidth()
            )

            EcoGrowTextField(
                value = state.provincia,
                onValueChange = { onIntent(BecomeProducerIntent.ProvinciaChanged(it)) },
                label = stringResource(R.string.field_provincia),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            EcoGrowButton(
                text = stringResource(R.string.btn_activate_producer),
                onClick = { onIntent(BecomeProducerIntent.Submit) },
                isLoading = state.isLoading,
                enabled = state.isFormValid && !state.isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            if (state.error != null) {
                Text(
                    text = state.error,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun BecomeProducerScreenPreview() {
    EcoGrowTheme {
        BecomeProducerScreen(
            state = BecomeProducerState(),
            onIntent = {}
        )
    }
}
