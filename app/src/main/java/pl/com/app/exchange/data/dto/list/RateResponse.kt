package pl.com.app.exchange.data.dto.list

data class RateResponse(
	val currency: String,
	val code: String,
	val mid: Double
)