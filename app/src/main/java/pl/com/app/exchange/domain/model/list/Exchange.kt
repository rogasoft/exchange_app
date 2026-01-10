package pl.com.app.exchange.domain.model.list

data class Exchange(
	val table: String,
	val effectiveDate: String,
	val rates: List<Rate>
)