package pl.com.app.exchange.presentation.navigation

import android.annotation.SuppressLint
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import pl.com.app.exchange.presentation.details.exchangeDetails
import pl.com.app.exchange.presentation.list.exchangeList

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavigationContainer(navController: NavHostController) {
	Scaffold(
		modifier = Modifier
			.fillMaxSize()
			.systemBarsPadding()
			.navigationBarsPadding()
	) {
		NavHost(
			modifier = Modifier
				.fillMaxSize()
				.background(Color.White),
			navController = navController,
			startDestination = Route.ExchangeList,
			enterTransition = { EnterTransition.None },
			exitTransition = { ExitTransition.None }
		) {
			with(navController) {
				exchangeList(clickItemAction = { table, code, mid ->
					navigate(Route.ExchangeDetails(table = table, code = code, mid = mid)) }
				)
				exchangeDetails()
			}
		}
	}
}