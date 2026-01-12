package pl.com.app.exchange.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Route {

	@Serializable
	data object ExchangeList : Route()

	@Serializable
	data class ExchangeDetails(val table: String, val code: String, val mid: Double) : Route()
}