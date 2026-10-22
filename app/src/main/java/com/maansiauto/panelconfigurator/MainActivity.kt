package com.maansiauto.panelconfigurator

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.maansiauto.panelconfigurator.navigation.AppNavigation
import com.maansiauto.panelconfigurator.ui.theme.MaansiPanelConfiguratorTheme
import com.maansiauto.panelconfigurator.viewmodel.ConfiguratorViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaansiApp()
        }
    }
}

@Composable
fun MaansiApp() {
    val viewModel: ConfiguratorViewModel = viewModel(
        factory = ConfiguratorViewModel.Factory(
            application = androidx.compose.ui.platform.LocalContext.current.applicationContext as Application
        )
    )
    MaansiPanelConfiguratorTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            AppNavigation(viewModel = viewModel)
        }
    }
}
