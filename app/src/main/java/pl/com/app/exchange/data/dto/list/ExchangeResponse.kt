package pl.com.app.exchange.data.dto.list

data class ExchangeResponse(
	val table: String,
	val effectiveDate: String,
	val rates: List<RateResponse>
)