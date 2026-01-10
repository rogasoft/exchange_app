package pl.com.app.exchange.presentation.list

import pl.com.app.exchange.domain.model.list.Exchange

data class ExchangeListUiState(
	val isLoading: Boolean = true,
	val isError: Boolean = false,
	val exchange: Exchange? = null
)