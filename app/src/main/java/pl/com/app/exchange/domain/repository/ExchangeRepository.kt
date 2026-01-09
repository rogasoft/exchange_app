package pl.com.app.exchange.domain.repository

import kotlinx.coroutines.flow.Flow
import pl.com.app.exchange.data.api.DataResult
import pl.com.app.exchange.domain.model.details.RateDetails
import pl.com.app.exchange.domain.model.list.Exchange

interface ExchangeRepository {

	suspend fun getExchanges(table: String): Flow<DataResult<Exchange>>

	suspend fun getRateDetails(table: String, code: String): Flow<DataResult<RateDetails>>

	suspend fun getLastRates(table: String, code: String): Flow<DataResult<RateDetails>>
}