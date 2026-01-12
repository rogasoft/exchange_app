package pl.com.app.exchange.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import pl.com.app.exchange.common.NetworkManager
import pl.com.app.exchange.common.date.DateRangeProvider
import pl.com.app.exchange.data.api.DataResult
import pl.com.app.exchange.data.api.ExchangeService
import pl.com.app.exchange.data.source.ExchangePagingSource
import pl.com.app.exchange.domain.mapper.ExchangeMapper
import pl.com.app.exchange.domain.mapper.RateMapper
import pl.com.app.exchange.domain.model.details.RateDetails
import pl.com.app.exchange.domain.model.list.Rate
import pl.com.app.exchange.domain.repository.ExchangeRepository

class ExchangeRepositoryImpl(
	private val ioDispatcher: CoroutineDispatcher,
	private val networkManager: NetworkManager,
	private val exchangeService: ExchangeService,
	private val exchangeMapper: ExchangeMapper,
	private val rateMapper: RateMapper,
	private val dateRangeProvider: DateRangeProvider
) : ExchangeRepository {

	private val tables = listOf("a", "b")

	override fun getExchanges(): Pager<Int, Rate> =
		Pager(
			config = PagingConfig(
				pageSize = 100,
				enablePlaceholders = false
			),
			pagingSourceFactory = {
				ExchangePagingSource(
					networkManager = networkManager,
					exchangeService = exchangeService,
					exchangeMapper = exchangeMapper,
					tables = tables
				)
			}
		)

	override suspend fun getRateDetails(table: String, code: String): Flow<DataResult<RateDetails>> = flow {
		networkManager.safeApiCall { exchangeService.getRateDetails(table = table, code = code) }
			.let { result ->
				when(result) {
					is DataResult.Success -> DataResult.Success(data = rateMapper.mapDtoToDomain(result.data))
					is DataResult.Error -> result
					is DataResult.NetworkError -> DataResult.NetworkError
				}
			}.let { result -> emit(result) }
	}.flowOn(ioDispatcher)

	override suspend fun getLastRates(table: String, code: String): Flow<DataResult<RateDetails>> = flow {
		networkManager.safeApiCall {
			dateRangeProvider.getCurrentAndTwoWeeksAgo().let { (startDate, endDate) ->
				exchangeService.getLastRates(
					table = table,
					code = code,
					startDate = startDate,
					endDate = endDate
				)
			}
		}
			.let { result ->
				when(result) {
					is DataResult.Success -> DataResult.Success(data = rateMapper.mapDtoToDomain(result.data))
					is DataResult.Error -> result
					is DataResult.NetworkError -> DataResult.NetworkError
				}
			}.let { result -> emit(result) }
	}.flowOn(ioDispatcher)
}