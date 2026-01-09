package pl.com.app.exchange.domain.mapper

import pl.com.app.exchange.data.dto.list.ExchangeResponse
import pl.com.app.exchange.data.dto.list.RateResponse
import pl.com.app.exchange.domain.model.list.Exchange
import pl.com.app.exchange.domain.model.list.Rate

class ExchangeMapper {

	fun mapDtoToDomain(list: List<ExchangeResponse>): Exchange =
		list.firstNotNullOf { (table, effectiveDate, rates) ->
			Exchange(
				table = table,
				effectiveDate = effectiveDate,
				rates = mapRates(rates)
			)
		}

	private fun mapRates(list: List<RateResponse>): List<Rate> =
		list.map { Rate(currency = it.currency, code = it.code, mid = it.mid) }
}