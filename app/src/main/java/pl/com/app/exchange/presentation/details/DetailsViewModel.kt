package pl.com.app.exchange.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.com.app.exchange.data.api.DataResult
import pl.com.app.exchange.domain.usecase.GetLastRatesUseCase
import pl.com.app.exchange.domain.usecase.GetRateDetailsUseCase
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
	private val getRateDetailsUseCase: GetRateDetailsUseCase,
	private val getLastRatesUseCase: GetLastRatesUseCase,
) : ViewModel() {

	private val _uiState = MutableStateFlow(DetailsUiState())
	val uiState = _uiState.asStateFlow()

	init {
		getRateDetails()
		getLastRates()
	}

	private fun getRateDetails() {
		viewModelScope.launch {
			getRateDetailsUseCase(table = "a", code = "usd").collect { result ->
				if (result is DataResult.Success) {
					_uiState.update { current -> current.copy(rateDetails = result.data) }
				}
			}
		}
	}

	private fun getLastRates() {
		viewModelScope.launch {
			getLastRatesUseCase(table = "a", code = "usd").collect { result ->
				if (result is DataResult.Success) {
					_uiState.update { current -> current.copy(lastRates = result.data) }
				}
			}
		}
	}
}