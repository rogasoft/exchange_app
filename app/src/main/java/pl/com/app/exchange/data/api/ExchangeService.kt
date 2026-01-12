package pl.com.app.exchange.data.api

import pl.com.app.exchange.data.dto.details.RateDetailsResponse
import pl.com.app.exchange.data.dto.list.ExchangeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val JSON_POSTFIX = "json"
private const val TABLE_PATH = "table"
private const val CODE_PATH = "code"
private const val START_DATE_PATH = "startDate"
private const val END_DATE_PATH = "endDate"
private const val FORMAT_QUERY = "format"
private const val TABLES = "tables"
private const val RATES = "rates"

interface ExchangeService {

	@GET("$TABLES/{$TABLE_PATH}")
	suspend fun getExchanges(
		@Path(TABLE_PATH) table: String,
		@Query(FORMAT_QUERY) format: String = JSON_POSTFIX
	): Response<List<ExchangeResponse>>

	@GET("$RATES/{$TABLE_PATH}/{$CODE_PATH}")
	suspend fun getRateDetails(
		@Path(TABLE_PATH) table: String,
		@Path(CODE_PATH) code: String,
		@Query(FORMAT_QUERY) format: String = JSON_POSTFIX
	): Response<RateDetailsResponse>

	@GET("$RATES/{$TABLE_PATH}/{$CODE_PATH}/{$START_DATE_PATH}/{$END_DATE_PATH}")
	suspend fun getLastRates(
		@Path(TABLE_PATH) table: String,
		@Path(CODE_PATH) code: String,
		@Path(START_DATE_PATH) startDate: String,
		@Path(END_DATE_PATH) endDate: String,
		@Query(FORMAT_QUERY) format: String = JSON_POSTFIX
	): Response<RateDetailsResponse>
}