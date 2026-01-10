package pl.com.app.exchange.domain.model.details

data class RateItem(
	val effectiveDate: String,
	val mid: Double,
	val isMoreThanTenPercent: Boolean = false
)