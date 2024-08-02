package ru.bgitu.feature.profile.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.feature.profile.R

enum class ProfileItem(
    @StringRes val labelRes: Int,
    @DrawableRes val iconRes: Int
) {
    MY_GROUPS(R.string.my_groups, AppIcons.Group),
    SETTINGS(R.string.settings, AppIcons.SettingsOutlined),
    HELP(R.string.help, AppIcons.Help),
    ABOUT(R.string.about_app, AppIcons.SmartPhone),
}
