package pl.com.app.exchange.common.extension

import java.math.BigDecimal

private const val TEN_PERCENTAGE = "0.1"

fun BigDecimal.differsByAtLeast10Percent(other: BigDecimal): Boolean {
	if (this == BigDecimal.ZERO) return other != BigDecimal.ZERO

	val threshold = this.multiply(BigDecimal(TEN_PERCENTAGE))
	return this.subtract(other).abs() >= threshold
}