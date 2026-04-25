package com.rvalero.ecogrow.ui.profileScreen

sealed interface ProfileIntent {
    data object BecomeProducerClicked : ProfileIntent
    data object EditProfileClicked : ProfileIntent
    data object MyOrdersClicked : ProfileIntent
    data object FavoritesClicked : ProfileIntent
    data object SettingsClicked : ProfileIntent
    data object MyProductsClicked : ProfileIntent
    data object ReceivedOrdersClicked : ProfileIntent
    data object EditShopClicked : ProfileIntent
    data object LogoutClicked : ProfileIntent
    data object LogoutConfirmed : ProfileIntent
    data object LogoutDismissed : ProfileIntent
}
