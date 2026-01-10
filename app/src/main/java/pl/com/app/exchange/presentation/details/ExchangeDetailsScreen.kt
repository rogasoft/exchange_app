package pl.com.app.exchange.presentation.details

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import pl.com.app.exchange.R
import pl.com.app.exchange.domain.model.details.RateItem
import pl.com.app.exchange.presentation.navigation.Route
import pl.com.app.exchange.ui.component.ErrorDialog
import pl.com.app.exchange.ui.component.LoadingOverlay
import pl.com.app.exchange.ui.theme.ExchangeTheme

fun NavGraphBuilder.exchangeDetails() {
	composable<Route.ExchangeDetails> {
		val viewModel: ExchangeDetailsViewModel = hiltViewModel()
		val uiState = viewModel.uiState.collectAsState()

		when {
			uiState.value.isLoading -> LoadingOverlay()
			uiState.value.isError -> ErrorDialog(positiveAction = viewModel::retry)
			else -> ExchangeDetailsScreen(uiState = uiState.value)
		}
	}
}

@Composable
private fun ExchangeDetailsScreen(
	uiState: ExchangeDetailsUiState
) {
	Column {
		Header(uiState = uiState)

		Spacer(modifier = Modifier.height(8.dp))

		List(rates = uiState.lastRates)
	}
}

@Composable
private fun Header(uiState: ExchangeDetailsUiState) {
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.padding(16.dp)
	) {
		Text(
			text = uiState.rateDetails?.currency.orEmpty(),
			fontSize = 18.sp,
			color = Color.Black
		)

		Spacer(modifier = Modifier.height(8.dp))

		Text(
			text = uiState.rateDetails?.code.orEmpty(),
			fontSize = 18.sp,
			color = Color.Black
		)

		Spacer(modifier = Modifier.height(8.dp))

		Text(
			text = stringResource(R.string.currency, uiState.mid),
			fontSize = 22.sp,
			color = Color.Black
		)
	}
}

@Composable
private fun List(rates: List<RateItem>) {
	rates.let { rates ->
		LazyColumn(
			modifier = Modifier
				.fillMaxSize()
				.padding(8.dp)
		) {
			items(rates) { rate ->
				RateItem(rate = rate,)

				Spacer(modifier = Modifier.height(4.dp))
			}
		}
	}
}

@Composable
private fun RateItem(rate: RateItem) {
	val shape = RoundedCornerShape(8.dp)
	val background = if (rate.isMoreThanTenPercent) Color.Red else Color.Gray

	Column (
		modifier = Modifier
			.fillMaxWidth()
			.background(
				color = background,
				shape = shape
			)
			.clip(shape)
			.border(width = 1.dp, color = Color.Black, shape = shape)
			.padding(vertical = 4.dp, horizontal = 8.dp)
			.height(IntrinsicSize.Min)
	) {
		Text(
			text = rate.effectiveDate,
			fontSize = 18.sp,
			color = Color.Black
		)

		Spacer(modifier = Modifier.height(8.dp))

		Text(
			text = stringResource(R.string.currency, rate.mid),
			fontSize = 22.sp,
			color = Color.Black
		)
	}
}

@Composable
@Preview
private fun Preview() {
	ExchangeTheme {
		ExchangeDetailsScreen(uiState = ExchangeDetailsUiState())
	}
}