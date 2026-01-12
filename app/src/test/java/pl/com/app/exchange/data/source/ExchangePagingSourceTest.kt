package pl.com.app.exchange.data.source

import androidx.paging.PagingSource
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import pl.com.app.exchange.common.NetworkManager
import pl.com.app.exchange.data.api.DataResult
import pl.com.app.exchange.data.api.ExchangeService
import pl.com.app.exchange.data.dto.list.ExchangeResponse
import pl.com.app.exchange.data.dto.list.RateResponse
import pl.com.app.exchange.domain.mapper.ExchangeMapper
import pl.com.app.exchange.domain.model.list.Rate

class ExchangePagingSourceTest {

	private val networkManager: NetworkManager = mockk(relaxed = true)
	private val exchangeService: ExchangeService = mockk()
	private val exchangeMapper: ExchangeMapper = mockk()

	private lateinit var source: ExchangePagingSource

	@Before
	fun setup() {
		source = ExchangePagingSource(
			networkManager,
			exchangeService,
			exchangeMapper,
			tables = listOf("a", "b")
		)
	}

	@After
	fun tearDown() {
		clearAllMocks()
	}

	@Test
	fun `given response when load returns first page then api succeed`() = runTest {
		// GIVEN
		val dto = ExchangeResponse(
			table = "a",
			effectiveDate = "2026-01-01",
			rates = listOf(RateResponse(currency = "USD", code = "US Dollar", mid = 4.0))
		)

		val domainRate = Rate(table = "a", currency = "USD", code = "US Dollar", mid = 4.0)

		// WHEN
		coEvery { networkManager.safeApiCall<List<ExchangeResponse>>(any()) } returns
			DataResult.Success(listOf(dto))
		every { exchangeMapper.mapRateDtoToDomain(listOf(dto)) } returns listOf(domainRate)
		val result = source.load(
			PagingSource.LoadParams.Refresh(
				key = null,
				loadSize = 100,
				placeholdersEnabled = false
			)
		)

		// THEN
		result shouldBe PagingSource.LoadResult.Page(
			data = listOf(domainRate),
			prevKey = null,
			nextKey = 1
		)
	}

	@Test
	fun `given response when load returns second page then api succeed`() = runTest {
		// GIVEN
		val dto = ExchangeResponse(
			table = "b",
			effectiveDate = "2026-01-01",
			rates = listOf(RateResponse(currency = "USD", code = "US Dollar", mid = 4.0))
		)

		val domainRate = Rate(table = "b", currency = "USD", code = "US Dollar", mid = 4.0)

		// WHEN
		coEvery { networkManager.safeApiCall<List<ExchangeResponse>>(any()) } returns
				DataResult.Success(listOf(dto))
		every { exchangeMapper.mapRateDtoToDomain(listOf(dto)) } returns listOf(domainRate)
		val result = source.load(
			PagingSource.LoadParams.Refresh(
				key = 1,
				loadSize = 100,
				placeholdersEnabled = false
			)
		)

		// THEN
		result shouldBe PagingSource.LoadResult.Page(
			data = listOf(domainRate),
			prevKey = 0,
			nextKey = null
		)
	}

	@Test
	fun `when api returns error then load returns error`() = runTest {
		// WHEN
		coEvery { networkManager.safeApiCall<ExchangeResponse>(any()) } returns
			DataResult.Error(message = "error", code = 500)

		val result = source.load(
			PagingSource.LoadParams.Refresh(
				key = null,
				loadSize = 100,
				placeholdersEnabled = false
			)
		)

		// THEN
		result.shouldBeInstanceOf<PagingSource.LoadResult.Error<Int, Rate>>()
	}

	@Test
	fun `when api returns network error then load returns error`() = runTest {
		// WHEN
		coEvery { networkManager.safeApiCall<ExchangeResponse>(any()) } returns
			DataResult.NetworkError

		val result = source.load(
			PagingSource.LoadParams.Refresh(
				key = null,
				loadSize = 100,
				placeholdersEnabled = false
			)
		)

		// THEN
		result.shouldBeInstanceOf<PagingSource.LoadResult.Error<Int, Rate>>()
	}
}