package pl.com.app.exchange.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.com.app.exchange.data.api.DataResult
import pl.com.app.exchange.di.DefaultDispatcher
import pl.com.app.exchange.di.IoDispatcher
import pl.com.app.exchange.domain.model.details.RateItem
import pl.com.app.exchange.domain.usecase.GetLastRatesUseCase
import pl.com.app.exchange.domain.usecase.GetRateDetailsUseCase
import pl.com.app.exchange.presentation.list.ExchangeListUiState
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class ExchangeDetailsViewModel @Inject constructor(
	private val getRateDetailsUseCase: GetRateDetailsUseCase,
	private val getLastRatesUseCase: GetLastRatesUseCase,
	savedStateHandle: SavedStateHandle
) : ViewModel() {

	private val _uiState = MutableStateFlow(ExchangeDetailsUiState())
	val uiState = _uiState.asStateFlow()

	private val table = savedStateHandle.get<String>("table").orEmpty()
	private val code = savedStateHandle.get<String>("code").orEmpty()
	private val mid = savedStateHandle.get<Double>("mid") ?: 0.0

	init {
		getRateDetails()
	}

	fun retry() {
		getRateDetails()
	}

	private fun getRateDetails() {
		viewModelScope.launch {
			getRateDetailsUseCase(table = table, code = code).collect { result ->
				when(result) {
					is DataResult.Success -> {
						updateState {
							_uiState.value.copy(
								isLoading = false,
								isError = false,
								mid = mid,
								rateDetails = result.data
							)
						}
						getLastRates()
					}
					is DataResult.Error,
					DataResult.NetworkError -> updateState { _uiState.value.copy(isLoading = false, isError = true) }
				}
			}
		}
	}

	private fun getLastRates() {
		viewModelScope.launch {
			getLastRatesUseCase(table = table, code = code, currentMid = BigDecimal(mid)).collect { result ->
				when(result) {
					is DataResult.Success -> updateState {
						_uiState.value.copy(lastRates = result.data.rates.sortedByDescending { it.effectiveDate })
					}
					is DataResult.Error,
					DataResult.NetworkError -> updateState { _uiState.value.copy(isLoading = false, isError = true) }
				}
			}
		}
	}

	private fun updateState(state: (ExchangeDetailsUiState) -> ExchangeDetailsUiState) {
		_uiState.update(state)
	}
}