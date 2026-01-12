package pl.com.app.exchange.domain.mapper

import io.kotest.matchers.shouldBe
import org.junit.Test
import pl.com.app.exchange.data.dto.list.ExchangeResponse
import pl.com.app.exchange.data.dto.list.RateResponse
import pl.com.app.exchange.domain.model.list.Rate

class ExchangeMapperTest {

	private val mapper = ExchangeMapper()

	@Test
	fun `given dto data when mapRateDtoToDomain called then maps rates correctly`() {
		// GIVEN
		val dto = ExchangeResponse(
			table = "a",
			effectiveDate = "2026-01-01",
			rates = listOf(
				RateResponse(currency = "US Dollar", code = "USD", mid = 4.0),
				RateResponse(currency = "Euro", code = "EUR", mid = 4.5)
			)
		)

		// WHEN
		val result = mapper.mapRateDtoToDomain(listOf(dto))

		// THEN
		result shouldBe listOf(
			Rate(table = "a", currency = "US Dollar", code = "USD", mid = 4.0),
			Rate(table = "a", currency = "Euro", code = "EUR", mid = 4.5)
		)
	}

	@Test
	fun `given dto data when mapRateDtoToDomain called then uses first exchange with rates`() {
		// GIVEN
		val emptyExchange = ExchangeResponse(
			table = "a",
			effectiveDate = "2026-01-01",
			rates = emptyList()
		)

		val validExchange = ExchangeResponse(
			table = "b",
			effectiveDate = "2026-01-01",
			rates = listOf(
				RateResponse(currency = "US Dollar", code = "USD", mid = 4.0)
			)
		)

		// WHEN
		val result = mapper.mapRateDtoToDomain(listOf(emptyExchange, validExchange))

		// THEN
		result shouldBe listOf(
			Rate(table = "b", currency = "US Dollar", code = "USD", mid = 4.0)
		)
	}
}