package pl.com.app.exchange.data.dto.details

data class RateDetailsResponse(
	val table: String,
	val currency: String,
	val code: String,
	val rates: List<RateItemResponse>
)