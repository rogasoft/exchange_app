package pl.com.app.exchange.domain.mapper

import pl.com.app.exchange.data.dto.details.RateDetailsResponse
import pl.com.app.exchange.data.dto.details.RateItemResponse
import pl.com.app.exchange.domain.model.details.RateDetails
import pl.com.app.exchange.domain.model.details.RateItem

class RateMapper {

	fun mapDtoToDomain(dto: RateDetailsResponse): RateDetails =
		RateDetails(
			table = dto.table,
			currency = dto.currency,
			code = dto.code,
			rates = mapRates(dto.rates)
		)

	private fun mapRates(list: List<RateItemResponse>): List<RateItem> =
		list.map { RateItem(effectiveDate = it.effectiveDate, mid = it.mid) }
}