package pl.com.app.exchange.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.com.app.exchange.domain.usecase.GetExchangesUseCase
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(getExchangesUseCase: GetExchangesUseCase, ) : ViewModel() {

	val exchanges = getExchangesUseCase().cachedIn(viewModelScope)
}