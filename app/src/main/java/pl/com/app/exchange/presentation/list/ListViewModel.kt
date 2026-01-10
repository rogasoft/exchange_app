package pl.com.app.exchange.presentation.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pl.com.app.exchange.data.api.DataResult
import pl.com.app.exchange.domain.usecase.GetExchangesUseCase
import javax.inject.Inject

private const val TABLE_A = "a"
private const val TABLE_B = "b"

@HiltViewModel
class ListViewModel @Inject constructor(
	private val getExchangesUseCase: GetExchangesUseCase,

) : ViewModel() {

	init {
		getExchanges()
	}

	private fun getExchanges() {
		viewModelScope.launch {
			getExchangesUseCase("a").collect { result ->
				when (result) {
					is DataResult.Success -> Log.d("ListViewModel", "Success")
					is DataResult.Error -> Log.d("ListViewModel", "Error")
					is DataResult.NetworkError -> Log.d("ListViewModel", "NetworkError")
				}
			}
		}
	}
}