package pl.com.app.exchange.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import pl.com.app.exchange.common.NetworkManager
import pl.com.app.exchange.data.api.DataResult
import pl.com.app.exchange.data.api.ExchangeService
import pl.com.app.exchange.domain.mapper.ExchangeMapper
import pl.com.app.exchange.domain.model.list.Rate
import java.lang.Exception

class ExchangePagingSource(
	private val networkManager: NetworkManager,
	private val exchangeService: ExchangeService,
	private val exchangeMapper: ExchangeMapper,
	private val tables: List<String>
) : PagingSource<Int, Rate>() {

	override fun getRefreshKey(state: PagingState<Int, Rate>): Int = 0

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Rate> {
		val index = params.key ?: 0
		return when (
			val result = networkManager.safeApiCall {
				exchangeService.getExchanges(table = tables[index])
			}
		) {
			is DataResult.Success -> {
				LoadResult.Page(
					data = exchangeMapper.mapRateDtoToDomain(result.data),
					prevKey = if (index == 0) null else index - 1,
					nextKey = if (index + 1 < tables.size) index + 1 else null
				)
			}

			is DataResult.NetworkError ->
				LoadResult.Error(Exception())

			is DataResult.Error ->
				LoadResult.Error(Exception())
		}
	}


}