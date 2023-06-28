package com.example.algorithmvisualisation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
) : ViewModel() {
    private val _state = MutableStateFlow(HomeContract.State.Empty)
    val state = _state.asStateFlow()

    init {
        updateList(_state.value.filter)
    }

    private fun updateList(filter: String) {
        _state.update { it.copy(filter = filter) }

    }

    fun onFilterChange(value: String) {
        updateList(value)
    }
}
