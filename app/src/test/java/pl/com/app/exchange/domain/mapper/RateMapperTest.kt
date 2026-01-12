package pl.com.app.exchange.domain.mapper

import io.kotest.matchers.shouldBe
import org.junit.Test
import pl.com.app.exchange.data.dto.details.RateDetailsResponse
import pl.com.app.exchange.data.dto.details.RateItemResponse
import pl.com.app.exchange.domain.model.details.RateDetails
import pl.com.app.exchange.domain.model.details.RateItem

class RateMapperTest {

	private val mapper = RateMapper()

	@Test
	fun `given dto data when mapDtoToDomain called then maps RateDetailsResponse to RateDetails correctly`() {
		// GIVEN
		val dto = RateDetailsResponse(
			table = "A",
			currency = "USD",
			code = "USD",
			rates = listOf(
				RateItemResponse(effectiveDate = "2026-01-01", mid = 4.0),
				RateItemResponse(effectiveDate = "2026-01-02", mid = 4.1)
			)
		)

		// WHEN
		val result = mapper.mapDtoToDomain(dto)

		// THEN
		result shouldBe RateDetails(
			table = "A",
			currency = "USD",
			code = "USD",
			rates = listOf(
				RateItem(effectiveDate = "2026-01-01", mid = 4.0),
				RateItem(effectiveDate = "2026-01-02", mid = 4.1)
			)
		)
	}

	@Test
	fun `given dto data when mapDtoToDomain called then maps empty rates list`() {
		// GIVEN
		val dto = RateDetailsResponse(
			table = "A",
			currency = "USD",
			code = "USD",
			rates = emptyList()
		)

		// WHEN
		val result = mapper.mapDtoToDomain(dto)

		// THEN
		result shouldBe RateDetails(
			table = "A",
			currency = "USD",
			code = "USD",
			rates = emptyList()
		)
	}
}