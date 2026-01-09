package pl.com.app.exchange.domain.usecase

import pl.com.app.exchange.domain.repository.ExchangeRepository

class GetExchangesUseCase(private val exchangeRepository: ExchangeRepository) {

	suspend operator fun invoke(table: String) = exchangeRepository.getExchanges(table = table)
}