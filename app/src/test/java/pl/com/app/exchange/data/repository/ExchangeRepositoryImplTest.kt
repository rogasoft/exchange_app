package pl.com.app.exchange.data.repository

import app.cash.turbine.test
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import pl.com.app.exchange.common.NetworkManager
import pl.com.app.exchange.common.date.DateRangeProvider
import pl.com.app.exchange.data.api.DataResult
import pl.com.app.exchange.data.api.ExchangeService
import pl.com.app.exchange.data.dto.details.RateDetailsResponse
import pl.com.app.exchange.domain.mapper.ExchangeMapper
import pl.com.app.exchange.domain.mapper.RateMapper
import pl.com.app.exchange.domain.model.details.RateDetails

class ExchangeRepositoryImplTest {

	private val testDispatcher = UnconfinedTestDispatcher()
	private val networkManager: NetworkManager = mockk(relaxed = true)
	private val exchangeService: ExchangeService = mockk()
	private val exchangeMapper: ExchangeMapper = mockk()
	private val rateMapper: RateMapper = mockk()
	private val dateRangeProvider: DateRangeProvider = mockk()

	private lateinit var repository: ExchangeRepositoryImpl

	@Before
	fun setup() {
		repository = ExchangeRepositoryImpl(
			ioDispatcher = testDispatcher,
			networkManager = networkManager,
			exchangeService = exchangeService,
			exchangeMapper = exchangeMapper,
			rateMapper = rateMapper,
			dateRangeProvider = dateRangeProvider
		)
	}

	@After
	fun tearDown() {
		clearAllMocks()
	}

	@Test
	fun `given input data when getRateDetails called then emits success state when api call succeed`() = runTest {
		// GIVEN
		val table = "a"
		val code = "USD"
		val dtoModel = mockk<RateDetailsResponse>()
		val domainModel = mockk<RateDetails>()

		// WHEN
		coEvery { networkManager.safeApiCall<RateDetailsResponse>(any()) } returns
				DataResult.Success(dtoModel)
		every { rateMapper.mapDtoToDomain(dtoModel) } returns domainModel
		testScheduler.advanceUntilIdle()

		// THEN
		repository.getRateDetails(table, code).test {
			awaitItem() shouldBe DataResult.Success(domainModel)
			awaitComplete()
		}
	}

	@Test
	fun `given input data when getRateDetails called then emits error state when api call error`() = runTest {
		// GIVEN
		val table = "a"
		val code = "USD"

		// WHEN
		coEvery { networkManager.safeApiCall<RateDetailsResponse>(any()) } returns
				DataResult.Error(message = "", code = 500)
		testScheduler.advanceUntilIdle()

		// THEN
		repository.getRateDetails(table, code).test {
			awaitItem() shouldBe DataResult.Error(message = "", code = 500)
			awaitComplete()
		}
	}

	@Test
	fun `given input data when getLastRates called then emits success state when api call succeed`() = runTest {
		// GIVEN
		val table = "a"
		val code = "USD"
		val startDate = "2024-01-01"
		val endDate = "2024-01-14"
		val dtoModel = mockk<RateDetailsResponse>()
		val domainModel = mockk<RateDetails>()

		// WHEN
		coEvery { dateRangeProvider.getCurrentAndTwoWeeksAgo() } returns (startDate to endDate)

		coEvery { networkManager.safeApiCall<RateDetailsResponse>(any()) } returns
				DataResult.Success(dtoModel)

		every { rateMapper.mapDtoToDomain(dtoModel) } returns domainModel
		testScheduler.advanceUntilIdle()

		// THEN
		repository.getLastRates(table, code).test {
			awaitItem() shouldBe DataResult.Success(domainModel)
			awaitComplete()
		}
	}

	@Test
	fun `given input data when getLastRates called then emits error state when api call error`() = runTest {
		// GIVEN
		val table = "a"
		val code = "USD"
		val startDate = "2024-01-01"
		val endDate = "2024-01-14"

		// WHEN
		coEvery { dateRangeProvider.getCurrentAndTwoWeeksAgo() } returns (startDate to endDate)

		coEvery { networkManager.safeApiCall<RateDetailsResponse>(any()) } returns
				DataResult.Error(message = "", code = 500)
		testScheduler.advanceUntilIdle()

		// THEN
		repository.getLastRates(table, code).test {
			awaitItem() shouldBe DataResult.Error(message = "", code = 500)
			awaitComplete()
		}
	}
}