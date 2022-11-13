package com.interview.square.di

import com.interview.square.feature_position_history.data.PositionHistoryRepository
import com.interview.square.feature_position_history.domain.IPositionHistoryRepository
import com.interview.square.feature_square_move.ui.SquareViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single<IPositionHistoryRepository> { PositionHistoryRepository() }
    viewModelOf(::SquareViewModel)
}