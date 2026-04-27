package com.rvalero.ecogrow.ui.publishProductScreen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import coil3.compose.AsyncImage
import com.example.compose.EcoGrowTheme
import com.rvalero.ecogrow.R
import com.rvalero.ecogrow.ui.util.LocalSnackbarHostState
import com.rvalero.ecogrow.ui.util.UiEvent
import org.koin.androidx.compose.koinViewModel

@Composable
fun PublishProductViewModelScreen(
    viewModel: PublishProductViewModel = koinViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val snackbarHostState = LocalSnackbarHostState.current

    val remainingSlots = (PublishProductOptions.MAX_PHOTOS - uiState.photos.size)
        .coerceAtLeast(1)
    val pickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = remainingSlots)
    ) { uris ->
        if (uris.isNotEmpty()) {
            viewModel.handleIntent(
                PublishProductIntent.PhotosPicked(uris.map { it.toString() })
            )
        }
    }

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is UiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
                    else -> Unit
                }
            }
        }
    }

    PublishProductScreen(
        state = uiState,
        onIntent = viewModel::handleIntent,
        onAddPhotoClick = {
            pickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        },
        modifier = modifier
    )
}

@Composable
fun PublishProductScreen(
    state: PublishProductState,
    onIntent: (PublishProductIntent) -> Unit,
    onAddPhotoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        PublishProductTopBar(
            isFormValid = state.isFormValid,
            isLoading = state.isLoading,
            onSubmit = { onIntent(PublishProductIntent.Submit) }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 24.dp)
        ) {
            PhotosSection(
                photos = state.photos,
                onAddPhotoClick = onAddPhotoClick,
                onPhotoRemoved = { uri -> onIntent(PublishProductIntent.PhotoRemoved(uri)) }
            )

            BasicDataSection(
                state = state,
                onIntent = onIntent
            )

            PriceStockSection(
                state = state,
                onIntent = onIntent
            )

            AvailabilityToggle(
                checked = state.disponible,
                onCheckedChange = { onIntent(PublishProductIntent.DisponibleChanged(it)) }
            )
        }
    }
}

@Composable
private fun PublishProductTopBar(
    isFormValid: Boolean,
    isLoading: Boolean,
    onSubmit: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.publish_product_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        val canSubmit = isFormValid && !isLoading
        val submitColor = if (canSubmit) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.outline

        Box(
            modifier = Modifier
                .clickable(enabled = canSubmit, onClick = onSubmit)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = stringResource(R.string.publish_product_action),
                color = submitColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun PhotosSection(
    photos: List<String>,
    onAddPhotoClick: () -> Unit,
    onPhotoRemoved: (String) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)) {
        SectionLabel(text = stringResource(R.string.publish_product_section_photos))
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            photos.forEach { uri ->
                PhotoTile(uri = uri, onRemove = { onPhotoRemoved(uri) })
            }
            if (photos.size < PublishProductOptions.MAX_PHOTOS) {
                AddPhotoTile(onClick = onAddPhotoClick)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.publish_product_photo_hint),
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@Composable
private fun PhotoTile(uri: String, onRemove: () -> Unit) {
    Box(modifier = Modifier.size(96.dp)) {
        AsyncImage(
            model = uri,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(14.dp))
                .background(MaterialTheme.colorScheme.surfaceContainer)
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp)
                .size(22.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.6f))
                .clickable(onClick = onRemove),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = stringResource(R.string.publish_product_photo_remove),
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

@Composable
private fun AddPhotoTile(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(96.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .border(
                width = 1.5.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.publish_product_photo_add),
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun BasicDataSection(
    state: PublishProductState,
    onIntent: (PublishProductIntent) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SectionLabel(text = stringResource(R.string.publish_product_section_basic))

        OutlinedTextField(
            value = state.nombre,
            onValueChange = { onIntent(PublishProductIntent.NombreChanged(it)) },
            label = { Text(stringResource(R.string.publish_product_field_name)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = state.descripcion,
            onValueChange = { onIntent(PublishProductIntent.DescripcionChanged(it)) },
            label = { Text(stringResource(R.string.publish_product_field_description)) },
            modifier = Modifier.fillMaxWidth()
        )

        CategoryDropdownField(
            label = stringResource(R.string.publish_product_field_category),
            selectedId = state.categoriaId,
            options = PublishProductOptions.categorias,
            onSelected = { onIntent(PublishProductIntent.CategoriaIdChanged(it)) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun PriceStockSection(
    state: PublishProductState,
    onIntent: (PublishProductIntent) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(
                value = state.precio,
                onValueChange = { onIntent(PublishProductIntent.PrecioChanged(it)) },
                label = { Text(stringResource(R.string.publish_product_field_price)) },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                )
            )
            DropdownField(
                label = stringResource(R.string.publish_product_field_unit),
                value = state.unidad,
                options = PublishProductOptions.unidades,
                onValueChange = { onIntent(PublishProductIntent.UnidadChanged(it)) },
                modifier = Modifier.weight(1f)
            )
        }
        OutlinedTextField(
            value = state.stock,
            onValueChange = { onIntent(PublishProductIntent.StockChanged(it)) },
            label = { Text(stringResource(R.string.publish_product_field_stock)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownField(
    label: String,
    value: String,
    options: List<String>,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            singleLine = true,
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = true)
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onValueChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryDropdownField(
    label: String,
    selectedId: Int?,
    options: List<Pair<Int, String>>,
    onSelected: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val displayValue = options.firstOrNull { it.first == selectedId }?.second.orEmpty()

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = displayValue,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            singleLine = true,
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = true)
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { (id, name) ->
                DropdownMenuItem(
                    text = { Text(name) },
                    onClick = {
                        onSelected(id)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun AvailabilityToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.publish_product_toggle_available_title),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = stringResource(R.string.publish_product_toggle_available_subtitle),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.75f),
                modifier = Modifier.padding(top = 2.dp)
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                checkedTrackColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text.uppercase(),
        fontSize = 11.sp,
        color = MaterialTheme.colorScheme.outline,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.8.sp
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PublishProductScreenEmptyPreview() {
    EcoGrowTheme {
        PublishProductScreen(
            state = PublishProductState(),
            onIntent = {},
            onAddPhotoClick = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PublishProductScreenFilledPreview() {
    EcoGrowTheme {
        PublishProductScreen(
            state = PublishProductState(
                nombre = "Tomates cherry",
                descripcion = "Cultivo ecológico al aire libre",
                categoriaId = 2,
                precio = "2,50",
                unidad = "kg",
                stock = "25",
                disponible = true
            ),
            onIntent = {},
            onAddPhotoClick = {}
        )
    }
}
