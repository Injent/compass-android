package ru.bgitu.core.designsystem.icon

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val AppIcons.Telegram: ImageVector
    get() {
        if (_Telegram != null) {
            return _Telegram!!
        }
        _Telegram = ImageVector.Builder(
            name = "Telegram",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 1000f,
            viewportHeight = 1000f
        ).apply {
            path(
                fill = Brush.linearGradient(
                    colorStops = arrayOf(
                        0f to Color(0xFF2AABEE),
                        1f to Color(0xFF229ED9)
                    ),
                    start = Offset(500f, -0f),
                    end = Offset(500f, 992.6f)
                ),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 1f,
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(500f, 500f)
                moveToRelative(-500f, 0f)
                arcToRelative(500f, 500f, 0f, isMoreThanHalf = true, isPositiveArc = true, 1000f, 0f)
                arcToRelative(500f, 500f, 0f, isMoreThanHalf = true, isPositiveArc = true, -1000f, 0f)
            }
            path(
                fill = SolidColor(Color(0xFFFFFFFF)),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 1f,
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(226.3f, 494.7f)
                curveTo(372.1f, 431.2f, 469.3f, 389.4f, 517.9f, 369.1f)
                curveTo(656.8f, 311.4f, 685.6f, 301.3f, 704.4f, 301f)
                curveTo(708.6f, 300.9f, 717.8f, 302f, 723.8f, 306.8f)
                curveTo(728.9f, 310.9f, 730.3f, 316.5f, 730.9f, 320.4f)
                curveTo(731.6f, 324.2f, 732.4f, 333.1f, 731.8f, 340f)
                curveTo(724.2f, 419.1f, 691.7f, 611f, 675.1f, 699.5f)
                curveTo(668.1f, 737f, 654.3f, 749.5f, 640.9f, 750.8f)
                curveTo(611.9f, 753.4f, 589.9f, 731.6f, 561.7f, 713.2f)
                curveTo(517.7f, 684.3f, 492.9f, 666.3f, 450.2f, 638.2f)
                curveTo(400.8f, 605.7f, 432.8f, 587.8f, 460.9f, 558.6f)
                curveTo(468.3f, 550.9f, 596.2f, 434.6f, 598.7f, 424f)
                curveTo(599f, 422.7f, 599.3f, 417.8f, 596.4f, 415.2f)
                curveTo(593.4f, 412.6f, 589.1f, 413.5f, 586f, 414.2f)
                curveTo(581.6f, 415.2f, 511.3f, 461.6f, 375.1f, 553.6f)
                curveTo(355.2f, 567.3f, 337.1f, 573.9f, 320.9f, 573.6f)
                curveTo(303f, 573.2f, 268.7f, 563.5f, 243.2f, 555.2f)
                curveTo(211.9f, 545f, 187f, 539.6f, 189.1f, 522.3f)
                curveTo(190.3f, 513.3f, 202.7f, 504.1f, 226.3f, 494.7f)
                close()
            }
        }.build()

        return _Telegram!!
    }

@Suppress("ObjectPropertyName")
private var _Telegram: ImageVector? = null
