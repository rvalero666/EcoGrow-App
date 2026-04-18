package com.rvalero.ecogrow.ui.homeScreen

sealed interface HomeIntent {
    data object LoadData : HomeIntent
    data class SearchQueryChanged(val query: String) : HomeIntent
    data object ClearSearch : HomeIntent
}