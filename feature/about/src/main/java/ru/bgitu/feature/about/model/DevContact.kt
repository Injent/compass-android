package ru.bgitu.feature.about.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import ru.bgitu.core.common.BuildConfig
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.icon.Telegram
import ru.bgitu.core.designsystem.icon.Vk
import ru.bgitu.feature.about.R

enum class DevContact(
    fullName: String,
    @StringRes val nameResId: Int,
    @StringRes val roleResId: Int,
    private val _socials: List<Social>
) {
    MOBILE_DEV(
        fullName = "eliseyVerevkin",
        nameResId = R.string.mobile_dev_name,
        roleResId = R.string.mobile_dev_role,
        _socials =  listOf(
            Social(
                icon = AppIcons.Telegram,
                url = "tg"
            ),
            Social(
                icon = AppIcons.Vk,
                url = "vk"
            )
        )
    ),
    BACKEND_DEV(
        fullName ="kirillPudov",
        nameResId = R.string.backend_dev,
        roleResId = R.string.backend_dev_role,
        _socials = listOf(
            Social(
                icon = AppIcons.Telegram,
                url = "tg"
            ),
            Social(
                icon = AppIcons.Vk,
                url = "vk"
            )
        )
    );

    private val baseUrl = "${BuildConfig.COMPASS_SITE}/contacts/$fullName/"

    val socials: List<Social>
        get() = _socials.map {
            it.copy(url = "$baseUrl${it.url}")
        }

    val avatarUrl: String
        get() = "${baseUrl}avatar.png"

    data class Social(
        val icon: ImageVector,
        val url: String
    )
}