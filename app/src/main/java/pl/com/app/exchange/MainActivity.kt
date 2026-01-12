package pl.com.app.exchange

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import pl.com.app.exchange.presentation.navigation.NavigationContainer
import pl.com.app.exchange.ui.theme.ExchangeTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			ExchangeTheme { NavigationContainer(navController = rememberNavController()) }
		}
	}
}