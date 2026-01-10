package pl.com.app.exchange.common.date

import java.time.LocalDate

interface DateProvider {

	fun today(): LocalDate
}