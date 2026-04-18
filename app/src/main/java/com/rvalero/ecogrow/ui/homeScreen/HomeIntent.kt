package com.rvalero.ecogrow.ui.homeScreen

sealed interface HomeIntent {
    data object LoadData : HomeIntent
}