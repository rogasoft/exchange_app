package pl.com.app.exchange.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import pl.com.app.exchange.common.extension.differsByAtLeast10Percent
import pl.com.app.exchange.data.api.DataResult
import pl.com.app.exchange.domain.model.details.RateDetails
import pl.com.app.exchange.domain.model.details.RateItem
import pl.com.app.exchange.domain.repository.ExchangeRepository
import java.math.BigDecimal
import kotlin.collections.map

class GetLastRatesUseCase(
	private val defaultDispatcher: CoroutineDispatcher,
	private val exchangeRepository: ExchangeRepository,
) {
	suspend operator fun invoke(
		table: String,
		code: String,
		currentMid: Double
	): Flow<DataResult<RateDetails>> =
		exchangeRepository.getLastRates(table, code)
			.map { result ->
				when (result) {
					is DataResult.Success -> {
						withContext(defaultDispatcher) {
							result.data.rates.compareWithCurrent(currentMid = currentMid)
						}.let { updatedRates ->
							result.copy(data = result.data.copy(rates = updatedRates))
						}
					}
					is DataResult.Error,
					is DataResult.NetworkError -> result
				}
			}

	private fun List<RateItem>.compareWithCurrent(currentMid: Double): List<RateItem> = map {
		it.copy(isMoreThanTenPercent = BigDecimal(currentMid).differsByAtLeast10Percent(BigDecimal(it.mid)))
	}
}