package pl.com.app.exchange.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import pl.com.app.exchange.domain.repository.ExchangeRepository
import pl.com.app.exchange.domain.usecase.GetExchangesUseCase
import pl.com.app.exchange.domain.usecase.GetLastRatesUseCase
import pl.com.app.exchange.domain.usecase.GetRateDetailsUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

	@Provides
	@Singleton
	fun provideGetExchangesUseCase(exchangeRepository: ExchangeRepository): GetExchangesUseCase =
		GetExchangesUseCase(exchangeRepository)

	@Provides
	@Singleton
	fun provideGetRateDetailsUseCase(exchangeRepository: ExchangeRepository): GetRateDetailsUseCase =
		GetRateDetailsUseCase(exchangeRepository)

	@Provides
	@Singleton
	fun provideGetLastRatesUseCase(
		@DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
		exchangeRepository: ExchangeRepository
	): GetLastRatesUseCase =
		GetLastRatesUseCase(defaultDispatcher, exchangeRepository)
}