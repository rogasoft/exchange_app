package pl.com.app.exchange.domain.usecase

import pl.com.app.exchange.domain.repository.ExchangeRepository

class GetRateDetailsUseCase(private val exchangeRepository: ExchangeRepository) {

	suspend operator fun invoke(table: String, code: String) =
		exchangeRepository.getRateDetails(table = table, code = code)
}