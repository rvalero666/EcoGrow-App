package com.rvalero.ecogrow.ui.profileScreen

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.example.compose.EcoGrowTheme
import com.rvalero.ecogrow.R
import com.rvalero.ecogrow.domain.model.UserRole
import com.rvalero.ecogrow.ui.components.EcoGrowButton
import com.rvalero.ecogrow.ui.util.UiEvent
import com.rvalero.ecogrow.ui.util.navigation.Routes
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileViewModelScreen(
    viewModel: ProfileViewModel = koinViewModel(),
    onNavigateToBecomeProducer: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is UiEvent.NavigateTo -> {
                        if (event.route is Routes.BecomeProducerRoute) {
                            onNavigateToBecomeProducer()
                        }
                    }
                    else -> Unit
                }
            }
        }
    }

    ProfileScreen(
        state = uiState,
        onIntent = viewModel::handleIntent,
        modifier = modifier
    )
}

@Composable
fun ProfileScreen(
    state: ProfileState,
    onIntent: (ProfileIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.surface)
    ) {
        ProfileHeader(
            nombre = state.nombre,
            rol = state.rol
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (state.rol) {
            UserRole.CONSUMER -> ConsumerProfileContent(onIntent = onIntent)
            UserRole.PRODUCER -> ProducerProfileContent(onIntent = onIntent)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun ProfileHeader(
    nombre: String,
    rol: UserRole
) {
    val iniciales = remember(nombre) {
        nombre.trim()
            .split(" ")
            .filter { it.isNotEmpty() }
            .take(2)
            .joinToString("") { it.first().uppercase() }
            .ifEmpty { "?" }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(top = 24.dp, bottom = 32.dp, start = 20.dp, end = 20.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = iniciales,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = nombre.ifBlank { stringResource(R.string.tab_profile) },
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(
                    if (rol == UserRole.PRODUCER) R.string.profile_badge_producer
                    else R.string.profile_badge_consumer
                ),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)
            )
        }
    }
}

@Composable
private fun ConsumerProfileContent(
    onIntent: (ProfileIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BecomeProducerCard(onClick = { onIntent(ProfileIntent.BecomeProducerClicked) })

        SectionTitle(text = stringResource(R.string.profile_section_options))

        ProfileOptionsCard {
            ProfileOptionItem(
                icon = Icons.Filled.ShoppingBag,
                label = stringResource(R.string.profile_option_orders),
                onClick = { onIntent(ProfileIntent.MyOrdersClicked) }
            )
            ProfileOptionItem(
                icon = Icons.Filled.Favorite,
                label = stringResource(R.string.profile_option_favorites),
                onClick = { onIntent(ProfileIntent.FavoritesClicked) }
            )
            ProfileOptionItem(
                icon = Icons.Filled.Edit,
                label = stringResource(R.string.profile_option_edit),
                onClick = { onIntent(ProfileIntent.EditProfileClicked) }
            )
            ProfileOptionItem(
                icon = Icons.Filled.Notifications,
                label = stringResource(R.string.profile_option_notifications),
                onClick = { onIntent(ProfileIntent.SettingsClicked) }
            )
            ProfileOptionItem(
                icon = Icons.Filled.Settings,
                label = stringResource(R.string.profile_option_settings),
                onClick = { onIntent(ProfileIntent.SettingsClicked) },
                showDivider = false
            )
        }
    }
}

@Composable
private fun ProducerProfileContent(
    onIntent: (ProfileIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SectionTitle(text = stringResource(R.string.profile_section_my_shop))

        ProfileOptionsCard {
            ProfileOptionItem(
                icon = Icons.Filled.Inventory2,
                label = stringResource(R.string.profile_option_my_products),
                onClick = { onIntent(ProfileIntent.MyProductsClicked) }
            )
            ProfileOptionItem(
                icon = Icons.Filled.ShoppingBag,
                label = stringResource(R.string.profile_option_received_orders),
                onClick = { onIntent(ProfileIntent.ReceivedOrdersClicked) }
            )
            ProfileOptionItem(
                icon = Icons.Filled.Storefront,
                label = stringResource(R.string.profile_option_edit_shop),
                onClick = { onIntent(ProfileIntent.EditShopClicked) },
                showDivider = false
            )
        }

        SectionTitle(text = stringResource(R.string.profile_section_options))

        ProfileOptionsCard {
            ProfileOptionItem(
                icon = Icons.Filled.Edit,
                label = stringResource(R.string.profile_option_edit),
                onClick = { onIntent(ProfileIntent.EditProfileClicked) }
            )
            ProfileOptionItem(
                icon = Icons.Filled.Notifications,
                label = stringResource(R.string.profile_option_notifications),
                onClick = { onIntent(ProfileIntent.SettingsClicked) }
            )
            ProfileOptionItem(
                icon = Icons.Filled.Settings,
                label = stringResource(R.string.profile_option_settings),
                onClick = { onIntent(ProfileIntent.SettingsClicked) },
                showDivider = false
            )
        }
    }
}

@Composable
private fun BecomeProducerCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = stringResource(R.string.profile_become_producer_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = stringResource(R.string.profile_become_producer_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            EcoGrowButton(
                text = stringResource(R.string.profile_become_producer_cta),
                onClick = onClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
private fun ProfileOptionsCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(vertical = 4.dp)) {
            content()
        }
    }
}

@Composable
private fun ProfileOptionItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    showDivider: Boolean = true
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        if (showDivider) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 48.dp)
                    .height(1.dp)
                    .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ProfileScreenConsumerPreview() {
    EcoGrowTheme {
        ProfileScreen(
            state = ProfileState(nombre = "Ramón Lorina", rol = UserRole.CONSUMER),
            onIntent = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ProfileScreenProducerPreview() {
    EcoGrowTheme {
        ProfileScreen(
            state = ProfileState(nombre = "Finca La Encina", rol = UserRole.PRODUCER),
            onIntent = {}
        )
    }
}
