package ru.bgitu.core.designsystem.illustration

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.bgitu.core.designsystem.theme.AppTheme

val AppIllustrations.Tasks: ImageVector
    @Composable get() {
        if (_Tasks != null) {
            return _Tasks!!
        }
        val primaryColor = AppTheme.colorScheme.backgroundBrand
        _Tasks = ImageVector.Builder(
            name = "Tasks",
            defaultWidth = 300.dp,
            defaultHeight = 300.dp,
            viewportWidth = 300f,
            viewportHeight = 300f
        ).apply {
            path(fill = SolidColor(primaryColor)) {
                moveToRelative(71.33f, 209.26f)
                lineToRelative(152.56f, 88.08f)
                arcToRelative(3.59f, 3.59f, 0f, isMoreThanHalf = false, isPositiveArc = false, 5.39f, -3.1f)
                lineTo(229.28f, 93.3f)
                arcToRelative(9.79f, 9.79f, 0f, isMoreThanHalf = false, isPositiveArc = false, -4.89f, -8.46f)
                lineTo(78.62f, 0.65f)
                arcToRelative(4.86f, 4.86f, 0f, isMoreThanHalf = false, isPositiveArc = false, -7.29f, 4.21f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFFFFFFF)),
                fillAlpha = 0.5f,
                strokeAlpha = 0.5f
            ) {
                moveToRelative(71.33f, 209.26f)
                lineToRelative(152.56f, 88.08f)
                arcToRelative(3.59f, 3.59f, 0f, isMoreThanHalf = false, isPositiveArc = false, 5.39f, -3.1f)
                lineTo(229.28f, 93.3f)
                arcToRelative(9.79f, 9.79f, 0f, isMoreThanHalf = false, isPositiveArc = false, -4.89f, -8.46f)
                lineTo(78.62f, 0.65f)
                arcToRelative(4.86f, 4.86f, 0f, isMoreThanHalf = false, isPositiveArc = false, -7.29f, 4.21f)
                close()
            }
            path(fill = SolidColor(primaryColor)) {
                moveTo(75.65f, 218.49f)
                lineToRelative(139.89f, 80.76f)
                lineToRelative(0f, -204.19f)
                lineToRelative(-139.89f, -80.76f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFFFFFFF)),
                fillAlpha = 0.8f,
                strokeAlpha = 0.8f
            ) {
                moveTo(75.65f, 218.49f)
                lineToRelative(139.89f, 80.76f)
                lineToRelative(0f, -204.19f)
                lineToRelative(-139.89f, -80.76f)
                close()
            }
            path(fill = SolidColor(primaryColor)) {
                moveTo(61.99f, 202.18f)
                lineToRelative(139.89f, 80.76f)
                lineToRelative(13.66f, -187.88f)
                lineToRelative(-139.89f, -80.76f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFFFFFFF)),
                fillAlpha = 0.9f,
                strokeAlpha = 0.9f
            ) {
                moveTo(61.99f, 202.18f)
                lineToRelative(139.89f, 80.76f)
                lineToRelative(13.66f, -187.88f)
                lineToRelative(-139.89f, -80.76f)
                close()
            }
            path(fill = SolidColor(primaryColor)) {
                moveTo(75.65f, 14.3f)
                lineToRelative(139.89f, 80.76f)
                lineToRelative(5.3f, -3.06f)
                lineToRelative(-139.89f, -80.77f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFFFFFFF)),
                fillAlpha = 0.7f,
                strokeAlpha = 0.7f
            ) {
                moveTo(75.65f, 14.3f)
                lineToRelative(139.89f, 80.76f)
                lineToRelative(5.3f, -3.06f)
                lineToRelative(-139.89f, -80.77f)
                close()
            }
            path(fill = SolidColor(primaryColor)) {
                moveTo(220.84f, 92f)
                lineToRelative(0f, 203.41f)
                lineToRelative(-5.3f, 3.84f)
                lineToRelative(0f, -204.19f)
                close()
            }
            path(fill = SolidColor(primaryColor)) {
                moveTo(220.84f, 92f)
                lineToRelative(0f, 203.41f)
                lineToRelative(-5.3f, 3.84f)
                lineToRelative(0f, -204.19f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFFFFFFF)),
                fillAlpha = 0.7f,
                strokeAlpha = 0.7f
            ) {
                moveTo(220.84f, 92f)
                lineToRelative(0f, 203.41f)
                lineToRelative(-5.3f, 3.84f)
                lineToRelative(0f, -204.19f)
                close()
            }
            path(fill = SolidColor(primaryColor)) {
                moveToRelative(177.04f, 121.56f)
                arcToRelative(1.5f, 1.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.37f, 0f)
                arcToRelative(1.41f, 1.41f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.94f, -0.84f)
                lineToRelative(-4.16f, -10.45f)
                arcToRelative(1.41f, 1.41f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.79f, -1.82f)
                arcToRelative(1.39f, 1.39f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.82f, 0.78f)
                lineToRelative(3.46f, 8.69f)
                lineToRelative(19.91f, -15.81f)
                arcToRelative(1.4f, 1.4f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2f, 0.22f)
                arcToRelative(1.42f, 1.42f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.22f, 2f)
                lineToRelative(-21.39f, 17f)
                arcToRelative(1.39f, 1.39f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.9f, 0.23f)
                close()
            }
            path(fill = SolidColor(primaryColor)) {
                moveToRelative(153.79f, 99.03f)
                arcToRelative(1.46f, 1.46f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.7f, -0.18f)
                lineToRelative(-58.9f, -34f)
                arcToRelative(1.4f, 1.4f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.41f, -2.43f)
                lineToRelative(58.9f, 34f)
                arcToRelative(1.4f, 1.4f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.71f, 2.62f)
                close()
            }
            path(fill = SolidColor(primaryColor)) {
                moveToRelative(152.44f, 111.12f)
                arcToRelative(1.37f, 1.37f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.7f, -0.19f)
                lineToRelative(-58.9f, -34f)
                arcToRelative(1.4f, 1.4f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.41f, -2.43f)
                lineToRelative(58.9f, 34f)
                arcToRelative(1.4f, 1.4f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.71f, 2.62f)
                close()
            }
            path(fill = SolidColor(primaryColor)) {
                moveToRelative(186.43f, 136.87f)
                arcToRelative(3.7f, 3.7f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1.82f, -0.48f)
                lineToRelative(-18.21f, -10.07f)
                arcToRelative(7.75f, 7.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, -4f, -7.43f)
                lineToRelative(1.73f, -20.31f)
                arcToRelative(3.77f, 3.77f, 0f, isMoreThanHalf = false, isPositiveArc = true, 5.62f, -2.93f)
                lineToRelative(19.15f, 11.06f)
                arcToRelative(5.65f, 5.65f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2.79f, 5.26f)
                lineToRelative(-1.56f, 21.41f)
                arcToRelative(3.72f, 3.72f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1.95f, 3f)
                arcToRelative(3.81f, 3.81f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1.75f, 0.49f)
                close()
                moveTo(167.92f, 97.96f)
                arcToRelative(0.95f, 0.95f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.95f, 0.86f)
                lineToRelative(-1.73f, 20.31f)
                arcToRelative(4.92f, 4.92f, 0f, isMoreThanHalf = false, isPositiveArc = false, 2.52f, 4.73f)
                lineToRelative(18.21f, 10.08f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.91f, 0f)
                arcToRelative(0.94f, 0.94f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.49f, -0.76f)
                lineToRelative(1.56f, -21.41f)
                arcToRelative(2.82f, 2.82f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.39f, -2.62f)
                verticalLineToRelative(0f)
                lineToRelative(-19.16f, -11.06f)
                arcToRelative(0.85f, 0.85f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.46f, -0.13f)
                close()
            }
            path(fill = SolidColor(primaryColor)) {
                moveToRelative(149.63f, 149.07f)
                arcToRelative(1.39f, 1.39f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.7f, -0.19f)
                lineToRelative(-58.9f, -34f)
                arcToRelative(1.4f, 1.4f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.41f, -2.43f)
                lineToRelative(58.9f, 34f)
                arcToRelative(1.4f, 1.4f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.71f, 2.62f)
                close()
            }
            path(fill = SolidColor(primaryColor)) {
                moveToRelative(148.28f, 161.15f)
                arcToRelative(1.39f, 1.39f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.7f, -0.19f)
                lineToRelative(-58.9f, -34f)
                arcToRelative(1.4f, 1.4f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.41f, -2.43f)
                lineToRelative(58.9f, 34f)
                arcToRelative(1.4f, 1.4f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.71f, 2.62f)
                close()
            }
            path(fill = SolidColor(primaryColor)) {
                moveToRelative(182.27f, 186.9f)
                arcToRelative(3.83f, 3.83f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1.82f, -0.47f)
                lineTo(162.24f, 176.35f)
                arcToRelative(7.74f, 7.74f, 0f, isMoreThanHalf = false, isPositiveArc = true, -4f, -7.43f)
                lineTo(160.04f, 148.65f)
                arcToRelative(3.76f, 3.76f, 0f, isMoreThanHalf = false, isPositiveArc = true, 5.62f, -2.93f)
                lineToRelative(19.15f, 11.06f)
                verticalLineToRelative(0f)
                arcToRelative(5.63f, 5.63f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2.79f, 5.25f)
                lineTo(186.04f, 183.42f)
                arcToRelative(3.76f, 3.76f, 0f, isMoreThanHalf = false, isPositiveArc = true, -3.74f, 3.48f)
                close()
                moveTo(163.76f, 147.99f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.44f, 0.11f)
                arcToRelative(0.93f, 0.93f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.51f, 0.76f)
                lineToRelative(-1.77f, 20.3f)
                arcToRelative(4.92f, 4.92f, 0f, isMoreThanHalf = false, isPositiveArc = false, 2.52f, 4.73f)
                lineToRelative(18.21f, 10.08f)
                arcToRelative(0.91f, 0.91f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.91f, 0f)
                arcToRelative(0.9f, 0.9f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.49f, -0.76f)
                lineToRelative(1.56f, -21.41f)
                arcToRelative(2.82f, 2.82f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.39f, -2.62f)
                lineToRelative(-19.16f, -11.06f)
                arcToRelative(0.85f, 0.85f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.42f, -0.13f)
                close()
            }
            path(fill = SolidColor(primaryColor)) {
                moveToRelative(173.41f, 171.13f)
                arcToRelative(1.57f, 1.57f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.37f, 0f)
                arcToRelative(1.41f, 1.41f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.94f, -0.84f)
                lineTo(167.94f, 159.85f)
                arcToRelative(1.4f, 1.4f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2.61f, -1f)
                lineToRelative(3.49f, 8.6f)
                lineToRelative(19.88f, -15.8f)
                arcToRelative(1.41f, 1.41f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.75f, 2.2f)
                lineToRelative(-21.39f, 17f)
                arcToRelative(1.4f, 1.4f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.87f, 0.28f)
                close()
            }
            path(fill = SolidColor(primaryColor)) {
                moveToRelative(145.48f, 199.1f)
                arcToRelative(1.37f, 1.37f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.7f, -0.19f)
                lineToRelative(-58.91f, -34f)
                arcToRelative(1.41f, 1.41f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.41f, -2.44f)
                lineToRelative(58.9f, 34f)
                arcToRelative(1.4f, 1.4f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.7f, 2.62f)
                close()
            }
            path(fill = SolidColor(primaryColor)) {
                moveToRelative(144.13f, 211.19f)
                arcToRelative(1.43f, 1.43f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.71f, -0.19f)
                lineToRelative(-58.9f, -34f)
                arcToRelative(1.4f, 1.4f, 0f, isMoreThanHalf = true, isPositiveArc = true, 1.41f, -2.43f)
                lineToRelative(58.9f, 34f)
                arcToRelative(1.41f, 1.41f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.7f, 2.63f)
                close()
            }
            path(fill = SolidColor(primaryColor)) {
                moveToRelative(178.11f, 236.93f)
                arcToRelative(3.65f, 3.65f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1.81f, -0.47f)
                lineTo(158.04f, 226.38f)
                arcToRelative(7.75f, 7.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, -4f, -7.43f)
                lineToRelative(1.74f, -20.3f)
                arcToRelative(3.72f, 3.72f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2f, -3f)
                arcToRelative(3.76f, 3.76f, 0f, isMoreThanHalf = false, isPositiveArc = true, 3.61f, 0.08f)
                lineToRelative(19.15f, 11.06f)
                verticalLineToRelative(0f)
                arcToRelative(5.64f, 5.64f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2.78f, 5.26f)
                lineToRelative(-1.56f, 21.41f)
                arcToRelative(3.74f, 3.74f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1.95f, 3f)
                arcToRelative(3.8f, 3.8f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1.7f, 0.47f)
                close()
                moveTo(159.6f, 198.03f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.44f, 0.1f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.51f, 0.76f)
                lineToRelative(-1.73f, 20.3f)
                arcToRelative(4.91f, 4.91f, 0f, isMoreThanHalf = false, isPositiveArc = false, 2.52f, 4.73f)
                lineToRelative(18.22f, 10.08f)
                arcToRelative(0.93f, 0.93f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.39f, -0.75f)
                lineToRelative(1.56f, -21.42f)
                arcToRelative(2.8f, 2.8f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.39f, -2.62f)
                lineTo(160.04f, 198.15f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.44f, -0.12f)
                close()
            }
            path(fill = SolidColor(primaryColor)) {
                moveToRelative(177.13f, 83.5f)
                arcToRelative(6f, 6f, 0f, isMoreThanHalf = false, isPositiveArc = true, -3f, -0.79f)
                lineTo(149.04f, 68.54f)
                arcToRelative(1.39f, 1.39f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.61f, -0.7f)
                arcToRelative(16f, 16f, 0f, isMoreThanHalf = false, isPositiveArc = false, -6.19f, -7.63f)
                curveToRelative(-1f, -0.6f, -3.62f, -1.79f, -5f, 0.22f)
                arcToRelative(1.42f, 1.42f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1.84f, 0.41f)
                lineTo(113.78f, 48.7f)
                arcToRelative(8f, 8f, 0f, isMoreThanHalf = false, isPositiveArc = true, -4f, -6.92f)
                lineTo(109.78f, 26.4f)
                arcToRelative(8.74f, 8.74f, 0f, isMoreThanHalf = false, isPositiveArc = true, 5.08f, -7.93f)
                arcToRelative(1.4f, 1.4f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.29f, 0.06f)
                lineToRelative(69.46f, 40.36f)
                arcToRelative(1.4f, 1.4f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.14f, 2.5f)
                arcToRelative(3.71f, 3.71f, 0f, isMoreThanHalf = false, isPositiveArc = false, -2.22f, 3.4f)
                verticalLineToRelative(12.62f)
                arcToRelative(6f, 6f, 0f, isMoreThanHalf = false, isPositiveArc = true, -3f, 5.25f)
                arcToRelative(6.09f, 6.09f, 0f, isMoreThanHalf = false, isPositiveArc = true, -3.12f, 0.84f)
                close()
                moveTo(150.8f, 66.33f)
                lineTo(175.52f, 80.26f)
                arcToRelative(3.27f, 3.27f, 0f, isMoreThanHalf = false, isPositiveArc = false, 4.88f, -2.85f)
                lineTo(180.4f, 64.79f)
                arcToRelative(6.51f, 6.51f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.87f, -4.57f)
                lineTo(115.37f, 21.35f)
                arcToRelative(6f, 6f, 0f, isMoreThanHalf = false, isPositiveArc = false, -2.82f, 5f)
                verticalLineToRelative(15.43f)
                arcToRelative(5.14f, 5.14f, 0f, isMoreThanHalf = false, isPositiveArc = false, 2.61f, 4.47f)
                lineToRelative(20.57f, 11.59f)
                curveToRelative(1.95f, -1.72f, 4.9f, -1.78f, 7.88f, -0.06f)
                arcToRelative(18.44f, 18.44f, 0f, isMoreThanHalf = false, isPositiveArc = true, 7.19f, 8.55f)
                close()
            }
        }.build()

        return _Tasks!!
    }

@Suppress("ObjectPropertyName")
private var _Tasks: ImageVector? = null
