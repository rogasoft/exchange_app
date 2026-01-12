package pl.com.app.exchange.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pl.com.app.exchange.domain.mapper.ExchangeMapper
import pl.com.app.exchange.domain.mapper.RateMapper
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MapperModule {

	@Provides
	@Singleton
	fun provideExchangeMapper(): ExchangeMapper = ExchangeMapper()

	@Provides
	@Singleton
	fun provideRateMapper(): RateMapper = RateMapper()
}