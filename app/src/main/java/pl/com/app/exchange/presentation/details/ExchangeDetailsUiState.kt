package pl.com.app.exchange.presentation.details

import pl.com.app.exchange.domain.model.details.RateDetails
import pl.com.app.exchange.domain.model.details.RateItem

data class ExchangeDetailsUiState(
	val isLoading: Boolean = true,
	val isError: Boolean = false,
	val mid: Double = 0.0,
	val rateDetails: RateDetails? = null,
	val lastRates: List<RateItem> = emptyList()
)
