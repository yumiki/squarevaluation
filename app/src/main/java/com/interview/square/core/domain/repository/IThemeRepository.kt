package com.interview.square.core.domain.repository

import com.interview.square.core.domain.model.AppColor
import com.interview.square.core.domain.model.AppColorScheme

interface IThemeRepository {
    suspend fun updatePrimaryUserColorScheme(color: AppColor, variantColor: AppColor?)
    suspend fun getUserColorScheme(): AppColorScheme
}