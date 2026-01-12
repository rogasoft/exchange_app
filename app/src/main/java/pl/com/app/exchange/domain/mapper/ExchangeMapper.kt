package pl.com.app.exchange.domain.mapper

import pl.com.app.exchange.data.dto.list.ExchangeResponse
import pl.com.app.exchange.data.dto.list.RateResponse
import pl.com.app.exchange.domain.model.list.Rate

class ExchangeMapper {

	fun mapRateDtoToDomain(list: List<ExchangeResponse>): List<Rate> =
		list.flatMap { exchange -> mapRates(table = exchange.table, rates = exchange.rates) }

	private fun mapRates(table: String, rates: List<RateResponse>): List<Rate> =
		rates.map { Rate(table = table, currency = it.currency, code = it.code, mid = it.mid) }
}