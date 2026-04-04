package com.rvalero.ecogrow.ui.loginScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.example.compose.EcoGrowTheme
import com.rvalero.ecogrow.R
import com.rvalero.ecogrow.ui.components.EcoGrowButton
import com.rvalero.ecogrow.ui.components.EcoGrowTextField
import com.rvalero.ecogrow.ui.util.navigation.Routes
import com.rvalero.ecogrow.ui.util.UiEvent
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginViewModelScreen(
    viewModel: LoginViewModel = koinViewModel(),
    onNavigateToRegister: () -> Unit = {},
    onNavigateToHome: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is UiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
                    is UiEvent.NavigateTo -> {
                        if (event.route is Routes.RegisterRoute) {
                            onNavigateToRegister()
                        }

                        if(event.route is Routes.HomeRoute){
                            onNavigateToHome()
                        }
                    }
                    is UiEvent.NavigateBack -> {}
                }
            }
        }
    }

    LoginScreen(
        state = uiState,
        snackbarHostState = snackbarHostState,
        onIntent = viewModel::handleIntent
    )
}

@Composable
fun LoginScreen(
    state: LoginState,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onIntent: (LoginIntent) -> Unit
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.surface
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(top = 48.dp, bottom = 40.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Eco,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .offset(y = (-20).dp),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = stringResource(R.string.login_title),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    EcoGrowTextField(
                        value = state.email,
                        onValueChange = { onIntent(LoginIntent.EmailChanged(it)) },
                        label = stringResource(R.string.field_email),
                        keyboardType = KeyboardType.Email,
                        modifier = Modifier.fillMaxWidth()
                    )

                    EcoGrowTextField(
                        value = state.password,
                        onValueChange = { onIntent(LoginIntent.PasswordChanged(it)) },
                        label = stringResource(R.string.field_password),
                        isPassword = true,
                        isPasswordVisible = state.isPasswordVisible,
                        onTogglePasswordVisibility = { onIntent(LoginIntent.TogglePasswordVisibility) },
                        keyboardType = KeyboardType.Password,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            EcoGrowButton(
                text = stringResource(R.string.btn_login),
                onClick = { onIntent(LoginIntent.Submit) },
                isLoading = state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .offset(y = (-8).dp)
            )

            TextButton(
                onClick = { onIntent(LoginIntent.NavigateToRegister) },
                modifier = Modifier.offset(y = (-4).dp)
            ) {
                Text(
                    text = stringResource(R.string.link_no_account),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoginScreenPreview() {
    EcoGrowTheme {
        LoginScreen(
            state = LoginState(),
            onIntent = {}
        )
    }
}
