package ru.bgitu.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val AppIcons.PersonCard: ImageVector
    get() {
        if (_ChatBubbleUser != null) {
            return _ChatBubbleUser!!
        }
        _ChatBubbleUser = ImageVector.Builder(
            name = "ChatBubbleUser",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF3F7BAB)),
                strokeLineWidth = 1.16702f
            ) {
                moveToRelative(15.026f, 10.853f)
                arcToRelative(3.781f, 3.781f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.829f, -2.334f)
                arcToRelative(3.839f, 3.839f, 0f, isMoreThanHalf = false, isPositiveArc = false, -7.679f, 0f)
                arcToRelative(3.781f, 3.781f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.829f, 2.334f)
                arcToRelative(5.835f, 5.835f, 0f, isMoreThanHalf = false, isPositiveArc = false, -2.334f, 2.696f)
                arcToRelative(1.167f, 1.167f, 0f, isMoreThanHalf = true, isPositiveArc = false, 2.147f, 0.91f)
                arcToRelative(3.501f, 3.501f, 0f, isMoreThanHalf = false, isPositiveArc = true, 3.198f, -2.124f)
                verticalLineToRelative(0f)
                arcToRelative(3.501f, 3.501f, 0f, isMoreThanHalf = false, isPositiveArc = true, 3.209f, 2.124f)
                arcToRelative(1.167f, 1.167f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.074f, 0.712f)
                arcToRelative(1.272f, 1.272f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.455f, -0.093f)
                arcToRelative(1.167f, 1.167f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.619f, -1.529f)
                arcToRelative(5.835f, 5.835f, 0f, isMoreThanHalf = false, isPositiveArc = false, -2.346f, -2.696f)
                close()
                moveTo(12.015f, 10.001f)
                verticalLineToRelative(0f)
                arcTo(1.505f, 1.505f, 0f, isMoreThanHalf = true, isPositiveArc = true, 13.521f, 8.508f)
                arcTo(1.505f, 1.505f, 0f, isMoreThanHalf = false, isPositiveArc = true, 12.015f, 10.001f)
                close()
                moveTo(19.018f, 0f)
                lineTo(5.013f, 0f)
                arcTo(3.501f, 3.501f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.512f, 3.501f)
                lineTo(1.512f, 16.338f)
                arcToRelative(3.501f, 3.501f, 0f, isMoreThanHalf = false, isPositiveArc = false, 3.501f, 3.501f)
                lineTo(8.036f, 19.839f)
                lineToRelative(3.151f, 3.163f)
                arcToRelative(1.167f, 1.167f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.829f, 0.338f)
                arcToRelative(1.167f, 1.167f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.759f, -0.28f)
                lineToRelative(3.758f, -3.221f)
                horizontalLineToRelative(2.486f)
                arcToRelative(3.501f, 3.501f, 0f, isMoreThanHalf = false, isPositiveArc = false, 3.501f, -3.501f)
                lineTo(22.519f, 3.501f)
                arcTo(3.501f, 3.501f, 0f, isMoreThanHalf = false, isPositiveArc = false, 19.018f, 0f)
                close()
                moveTo(20.185f, 16.338f)
                arcToRelative(1.167f, 1.167f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1.167f, 1.167f)
                horizontalLineToRelative(-2.918f)
                arcToRelative(1.167f, 1.167f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.759f, 0.28f)
                lineTo(12.074f, 20.586f)
                lineTo(9.343f, 17.844f)
                arcTo(1.167f, 1.167f, 0f, isMoreThanHalf = false, isPositiveArc = false, 8.514f, 17.505f)
                lineTo(5.013f, 17.505f)
                arcTo(1.167f, 1.167f, 0f, isMoreThanHalf = false, isPositiveArc = true, 3.846f, 16.338f)
                lineTo(3.846f, 3.501f)
                arcTo(1.167f, 1.167f, 0f, isMoreThanHalf = false, isPositiveArc = true, 5.013f, 2.334f)
                lineTo(19.018f, 2.334f)
                arcToRelative(1.167f, 1.167f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.167f, 1.167f)
                close()
            }
        }.build()

        return _ChatBubbleUser!!
    }

@Suppress("ObjectPropertyName")
private var _ChatBubbleUser: ImageVector? = null
