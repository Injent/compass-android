package ru.bgitu.feature.profile.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.icon.Group
import ru.bgitu.core.designsystem.icon.Help
import ru.bgitu.core.designsystem.icon.SettingsOutlined
import ru.bgitu.core.designsystem.icon.Smartphone
import ru.bgitu.feature.profile.R

enum class ProfileItem(
    @StringRes val labelRes: Int,
    val icon: ImageVector
) {
    MY_GROUPS(R.string.my_groups, AppIcons.Group),
    SETTINGS(R.string.settings, AppIcons.SettingsOutlined),
    HELP(R.string.help, AppIcons.Help),
    ABOUT(R.string.about_app, AppIcons.Smartphone),
}
