package pl.com.app.exchange.domain.usecase

import app.cash.turbine.test
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import pl.com.app.exchange.data.api.DataResult
import pl.com.app.exchange.domain.model.details.RateDetails
import pl.com.app.exchange.domain.model.details.RateItem
import pl.com.app.exchange.domain.repository.ExchangeRepository

class GetLastRatesUseCaseTest {

	private val testDispatcher = UnconfinedTestDispatcher()
	private val repository: ExchangeRepository = mockk()
	private lateinit var useCase: GetLastRatesUseCase

	@Before
	fun setup() {
		useCase = GetLastRatesUseCase(testDispatcher, repository)
	}

	@After
	fun tearDown() {
		clearAllMocks()
	}

	@Test
	fun `given rates data when invoke called then maps rates and sets isMoreThanTenPercent correctly`() = runTest {
		// GIVEN
		val table = "a"
		val code = "USD"
		val currency = "USD"
		val mid = 4.0
		val rates = listOf(
			RateItem(effectiveDate = "2026-01-01", mid = 4.5, isMoreThanTenPercent = false),
			RateItem(effectiveDate = "2026-01-01", mid = 3.95, isMoreThanTenPercent = false)
		)
		val rateDetails = RateDetails(table = table, currency = currency, code = code, rates = rates)

		// WHEN
		coEvery { repository.getLastRates(table = table, code = code) } returns
			flowOf(DataResult.Success(rateDetails))
		val resultFlow = useCase(table = table, code = code, currentMid = mid)
		testScheduler.advanceUntilIdle()

		// THEN
		resultFlow.test {
			val item = awaitItem() as DataResult.Success
			item.data.rates[0].isMoreThanTenPercent shouldBe true
			item.data.rates[1].isMoreThanTenPercent shouldBe false
			awaitComplete()
		}
	}

	@Test
	fun `given error when invoke called then returns error`() = runTest {
		// GIVEN
		val error = DataResult.Error(message = "error", code = 500)

		// WHEN
		coEvery { repository.getLastRates(any(), any()) } returns flowOf(error)
		val resultFlow = useCase(table = "a", code = "USD", currentMid = 4.0)

		// THEN
		resultFlow.test {
			awaitItem() shouldBe error
			awaitComplete()
		}
	}

	@Test
	fun `when invoke called then returns network error`() = runTest {
		// WHEN
		coEvery { repository.getLastRates(any(), any()) } returns flowOf(DataResult.NetworkError)
		val resultFlow = useCase(table = "a", code = "USD", currentMid = 4.0)

		// THEN
		resultFlow.test {
			awaitItem() shouldBe DataResult.NetworkError
			awaitComplete()
		}
	}
}