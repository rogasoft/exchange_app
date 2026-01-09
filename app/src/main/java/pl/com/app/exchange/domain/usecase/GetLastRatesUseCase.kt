package pl.com.app.exchange.domain.usecase

import pl.com.app.exchange.domain.repository.ExchangeRepository

class GetLastRatesUseCase(private val exchangeRepository: ExchangeRepository) {

	suspend operator fun invoke(table: String, code: String) =
		exchangeRepository.getLastRates(table = table, code = code)
}