package pl.com.app.exchange.data.api

sealed class DataResult<out T> {

	data class Success<T>(val data: T, val code: Int) : DataResult<T>()

	data class Error(val message: String, val code: Int? = null) : DataResult<Nothing>()

	object NetworkError : DataResult<Nothing>()
}
