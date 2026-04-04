package com.rvalero.ecogrow.ui.util.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.rvalero.ecogrow.ui.activationScreen.ActivationViewModelScreen
import com.rvalero.ecogrow.ui.homeScreen.HomeScreenViewModel
import com.rvalero.ecogrow.ui.loginScreen.LoginViewModelScreen
import com.rvalero.ecogrow.ui.registerScreen.RegisterViewModelScreen
@Composable
fun EcoGrowNavHost() {
    val backStack = rememberNavBackStack(Routes.LoginRoute)

    fun navigateTo(route: Routes) { backStack.add(route) }

    fun pop() { backStack.removeLastOrNull() }

    NavDisplay(
        backStack = backStack,
        onBack = { pop() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),

        entryProvider = entryProvider {
            entry<Routes.LoginRoute> {
                LoginViewModelScreen(
                    onNavigateToRegister = {
                        navigateTo(Routes.RegisterRoute)
                    },
                    onNavigateToHome = {
                        navigateTo(Routes.HomeRoute)
                    }
                )
            }

            entry<Routes.RegisterRoute> {
                RegisterViewModelScreen(
                    onNavigateToLogin = dropUnlessResumed { pop() },
                    onNavigateToActivation = { email ->
                        navigateTo(Routes.ActivationRoute(email))
                    }
                )
            }

            entry<Routes.ActivationRoute> { route ->
                ActivationViewModelScreen(
                    email = route.email,
                    onNavigateToLogin = dropUnlessResumed {
                        backStack.removeAll { it !is Routes.LoginRoute }
                    }
                )
            }

            entry<Routes.HomeRoute> {
                HomeScreenViewModel(

                )
            }
        }
    )
}
