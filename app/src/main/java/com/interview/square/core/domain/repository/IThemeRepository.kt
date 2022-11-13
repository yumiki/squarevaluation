package com.interview.square.core.domain.repository

import com.interview.square.core.domain.model.AppColor
import com.interview.square.core.domain.model.AppColorScheme

interface IThemeRepository {
    suspend fun updatePrimaryUserColorScheme(color: AppColor)
    suspend fun getUserColorScheme(): AppColorScheme
}

object StubThemeRepository: IThemeRepository {
    override suspend fun updatePrimaryUserColorScheme(color: AppColor) {
        TODO("Not yet implemented")
    }

    override suspend fun getUserColorScheme(): AppColorScheme {
        TODO("Not yet implemented")
    }
}