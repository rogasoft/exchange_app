package pl.com.app.exchange.common.date

import java.time.format.DateTimeFormatter

class DateRangeProvider(private val dateProvider: DateProvider) {

	private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

	fun getCurrentAndTwoWeeksAgo(): Pair<String, String> =
		dateProvider.today().let { today ->
			today.minusWeeks(2).format(formatter) to today.format(formatter)
		}

}