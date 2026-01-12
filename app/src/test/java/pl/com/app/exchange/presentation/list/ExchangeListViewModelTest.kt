package pl.com.app.exchange.presentation.list

import androidx.paging.PagingData
import app.cash.turbine.test
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import pl.com.app.exchange.domain.model.list.Rate
import pl.com.app.exchange.domain.usecase.GetExchangesUseCase

class ExchangeListViewModelTest {

	private val getExchangesUseCase: GetExchangesUseCase = mockk(relaxed = true)

	private lateinit var viewModel: ListViewModel

	@Before
	fun setup() {
		viewModel = ListViewModel(getExchangesUseCase)
	}

	@After
	fun tearDown() {
		clearAllMocks()
	}

	@Test
	fun `given paging data when use case called then exchanges emits PagingData`() = runTest {
		// GIVEN
		val pagingData: PagingData<Rate> = mockk()
		val flow = flowOf(pagingData)

		// WHEN
		every { getExchangesUseCase() } returns flow
		val result = getExchangesUseCase()

		// THEN
		result.test {
			awaitItem() shouldBe pagingData
			awaitComplete()
		}
	}

	@Test
	fun `given paging data exception when use case called then exchanges emits error`() = runTest {
		// GIVEN
		val exception = Exception("")

		// WHEN
		every { getExchangesUseCase() } returns flow { throw exception }
		val result = getExchangesUseCase()

		// THEN
		result.test {
			awaitError() shouldBe exception
		}
	}
}