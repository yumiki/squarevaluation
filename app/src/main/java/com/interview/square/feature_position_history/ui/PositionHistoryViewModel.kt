package com.interview.square.feature_position_history.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.interview.square.core.domain.model.PositionHistory
import com.interview.square.feature_position_history.domain.IPositionHistoryRepository
import com.interview.square.screens.NAV_ARGS_HISTORY_ID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PositionHistoryViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: IPositionHistoryRepository
): IPositionHistoryViewModel, ViewModel() {
    private val _history = MutableStateFlow(emptyList<PositionHistory>())
    override val history: StateFlow<List<PositionHistory>> = _history.asStateFlow()

    init {
        savedStateHandle.get<String>(NAV_ARGS_HISTORY_ID)?.let { repository.getRecord(it) }?.run {
            _history.value = positions
        }
    }

}

interface IPositionHistoryViewModel {
    val history: StateFlow<List<PositionHistory>>
}