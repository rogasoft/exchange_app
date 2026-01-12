package pl.com.app.exchange.common.extension

import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import org.junit.Test
import java.math.BigDecimal

class NumberExtensionsTest {

	@Test
	fun `given input data when differsByAtLeast10Percent called then returns true value is 10 percent larger`() {
		// GIVEN
		val base = BigDecimal("100.00")
		val other = BigDecimal("110.00")

		// WHEN
		val result = base.differsByAtLeast10Percent(other)

		result.shouldBeTrue()
	}

	@Test
	fun `given input data when differsByAtLeast10Percent called then returns true value is 10 percent smaller`() {
		// GIVEN
		val base = BigDecimal("100.00")
		val other = BigDecimal("90.00")

		// WHEN
		val result = base.differsByAtLeast10Percent(other)

		// THEN
		result.shouldBeTrue()
	}

	@Test
	fun `given input data when differsByAtLeast10Percent called then returns false difference is less than 10 percent`() {
		// GIVEN
		val base = BigDecimal("100.00")
		val other = BigDecimal("109.99")

		// WHEN
		val result = base.differsByAtLeast10Percent(other)

		// THEN
		result.shouldBeFalse()
	}

	@Test
	fun `given input data when differsByAtLeast10Percent called then returns false when values are equal`() {
		// GIVEN
		val base = BigDecimal("100.00")
		val other = BigDecimal("100.00")

		// WHEN
		val result = base.differsByAtLeast10Percent(other)

		// THEN
		result.shouldBeFalse()
	}
}