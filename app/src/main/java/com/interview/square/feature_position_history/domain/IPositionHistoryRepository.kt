package com.interview.square.feature_position_history.domain

import com.interview.square.core.domain.model.PositionRecord

interface IPositionHistoryRepository {
    fun persistRecord(record: PositionRecord)
    fun getRecord(id: String): PositionRecord?
}