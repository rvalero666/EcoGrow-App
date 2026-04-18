package com.rvalero.ecogrow.ui.homeScreen

import com.rvalero.ecogrow.domain.model.Producer
import com.rvalero.ecogrow.domain.model.Product

data class HomeState(
    val userName: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val producers: List<Producer> = emptyList(),
    val products: List<Product> = emptyList(),
    val searchQuery: String = "",
    val searchResults: List<Product> = emptyList(),
    val isSearching: Boolean = false
)