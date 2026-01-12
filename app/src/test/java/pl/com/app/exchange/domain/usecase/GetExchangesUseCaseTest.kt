package pl.com.app.exchange.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingData
import app.cash.turbine.test
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertNotNull
import pl.com.app.exchange.domain.model.list.Rate
import pl.com.app.exchange.domain.repository.ExchangeRepository

class GetExchangesUseCaseTest {

	private val repository: ExchangeRepository = mockk()
	private lateinit var useCase: GetExchangesUseCase

	@Before
	fun setup() {
		useCase = GetExchangesUseCase(repository)
	}

	@After
	fun tearDown() {
		clearAllMocks()
	}

	@Test
	fun `given paging data when invoke called then returns flow from repository`() = runTest {
		// GIVEN
		val rate = Rate(
			table = "a",
			currency = "USD",
			code = "USD",
			mid = 4.0
		)
		val pagingData = PagingData.from(listOf(rate))
		val flow = flowOf(pagingData)
		val pager = mockk<Pager<Int, Rate>>()

		// WHEN
		every { pager.flow } returns flow
		every { repository.getExchanges() } returns pager
		val result = useCase.invoke()

		// THEN
		result.test {
			val item = awaitItem()
			assertNotNull(item)
			awaitComplete()
		}
		verify(exactly = 1) { repository.getExchanges() }
	}

	@Test
	fun `given paging data when invoke called then emits flow`() = runTest {
		// GIVEN
		val pagingData = PagingData.empty<Rate>()
		val flow = flowOf(pagingData)
		val pager = mockk<Pager<Int, Rate>>()

		// WHEN
		every { pager.flow } returns flow
		every { repository.getExchanges() } returns pager

		// THEN
		useCase().test {
			awaitItem().shouldBeInstanceOf<PagingData<Rate>>()
			awaitComplete()
		}
	}
}