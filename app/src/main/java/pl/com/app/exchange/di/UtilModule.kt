package pl.com.app.exchange.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pl.com.app.exchange.common.date.DateProvider
import pl.com.app.exchange.common.date.DateProviderImpl
import pl.com.app.exchange.common.date.DateRangeProvider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UtilModule {

	@Provides
	@Singleton
	fun provideDateProvider(): DateProvider = DateProviderImpl()

	@Provides
	@Singleton
	fun provideDateRangeProvider(dateProvider: DateProvider): DateRangeProvider = DateRangeProvider(dateProvider)
}