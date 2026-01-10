package pl.com.app.exchange.domain.repository

import androidx.paging.Pager
import kotlinx.coroutines.flow.Flow
import pl.com.app.exchange.data.api.DataResult
import pl.com.app.exchange.domain.model.details.RateDetails
import pl.com.app.exchange.domain.model.list.Exchange
import pl.com.app.exchange.domain.model.list.Rate

interface ExchangeRepository {

	fun getExchanges(): Pager<Int, Rate>

	suspend fun getRateDetails(table: String, code: String): Flow<DataResult<RateDetails>>

	suspend fun getLastRates(table: String, code: String): Flow<DataResult<RateDetails>>
}