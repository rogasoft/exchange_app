package pl.com.app.exchange.presentation.details

import pl.com.app.exchange.domain.model.details.RateDetails

data class DetailsUiState(
	val isLoading: Boolean = true,
	val isError: Boolean = false,
	val rateDetails: RateDetails? = null,
	val lastRates: RateDetails? = null
)
