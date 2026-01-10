package pl.com.app.exchange.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
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
		currentMid: BigDecimal
	): Flow<DataResult<RateDetails>> =
		exchangeRepository.getLastRates(table, code)
			.map { result ->
				when (result) {
					is DataResult.Success -> {
						val updatedRates = withContext(defaultDispatcher) {
							result.data.rates.compareWithCurrent(currentMid = currentMid)
						}

						result.copy(
							data = result.data.copy(rates = updatedRates)
						)
					}

					is DataResult.Error -> result
					is DataResult.NetworkError -> result
				}
			}

	fun List<RateItem>.compareWithCurrent(currentMid: BigDecimal): List<RateItem> = map {
		it.copy(isMoreThanTenPercent = currentMid.differsByAtLeast10Percent(BigDecimal(it.mid)))
	}

	fun BigDecimal.differsByAtLeast10Percent(other: BigDecimal): Boolean {
		if (this == BigDecimal.ZERO) return other != BigDecimal.ZERO

		val threshold = this.multiply(BigDecimal("0.1"))
		return this.subtract(other).abs() >= threshold
	}
}