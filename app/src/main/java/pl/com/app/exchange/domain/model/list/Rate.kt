package pl.com.app.exchange.domain.model.list

data class Rate(
	val currency: String,
	val code: String,
	val mid: Double
)