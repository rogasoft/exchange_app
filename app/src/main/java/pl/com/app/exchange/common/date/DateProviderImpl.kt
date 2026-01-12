package pl.com.app.exchange.common.date

import java.time.LocalDate

class DateProviderImpl : DateProvider {

	override fun today(): LocalDate = LocalDate.now()
}