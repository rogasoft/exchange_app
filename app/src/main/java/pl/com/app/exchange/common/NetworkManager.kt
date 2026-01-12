package pl.com.app.exchange.common

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import okio.IOException
import pl.com.app.exchange.data.api.DataResult
import retrofit2.Response

class NetworkManager(val connectivityManager: ConnectivityManager) {

	val isCurrentlyOnline: Boolean
		get() = connectivityManager.let { manager ->
			manager.activeNetwork
				.let(manager::getNetworkCapabilities)
				?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) == true
		}

	suspend fun <T> safeApiCall(
		apiCall: suspend () -> Response<T>
	): DataResult<T> = runCatching { apiCall() }.fold(
		onSuccess = { response ->
			if (response.isSuccessful) {
				response.body()?.let { DataResult.Success(data = it)
				} ?: DataResult.Error(message = "Empty body", code = response.code())
			} else {
				DataResult.Error(
					message = response.errorBody()?.string() ?: "Unknown error",
					code = response.code()
				)
			}
		},
		onFailure = { throwable ->
			when {
				throwable is IOException || isCurrentlyOnline -> DataResult.NetworkError
				else -> DataResult.Error(message = throwable.message ?: "Unknown exception")
			}
		}
	)
}