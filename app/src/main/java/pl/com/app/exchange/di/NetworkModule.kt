package pl.com.app.exchange.di

import android.content.Context
import android.net.ConnectivityManager
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import pl.com.app.exchange.BuildConfig
import pl.com.app.exchange.common.NetworkManager
import pl.com.app.exchange.data.api.ExchangeService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

	@Provides
	fun provideGson() = Gson()

	@Provides
	fun provideHttpLoggingInterceptor() = HttpLoggingInterceptor()
		.apply { level = if (BuildConfig.DEBUG) Level.BODY else Level.NONE }

	@Provides
	@Singleton
	fun provideAuthOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient = OkHttpClient.Builder()
		.addNetworkInterceptor(httpLoggingInterceptor)
		.build()

	@Provides
	@Singleton
	fun provideAuthRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit =
		Retrofit.Builder()
			.baseUrl(BuildConfig.API_URL)
			.client(okHttpClient)
			.addConverterFactory(GsonConverterFactory.create(gson))
			.build()

	@Provides
	@Singleton
	fun provideExchangeService(retrofit: Retrofit): ExchangeService = retrofit.create(ExchangeService::class.java)

	@Provides
	@Singleton
	fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager =
		context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

	@Provides
	@Singleton
	fun provideNetworkManager(connectivityManager: ConnectivityManager): NetworkManager =
		NetworkManager(connectivityManager)
}