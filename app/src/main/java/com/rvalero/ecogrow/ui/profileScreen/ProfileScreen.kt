package com.rvalero.ecogrow.ui.profileScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.rvalero.ecogrow.domain.model.UserRole
import com.rvalero.ecogrow.ui.util.UiEvent
import com.rvalero.ecogrow.ui.util.navigation.Routes
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileViewModelScreen(
    viewModel: ProfileViewModel = koinViewModel(),
    onNavigateToBecomeProducer: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is UiEvent.NavigateTo -> when (event.route) {
                        is Routes.BecomeProducerRoute -> onNavigateToBecomeProducer()
                        is Routes.LoginRoute -> onNavigateToLogin()
                        else -> Unit
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

    if (uiState.showLogoutDialog) {
        LogoutConfirmationDialog(
            onConfirm = { viewModel.handleIntent(ProfileIntent.LogoutConfirmed) },
            onDismiss = { viewModel.handleIntent(ProfileIntent.LogoutDismissed) }
        )
    }
}

@Composable
private fun LogoutConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.dialog_logout_title)) },
        text = { Text(text = stringResource(R.string.dialog_logout_message)) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = stringResource(R.string.dialog_logout_confirm),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.dialog_logout_cancel))
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    state: ProfileState,
    onIntent: (ProfileIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.tab_profile),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
            },
            actions = {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Filled.Notifications,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                IconButton(onClick = { onIntent(ProfileIntent.SettingsClicked) }) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface
            ),
            windowInsets = WindowInsets(0, 0, 0, 0)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 8.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HeroCard(state = state)

            when (state.rol) {
                UserRole.CONSUMER -> ConsumerProfileContent(onIntent = onIntent)
                UserRole.PRODUCER -> ProducerProfileContent(onIntent = onIntent)
            }
        }
    }
}

@Composable
private fun HeroCard(state: ProfileState) {
    val iniciales = remember(state.nombre) { computeInitials(state.nombre) }
    val isProducer = state.rol == UserRole.PRODUCER
    val stats = if (isProducer) {
        listOf(
            "0" to stringResource(R.string.profile_stat_products),
            "0" to stringResource(R.string.profile_stat_in_progress),
            "—" to stringResource(R.string.profile_stat_rating)
        )
    } else {
        listOf(
            "0" to stringResource(R.string.profile_stat_orders),
            "0" to stringResource(R.string.profile_stat_favorites),
            "0" to stringResource(R.string.profile_stat_producers)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        Icon(
            imageVector = Icons.Outlined.Eco,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(220.dp)
                .offset(x = 40.dp, y = 40.dp)
        )

        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(84.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = iniciales,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = state.nombre.ifBlank { "—" },
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = state.email.ifBlank { "" },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    HeroBadge(
                        text = if (isProducer) {
                            state.nombreNegocio?.let { "Productor · $it" }
                                ?: stringResource(R.string.profile_badge_producer)
                        } else {
                            stringResource(R.string.profile_badge_consumer)
                        },
                        background = if (isProducer) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.primaryContainer,
                        foreground = if (isProducer) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MaterialTheme.colorScheme.outlineVariant)
            )
            Spacer(modifier = Modifier.height(16.dp))
            StatsRow(stats = stats)
        }
    }
}

@Composable
private fun HeroBadge(
    text: String,
    background: Color,
    foreground: Color
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(background)
            .padding(horizontal = 9.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.Eco,
            contentDescription = null,
            tint = foreground,
            modifier = Modifier.size(12.dp)
        )
        Text(
            text = text,
            color = foreground,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun StatsRow(stats: List<Pair<String, String>>) {
    Row(modifier = Modifier.fillMaxWidth()) {
        stats.forEachIndexed { index, (value, label) ->
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = label.uppercase(),
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            if (index < stats.lastIndex) {
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(36.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant)
                )
            }
        }
    }
}

@Composable
private fun ConsumerProfileContent(onIntent: (ProfileIntent) -> Unit) {
    ActivitySection(onIntent = onIntent)

    BecomeProducerCta(onClick = { onIntent(ProfileIntent.BecomeProducerClicked) })

    SectionTitle(text = stringResource(R.string.profile_section_account))

    AccountList(
        items = listOf(
            AccountItem(Icons.Filled.Edit, stringResource(R.string.profile_option_edit)) {
                onIntent(ProfileIntent.EditProfileClicked)
            },
            AccountItem(Icons.Filled.Settings, stringResource(R.string.profile_option_settings)) {
                onIntent(ProfileIntent.SettingsClicked)
            },
            AccountItem(Icons.Filled.Notifications, stringResource(R.string.profile_option_notifications)) {
                onIntent(ProfileIntent.SettingsClicked)
            }
        )
    )

    LogoutRow(onClick = { onIntent(ProfileIntent.LogoutClicked) })
}

@Composable
private fun ProducerProfileContent(onIntent: (ProfileIntent) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        SectionTitle(
            text = stringResource(R.string.profile_section_my_shop),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = stringResource(R.string.profile_view_public_page),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Medium
        )
    }

    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        ActionTile(
            modifier = Modifier.weight(1f),
            icon = Icons.Filled.LocalShipping,
            title = stringResource(R.string.profile_option_received_orders),
            subtitle = stringResource(R.string.profile_subtitle_received_orders),
            highlighted = true,
            onClick = { onIntent(ProfileIntent.ReceivedOrdersClicked) }
        )
        ActionTile(
            modifier = Modifier.weight(1f),
            icon = Icons.Filled.Inventory2,
            title = stringResource(R.string.profile_option_my_products),
            subtitle = stringResource(R.string.profile_subtitle_my_products),
            onClick = { onIntent(ProfileIntent.MyProductsClicked) }
        )
    }

    ActivitySection(onIntent = onIntent)

    SectionTitle(text = stringResource(R.string.profile_section_account))

    AccountList(
        items = listOf(
            AccountItem(Icons.Filled.Edit, stringResource(R.string.profile_option_edit)) {
                onIntent(ProfileIntent.EditProfileClicked)
            },
            AccountItem(Icons.Filled.Storefront, stringResource(R.string.profile_option_edit_shop)) {
                onIntent(ProfileIntent.EditShopClicked)
            },
            AccountItem(Icons.Filled.Settings, stringResource(R.string.profile_option_settings)) {
                onIntent(ProfileIntent.SettingsClicked)
            }
        )
    )

    LogoutRow(onClick = { onIntent(ProfileIntent.LogoutClicked) })
}

@Composable
private fun ActivitySection(onIntent: (ProfileIntent) -> Unit) {
    SectionTitle(text = stringResource(R.string.profile_section_activity))

    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        ActionTile(
            modifier = Modifier.weight(1f),
            icon = Icons.Filled.LocalShipping,
            title = stringResource(R.string.profile_option_orders),
            subtitle = stringResource(R.string.profile_subtitle_orders_pending),
            onClick = { onIntent(ProfileIntent.MyOrdersClicked) }
        )
        ActionTile(
            modifier = Modifier.weight(1f),
            icon = Icons.Filled.Favorite,
            title = stringResource(R.string.profile_option_favorites),
            subtitle = stringResource(R.string.profile_subtitle_favorites),
            onClick = { onIntent(ProfileIntent.FavoritesClicked) }
        )
    }
}

@Composable
private fun ActionTile(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    highlighted: Boolean = false
) {
    val bgColor = if (highlighted) MaterialTheme.colorScheme.primaryContainer
    else MaterialTheme.colorScheme.surfaceContainerLowest
    val borderColor = if (highlighted) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
    else MaterialTheme.colorScheme.outlineVariant
    val iconBg = if (highlighted) MaterialTheme.colorScheme.onPrimary
    else MaterialTheme.colorScheme.primaryContainer
    val subtitleColor = if (highlighted) MaterialTheme.colorScheme.onPrimaryContainer
    else MaterialTheme.colorScheme.outline

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(14.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = subtitleColor
            )
        }
        if (highlighted) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.error)
            )
        }
    }
}

@Composable
private fun BecomeProducerCta(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.primary)
            .clickable(onClick = onClick)
    ) {
        Icon(
            imageVector = Icons.Outlined.Eco,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.15f),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(160.dp)
                .offset(x = 30.dp, y = (-20).dp)
        )
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = stringResource(R.string.profile_become_producer_kicker).uppercase(),
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = stringResource(R.string.profile_become_producer_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.widthIn(max = 240.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = stringResource(R.string.profile_become_producer_subtitle),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.88f),
                modifier = Modifier.widthIn(max = 260.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(MaterialTheme.colorScheme.onPrimary)
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.profile_become_producer_cta),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = modifier.padding(horizontal = 4.dp, vertical = 4.dp)
    )
}

private data class AccountItem(
    val icon: ImageVector,
    val label: String,
    val onClick: () -> Unit
)

@Composable
private fun AccountList(items: List<AccountItem>) {
    Column {
        items.forEachIndexed { index, item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = item.onClick)
                    .padding(horizontal = 4.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier.size(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    text = item.label,
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
            if (index < items.lastIndex) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant)
                )
            }
        }
    }
}

@Composable
private fun LogoutRow(onClick: () -> Unit) {
    Spacer(modifier = Modifier.height(8.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 4.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.Logout,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error
        )
        Text(
            text = stringResource(R.string.profile_logout),
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
    }
}

private fun computeInitials(nombre: String): String =
    nombre.trim()
        .split(" ")
        .filter { it.isNotEmpty() }
        .take(2)
        .joinToString("") { it.first().uppercase() }
        .ifEmpty { "?" }

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ProfileScreenConsumerPreview() {
    EcoGrowTheme {
        ProfileScreen(
            state = ProfileState(
                nombre = "Ramón Valero",
                email = "ramon@ejemplo.com",
                rol = UserRole.CONSUMER
            ),
            onIntent = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ProfileScreenProducerPreview() {
    EcoGrowTheme {
        ProfileScreen(
            state = ProfileState(
                nombre = "Ramón Valero",
                email = "ramon@ejemplo.com",
                rol = UserRole.PRODUCER,
                nombreNegocio = "Finca La Encina"
            ),
            onIntent = {}
        )
    }
}
