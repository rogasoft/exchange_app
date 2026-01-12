package pl.com.app.exchange.presentation.details

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Rule
import org.junit.Test
import pl.com.app.exchange.MainDispatcherRule
import pl.com.app.exchange.data.api.DataResult
import pl.com.app.exchange.domain.model.details.RateDetails
import pl.com.app.exchange.domain.model.details.RateItem
import pl.com.app.exchange.domain.usecase.GetLastRatesUseCase
import pl.com.app.exchange.domain.usecase.GetRateDetailsUseCase

class ExchangeDetailsViewModelTest {

	@get:Rule
	val mainDispatcherRule = MainDispatcherRule()

	private val getRateDetailsUseCase: GetRateDetailsUseCase = mockk()
	private val getLastRatesUseCase: GetLastRatesUseCase = mockk()
	private lateinit var savedStateHandle: SavedStateHandle

	private lateinit var viewModel: ExchangeDetailsViewModel

	@After
	fun tearDown() {
		clearAllMocks()
	}

	@Test
	fun `given argument data when rate details and last rates succeed then uiState is updated correctly`() = runTest {
		// GIVEN
		val table = "a"
		val code = "USD"
		val mid = 4.0
		val rateDetails = RateDetails(
			table = "a",
			currency = "USD",
			code = "USD",
			rates = emptyList()
		)
		val lastRates = listOf(
			RateItem("2024-01-02", 4.1, true),
			RateItem("2024-01-01", 3.9, false)
		)

		// WHEN
		coEvery { getRateDetailsUseCase(table, code) } returns flowOf(DataResult.Success(rateDetails))

		coEvery { getLastRatesUseCase(table, code, mid) } returns flowOf(
			DataResult.Success(rateDetails.copy(rates = lastRates))
		)
		savedStateHandle = SavedStateHandle(
			mapOf(
				"table" to "a",
				"code" to "USD",
				"mid" to 4.0
			)
		)
		viewModel = ExchangeDetailsViewModel(getRateDetailsUseCase, getLastRatesUseCase, savedStateHandle)

		// THEN
		viewModel.uiState.test {
			awaitItem().lastRates shouldBe lastRates.sortedByDescending { it.effectiveDate }
		}
	}

	@Test
	fun `given argument data when rate details fails then uiState is error`() = runTest {
		// GIVEN
		val table = "a"
		val code = "USD"
		val mid = 4.0

		// WHEN
		coEvery { getRateDetailsUseCase(table, code) } returns flowOf(DataResult.Error(message = ""))
		savedStateHandle = SavedStateHandle(
			mapOf(
				"table" to table,
				"code" to code,
				"mid" to mid
			)
		)

		viewModel = ExchangeDetailsViewModel(
			getRateDetailsUseCase,
			getLastRatesUseCase,
			savedStateHandle
		)

		// THEN
		viewModel.uiState.test {
			with(awaitItem()) {
				isLoading shouldBe false
				isError shouldBe true
				lastRates shouldBe emptyList()
			}
			cancelAndIgnoreRemainingEvents()
		}
	}
}