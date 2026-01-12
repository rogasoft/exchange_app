package pl.com.app.exchange.domain.usecase

import app.cash.turbine.test
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import pl.com.app.exchange.data.api.DataResult
import pl.com.app.exchange.domain.model.details.RateDetails
import pl.com.app.exchange.domain.repository.ExchangeRepository

class GetRateDetailsUseCaseTest {

	private val repository: ExchangeRepository = mockk()
	private lateinit var useCase: GetRateDetailsUseCase

	@Before
	fun setup() {
		useCase = GetRateDetailsUseCase(repository)
	}

	@After
	fun tearDown() {
		clearAllMocks()
	}

	@Test
	fun `given rate data when invoke called then emits success`() = runTest {
		// GIVEN
		val table = "a"
		val code = "USD"
		val rateDetails = mockk<RateDetails>()
		val flow = flowOf(DataResult.Success(rateDetails))

		// WHEN
		coEvery { repository.getRateDetails(table, code) } returns flow
		val resultFlow = useCase(table, code)

		// THEN
		resultFlow.test {
			awaitItem() shouldBe DataResult.Success(rateDetails)
			awaitComplete()
		}

		coVerify(exactly = 1) {
			repository.getRateDetails(table, code)
		}
	}

	@Test
	fun `given rate data when invoke called then emits error`() = runTest {
		// GIVEN
		val table = "a"
		val code = "USD"
		val error = DataResult.Error(message = "error", code = 500)
		val flow = flowOf(error)

		// WHEN
		coEvery {
			repository.getRateDetails(table, code)
		} returns flow
		val resultFlow = useCase(table, code)

		// THEN
		resultFlow.test {
			awaitItem() shouldBe error
			awaitComplete()
		}
	}

	@Test
	fun `given rate data when invoke called then emits network error`() = runTest {
		// GIVEN
		val table = "a"
		val code = "USD"
		val flow = flowOf(DataResult.NetworkError)

		// WHEN
		coEvery {
			repository.getRateDetails(table, code)
		} returns flow
		val resultFlow = useCase(table, code)

		// THEN
		resultFlow.test {
			awaitItem() shouldBe DataResult.NetworkError
			awaitComplete()
		}
	}
}