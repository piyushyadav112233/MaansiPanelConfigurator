package com.maansiauto.panelconfigurator.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.maansiauto.panelconfigurator.ui.screens.ConfirmationScreen
import com.maansiauto.panelconfigurator.ui.screens.LandingScreen
import com.maansiauto.panelconfigurator.ui.screens.TeamViewScreen
import com.maansiauto.panelconfigurator.ui.screens.WizardScreen
import com.maansiauto.panelconfigurator.viewmodel.ConfiguratorViewModel

object Routes {
    const val LANDING = "landing"
    const val WIZARD = "wizard"
    const val CONFIRMATION = "confirmation"
    const val TEAM = "team"
}

@Composable
fun AppNavigation(viewModel: ConfiguratorViewModel) {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.LANDING) {
        composable(Routes.LANDING) {
            LandingScreen(
                onStart = {
                    viewModel.resetWizard()
                    navController.navigate(Routes.WIZARD)
                },
                onTeamView = { navController.navigate(Routes.TEAM) }
            )
        }
        composable(Routes.WIZARD) {
            WizardScreen(
                viewModel = viewModel,
                onCancel = { navController.popBackStack(Routes.LANDING, inclusive = false) },
                onSubmitted = {
                    navController.navigate(Routes.CONFIRMATION) {
                        popUpTo(Routes.WIZARD) { inclusive = true }
                    }
                },
                onTeamView = { navController.navigate(Routes.TEAM) }
            )
        }
        composable(Routes.CONFIRMATION) {
            ConfirmationScreen(
                viewModel = viewModel,
                onHome = { navController.popBackStack(Routes.LANDING, inclusive = false) },
                onConfigureAnother = {
                    viewModel.resetWizard()
                    navController.navigate(Routes.WIZARD) {
                        popUpTo(Routes.LANDING)
                    }
                },
                onTeamView = { navController.navigate(Routes.TEAM) }
            )
        }
        composable(Routes.TEAM) {
            TeamViewScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
