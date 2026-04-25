package com.rvalero.ecogrow.ui.becomeProducerScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.example.compose.EcoGrowTheme
import com.rvalero.ecogrow.R
import com.rvalero.ecogrow.ui.components.EcoGrowTextField
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
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.surface
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            BecomeProducerTopBar(
                isLoading = state.isLoading,
                isFormValid = state.isFormValid,
                onBack = { onIntent(BecomeProducerIntent.NavigateBack) },
                onSubmit = { onIntent(BecomeProducerIntent.Submit) }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
                    .padding(top = 12.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Hero()

                Spacer(modifier = Modifier.height(8.dp))

                SectionLabel(text = stringResource(R.string.become_producer_section_business))

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
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = false
                )

                Spacer(modifier = Modifier.height(16.dp))

                SectionLabel(text = stringResource(R.string.become_producer_section_location))

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
            }
        }
    }
}

@Composable
private fun BecomeProducerTopBar(
    isLoading: Boolean,
    isFormValid: Boolean,
    onBack: () -> Unit,
    onSubmit: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.action_back),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        Text(
            text = stringResource(R.string.become_producer_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp)
        )

        val canSubmit = isFormValid && !isLoading
        val submitColor = if (canSubmit) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.outline

        Box(
            modifier = Modifier
                .clickable(enabled = canSubmit, onClick = onSubmit)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = stringResource(R.string.btn_publish),
                    color = submitColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(MaterialTheme.colorScheme.outlineVariant)
    )
}

@Composable
private fun Hero() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Eco,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.become_producer_hero_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = stringResource(R.string.become_producer_subtitle),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text.uppercase(),
        fontSize = 11.sp,
        color = MaterialTheme.colorScheme.outline,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.8.sp,
        modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp)
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun BecomeProducerScreenPreview() {
    EcoGrowTheme {
        BecomeProducerScreen(
            state = BecomeProducerState(
                nombreNegocio = "Finca La Encina",
                descripcion = "Hortalizas ecológicas",
                localidad = "Valencia",
                provincia = "Valencia"
            ),
            onIntent = {}
        )
    }
}
