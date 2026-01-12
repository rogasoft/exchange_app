package pl.com.app.exchange.domain.model.details

data class RateDetails(
	val table: String,
	val currency: String,
	val code: String,
	val rates: List<RateItem>
)