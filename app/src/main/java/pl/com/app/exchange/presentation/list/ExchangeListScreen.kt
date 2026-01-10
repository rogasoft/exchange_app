package pl.com.app.exchange.presentation.list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import pl.com.app.exchange.R
import pl.com.app.exchange.domain.model.list.Rate
import pl.com.app.exchange.presentation.navigation.Route
import pl.com.app.exchange.ui.component.ErrorDialog
import pl.com.app.exchange.ui.component.LoadingOverlay

fun NavGraphBuilder.exchangeList(clickItemAction: (table: String, code: String, mid: Double) -> Unit) {
	composable<Route.ExchangeList> {
		val viewModel: ListViewModel = hiltViewModel()
		val rates = viewModel.exchanges.collectAsLazyPagingItems()

		when (rates.loadState.refresh) {
			is LoadState.Loading -> LoadingOverlay()
			is LoadState.Error -> ErrorDialog(positiveAction = rates::retry)
			else -> ExchangeListScreen(
				rates = rates,
				clickItemAction = clickItemAction
			)
		}
	}
}

@Composable
private fun ExchangeListScreen(
	rates: LazyPagingItems<Rate>,
	clickItemAction: (table: String, code: String, mid: Double) -> Unit
) {
	LazyColumn(
		modifier = Modifier
			.fillMaxSize()
			.padding(8.dp)
	) {
		items(
			count = rates.itemCount,
			key = { index -> rates[index]?.code ?: index }
		) { index ->
			rates[index]?.let {
				RateItem(
					rate = it,
					clickAction = { clickItemAction(it.table, it.code, it.mid) }
				)

				Spacer(modifier = Modifier.height(4.dp))
			}
		}

		if (rates.loadState.append is LoadState.Loading) item { BottomLoading() }
	}
}

@Composable
private fun RateItem(
	rate: Rate,
	clickAction: () -> Unit
) {
	val shape = RoundedCornerShape(8.dp)

	Column (
		modifier = Modifier
			.fillMaxWidth()
			.background(
				color = Color.Gray,
				shape = shape
			)
			.clip(shape)
			.border(width = 1.dp, color = Color.Black, shape = shape)
			.clickable { clickAction() }
			.padding(vertical = 4.dp, horizontal = 8.dp)
			.height(IntrinsicSize.Min)
	) {
		Text(
			text = rate.currency,
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
private fun BottomLoading() {
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.padding(16.dp),
		contentAlignment = Alignment.Center
	) {
		CircularProgressIndicator()
	}
}

@Composable
@Preview
private fun Preview() {
//	ExchangeTheme {
//		ExchangeListScreen(
//			uiState = ExchangeListUiState(),
//			clickItemAction = { _, _, _ -> }
//		)
//	}
}