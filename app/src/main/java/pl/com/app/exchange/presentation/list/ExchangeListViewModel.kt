package pl.com.app.exchange.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.com.app.exchange.data.api.DataResult
import pl.com.app.exchange.domain.usecase.GetExchangesUseCase
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(private val getExchangesUseCase: GetExchangesUseCase, ) : ViewModel() {

	val exchanges = getExchangesUseCase().cachedIn(viewModelScope)
}