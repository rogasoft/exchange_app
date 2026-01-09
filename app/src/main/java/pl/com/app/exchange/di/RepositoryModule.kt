package pl.com.app.exchange.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import pl.com.app.exchange.common.NetworkManager
import pl.com.app.exchange.common.date.DateRangeProvider
import pl.com.app.exchange.data.api.ExchangeService
import pl.com.app.exchange.data.repository.ExchangeRepositoryImpl
import pl.com.app.exchange.domain.mapper.ExchangeMapper
import pl.com.app.exchange.domain.mapper.RateMapper
import pl.com.app.exchange.domain.repository.ExchangeRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

	@Provides
	@Singleton
	fun provideExchangeRepository(
		@IoDispatcher ioDispatcher: CoroutineDispatcher,
		networkManager: NetworkManager,
		exchangeService: ExchangeService,
		exchangeMapper: ExchangeMapper,
		rateMapper: RateMapper,
		dateRangeProvider: DateRangeProvider
	): ExchangeRepository =
		ExchangeRepositoryImpl(
			ioDispatcher,
			networkManager,
			exchangeService,
			exchangeMapper,
			rateMapper,
			dateRangeProvider
		)
}