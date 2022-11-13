package com.interview.square.feature_position_history.data

import com.interview.square.core.domain.model.PositionRecord
import com.interview.square.feature_position_history.domain.IPositionHistoryRepository

class PositionHistoryRepository: IPositionHistoryRepository {

    private val records = mutableMapOf<String, PositionRecord>()

    override fun persistRecord(record: PositionRecord) {
        records[record.id] = record
    }

    override fun getRecord(id: String): PositionRecord? = records[id]
}