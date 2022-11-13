package com.interview.square.core.data.repository

import com.interview.square.core.domain.model.AppColor
import com.interview.square.core.domain.model.AppColorScheme
import com.interview.square.core.domain.model.toAppColor
import com.interview.square.core.domain.repository.IThemeRepository
import com.interview.square.core.ui.theme.UserColorScheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class ThemeRepository: IThemeRepository {

    private var colorScheme = MutableStateFlow(UserColorScheme)

    override suspend fun updatePrimaryUserColorScheme(color: AppColor, variantColor: AppColor?) {
        colorScheme.update {
            it.copy(primary = color.toColor(), primaryContainer = variantColor?.toColor()?: it.primaryContainer)
        }
    }

    override suspend fun getUserColorScheme(): AppColorScheme = colorScheme.value.toAppColor()
}
