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
				rates = mapRates("", rates)
			)
		}

	fun mapRateDtoToDomain(list: List<ExchangeResponse>): List<Rate> =
		list.firstNotNullOf { exchange -> mapRates(table = exchange.table, rates = exchange.rates) }

	private fun mapRates(table: String, rates: List<RateResponse>): List<Rate> =
		rates.map { Rate(table = table, currency = it.currency, code = it.code, mid = it.mid) }
}