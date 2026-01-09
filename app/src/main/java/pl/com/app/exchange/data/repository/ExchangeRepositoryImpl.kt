package pl.com.app.exchange.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import pl.com.app.exchange.common.NetworkManager
import pl.com.app.exchange.common.date.DateRangeProvider
import pl.com.app.exchange.data.api.DataResult
import pl.com.app.exchange.data.api.ExchangeService
import pl.com.app.exchange.di.IoDispatcher
import pl.com.app.exchange.domain.mapper.ExchangeMapper
import pl.com.app.exchange.domain.mapper.RateMapper
import pl.com.app.exchange.domain.model.details.RateDetails
import pl.com.app.exchange.domain.model.list.Exchange
import pl.com.app.exchange.domain.repository.ExchangeRepository

class ExchangeRepositoryImpl(
	@IoDispatcher private val ioDispatcher: CoroutineDispatcher,
	private val networkManager: NetworkManager,
	private val exchangeService: ExchangeService,
	private val exchangeMapper: ExchangeMapper,
	private val rateMapper: RateMapper,
	private val dateRangeProvider: DateRangeProvider
) : ExchangeRepository {

	override suspend fun getExchanges(table: String): Flow<DataResult<Exchange>> = flow {
		networkManager.safeApiCall { exchangeService.getExchanges(table = table) }
			.let { result ->
				when(result) {
					is DataResult.Success ->
						DataResult.Success(
							data = exchangeMapper.mapDtoToDomain(result.data),
							code = result.code
						)

					is DataResult.Error -> result
					is DataResult.NetworkError -> DataResult.NetworkError
				}
			}.let { result -> emit(result) }
	}.flowOn(ioDispatcher)

	override suspend fun getRateDetails(table: String, code: String): Flow<DataResult<RateDetails>> = flow {
		networkManager.safeApiCall { exchangeService.getRateDetails(table = table, code = code) }
			.let { result ->
				when(result) {
					is DataResult.Success ->
						DataResult.Success(
							data = rateMapper.mapDtoToDomain(result.data),
							code = result.code
						)

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
					is DataResult.Success ->
						DataResult.Success(
							data = rateMapper.mapDtoToDomain(result.data),
							code = result.code
						)

					is DataResult.Error -> result
					is DataResult.NetworkError -> DataResult.NetworkError
				}
			}.let { result -> emit(result) }
	}.flowOn(ioDispatcher)
}