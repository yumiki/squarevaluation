package com.interview.square.core.data.repository

import com.interview.square.core.domain.model.AppColor
import com.interview.square.core.domain.model.AppColorScheme
import com.interview.square.core.domain.repository.IThemeRepository

class ThemeRepository: IThemeRepository {
    override suspend fun updatePrimaryUserColorScheme(color: AppColor) {
        TODO("Not yet implemented")
    }

    override suspend fun getUserColorScheme(): AppColorScheme {
        TODO("Not yet implemented")
    }
}
