package pl.com.app.exchange.domain.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import pl.com.app.exchange.domain.model.list.Rate
import pl.com.app.exchange.domain.repository.ExchangeRepository

class GetExchangesUseCase(private val exchangeRepository: ExchangeRepository) {

	operator fun invoke(): Flow<PagingData<Rate>> = exchangeRepository.getExchanges().flow
}