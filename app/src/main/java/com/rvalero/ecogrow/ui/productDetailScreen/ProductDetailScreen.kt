package com.rvalero.ecogrow.ui.productDetailScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import coil3.compose.AsyncImage
import com.example.compose.EcoGrowTheme
import com.rvalero.ecogrow.R
import com.rvalero.ecogrow.domain.model.ProductDetail
import com.rvalero.ecogrow.ui.components.EcoGrowBadge
import com.rvalero.ecogrow.ui.components.EcoGrowDetailTopBar
import com.rvalero.ecogrow.ui.components.EcoGrowMetaStrip
import com.rvalero.ecogrow.ui.components.EcoGrowQuantitySelector
import com.rvalero.ecogrow.ui.components.MetaStripItem
import com.rvalero.ecogrow.ui.util.UiEvent
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ProductDetailViewModelScreen(
    productId: Long,
    onNavigateBack: () -> Unit,
    viewModel: ProductDetailViewModel = koinViewModel { parametersOf(productId) }
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is UiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
                    is UiEvent.NavigateBack -> onNavigateBack()
                    is UiEvent.NavigateTo -> {}
                }
            }
        }
    }

    ProductDetailScreen(
        state = uiState,
        onIntent = viewModel::handleIntent
    )
}

@Composable
fun ProductDetailScreen(
    state: ProductDetailState,
    onIntent: (ProductDetailIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    if (state.isLoading) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val product = state.product
    if (product == null) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = state.error ?: stringResource(R.string.error_generic),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 90.dp)
        ) {
            HeroSection(
                imagenes = product.imagenes,
                selectedImageIndex = state.selectedImageIndex,
                onImageSelected = { onIntent(ProductDetailIntent.SelectImage(it)) },
                onBack = { onIntent(ProductDetailIntent.NavigateBack) },
                precio = product.precio,
                unidad = product.unidad
            )

            TitleBlock(product = product)

            ProducerRow(
                producerName = product.productorNombre,
                producerLocation = product.productorLocalidad
            )

            DescriptionSection(description = product.descripcion)

            val metaItems = buildList {
                if (product.productorLocalidad.isNotBlank()) add(MetaStripItem(stringResource(R.string.product_detail_meta_origin), product.productorLocalidad))
                add(MetaStripItem(stringResource(R.string.product_detail_meta_unit), product.unidad))
                if (product.categoria.isNotBlank()) add(MetaStripItem(stringResource(R.string.product_detail_meta_category), product.categoria))
            }
            EcoGrowMetaStrip(
                items = metaItems,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        BottomActionBar(
            quantity = state.quantity,
            price = product.precio,
            onIncrement = { onIntent(ProductDetailIntent.IncrementQuantity) },
            onDecrement = { onIntent(ProductDetailIntent.DecrementQuantity) },
            onAddToCart = { onIntent(ProductDetailIntent.AddToCart) },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun HeroSection(
    imagenes: List<String>,
    selectedImageIndex: Int,
    onImageSelected: (Int) -> Unit,
    onBack: () -> Unit,
    precio: Double,
    unidad: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(420.dp)
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        val currentUrl = imagenes.getOrNull(selectedImageIndex)
        if (currentUrl != null) {
            AsyncImage(
                model = currentUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                imageVector = Icons.Outlined.Eco,
                contentDescription = null,
                modifier = Modifier
                    .size(160.dp)
                    .align(Alignment.Center),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        EcoGrowDetailTopBar(onBack = onBack)


        if (imagenes.size > 1) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 60.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                imagenes.forEachIndexed { index, url ->
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { onImageSelected(index) }
                            .then(
                                if (index == selectedImageIndex) Modifier.border(
                                    2.dp,
                                    MaterialTheme.colorScheme.primary,
                                    RoundedCornerShape(10.dp)
                                ) else Modifier
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = url,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(10.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }

        // Floating price chip
        Surface(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = 20.dp, y = 22.dp)
                .shadow(8.dp, RoundedCornerShape(14.dp)),
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.primary
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                Text(
                    text = stringResource(R.string.product_detail_price_label),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                    letterSpacing = 0.8.sp,
                    fontSize = 10.sp
                )
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = String.format("%.2f €", precio),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary,
                        lineHeight = 26.sp
                    )
                    Text(
                        text = "/$unidad",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun TitleBlock(product: ProductDetail) {
    Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 44.dp)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(bottom = 10.dp)
        ) {
            if (product.categoria.isNotBlank()) {
                EcoGrowBadge(text = product.categoria)
            }
            if (product.productorVerificado) {
                EcoGrowBadge(text = stringResource(R.string.product_detail_verified))
            }
            EcoGrowBadge(text = stringResource(R.string.product_detail_season))
        }

        Text(
            text = product.nombre,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 36.sp
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 4.dp)
        ) {
            Text(
                text = product.unidad,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
            if (product.stock > 0) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .size(3.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.outline)
                )
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                )
                Text(
                    text = " ${product.stock} ${stringResource(R.string.product_detail_available)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ProducerRow(producerName: String, producerLocation: String) {
    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(999.dp))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(999.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .padding(start = 10.dp, end = 12.dp, top = 10.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Eco,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp)
        ) {
            Text(
                text = producerName,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (producerLocation.isNotBlank()) {
                Text(
                    text = producerLocation,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
        Text(
            text = stringResource(R.string.product_detail_view_profile),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(end = 6.dp)
        )
    }
}

@Composable
private fun DescriptionSection(description: String) {
    if (description.isBlank()) return

    val primaryColor = MaterialTheme.colorScheme.primary
    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    val strokeWidth = 3.dp.toPx()
                    drawLine(
                        color = primaryColor,
                        start = Offset(0f, 0f),
                        end = Offset(0f, size.height),
                        strokeWidth = strokeWidth
                    )
                }
                .padding(start = 14.dp, top = 2.dp, bottom = 2.dp)
        ) {
            Text(
                text = "«${description.take(80)}»",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 24.sp
            )
        }

        if (description.length > 80) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun BottomActionBar(
    quantity: Int,
    price: Double,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onAddToCart: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            EcoGrowQuantitySelector(
                quantity = quantity,
                onIncrement = onIncrement,
                onDecrement = onDecrement
            )

            val total = price * quantity
            Button(
                onClick = onAddToCart,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "${stringResource(R.string.product_detail_add_to_cart)} · ${String.format("%.2f €", total)}",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ProductDetailScreenPreview() {
    EcoGrowTheme {
        ProductDetailScreen(
            state = ProductDetailState(
                product = ProductDetail(
                    id = 1,
                    nombre = "Naranjas ecológicas",
                    descripcion = "Naranjas de Valencia cultivadas sin pesticidas ni fertilizantes químicos. Recogidas en su punto óptimo de maduración.",
                    precio = 2.50,
                    unidad = "kg",
                    stock = 200,
                    disponible = true,
                    productorId = 1,
                    productorNombre = "Huerta El Sol",
                    productorLocalidad = "Valencia",
                    productorVerificado = true,
                    categoria = "Frutas"
                ),
                quantity = 2
            ),
            onIntent = {}
        )
    }
}
