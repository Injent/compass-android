package ru.bgitu.core.designsystem.illustration

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.bgitu.core.designsystem.theme.LocalNewColorScheme

val AppIllustrations.Calendar: ImageVector
    @Composable get() {
        if (_IllCalendar != null) {
            return _IllCalendar!!
        }
        val primaryColor = LocalNewColorScheme.current.foreground
        
        _IllCalendar = ImageVector.Builder(
            name = "IllCalendar",
            defaultWidth = 225.2.dp,
            defaultHeight = 256.dp,
            viewportWidth = 225.2f,
            viewportHeight = 256f
        ).apply {
            path(fill = SolidColor(Color(0xFFF0F0F0))) {
                moveToRelative(1.21f, 224.43f)
                arcToRelative(2.74f, 2.74f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1.16f, -2.83f)
                lineToRelative(27.77f, -122.9f)
                arcToRelative(5.1f, 5.1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2.12f, -2.89f)
                lineToRelative(165.23f, -95.38f)
                arcToRelative(3.63f, 3.63f, 0f, isMoreThanHalf = false, isPositiveArc = true, 3.29f, -0.05f)
                arcToRelative(2.74f, 2.74f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.14f, 2.81f)
                lineToRelative(-27.78f, 122.9f)
                arcToRelative(5.08f, 5.08f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2.13f, 2.89f)
                lineTo(4.5f, 224.36f)
                arcToRelative(3.63f, 3.63f, 0f, isMoreThanHalf = false, isPositiveArc = true, -3.29f, 0.07f)
                close()
            }
            path(fill = SolidColor(Color(0xFFE6E6E6))) {
                moveToRelative(31f, 100.52f)
                lineToRelative(-27.77f, 122.94f)
                curveToRelative(-0.23f, 1.05f, 0.33f, 1.47f, 1.27f, 0.93f)
                lineToRelative(165.23f, -95.38f)
                arcToRelative(5.08f, 5.08f, 0f, isMoreThanHalf = false, isPositiveArc = false, 2.13f, -2.89f)
                lineTo(199.6f, 3.19f)
                curveToRelative(0.23f, -1.06f, -0.33f, -1.47f, -1.27f, -0.93f)
                lineToRelative(-165.23f, 95.38f)
                arcToRelative(5.11f, 5.11f, 0f, isMoreThanHalf = false, isPositiveArc = false, -2.1f, 2.88f)
                close()
            }
            path(fill = SolidColor(Color(0xFFF0F0F0))) {
                moveToRelative(201.61f, 2.2f)
                arcToRelative(3.63f, 3.63f, 0f, isMoreThanHalf = false, isPositiveArc = false, -3.29f, 0.06f)
                lineToRelative(-165.23f, 95.38f)
                arcToRelative(3.13f, 3.13f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.42f, 2.91f)
                lineToRelative(21.75f, 152.22f)
                arcToRelative(4.41f, 4.41f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.87f, 2.86f)
                arcToRelative(3.59f, 3.59f, 0f, isMoreThanHalf = false, isPositiveArc = false, 3.28f, -0.06f)
                lineTo(223.77f, 160.22f)
                arcToRelative(3.13f, 3.13f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.42f, -2.92f)
                lineTo(203.51f, 5.06f)
                arcToRelative(4.4f, 4.4f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.9f, -2.86f)
                close()
            }
            path(fill = SolidColor(Color(0xFFFAFAFA))) {
                moveToRelative(34.89f, 102.39f)
                lineToRelative(21.71f, 152.22f)
                arcToRelative(1.23f, 1.23f, 135f, isMoreThanHalf = false, isPositiveArc = false, 1.97f, 0.96f)
                lineTo(223.77f, 160.22f)
                arcToRelative(3.13f, 3.13f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.42f, -2.92f)
                lineTo(203.51f, 5.06f)
                arcToRelative(1.23f, 1.23f, 135f, isMoreThanHalf = false, isPositiveArc = false, -1.97f, -0.96f)
                lineToRelative(-165.23f, 95.38f)
                arcToRelative(3.13f, 3.13f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.43f, 2.92f)
                close()
            }
            path(fill = SolidColor(Color(0xFFE0E0E0))) {
                moveToRelative(3.38f, 222.78f)
                lineToRelative(42.29f, -24.42f)
                lineToRelative(1.23f, 8.85f)
                lineToRelative(-19.39f, 11.2f)
                curveToRelative(0f, 0f, -5.17f, 1.83f, -7.22f, 2.35f)
                curveToRelative(-2.05f, 0.52f, -16.91f, 2.03f, -16.91f, 2.03f)
                close()
            }
            path(fill = SolidColor(Color(0xFFE6E6E6))) {
                moveTo(46.92f, 207.21f)
                lineToRelative(6.34f, 44.48f)
                lineToRelative(-25.73f, -33.28f)
                close()
            }
            path(fill = SolidColor(Color(0xFFE0E0E0))) {
                moveToRelative(52.51f, 253.17f)
                lineToRelative(-23.06f, -29.95f)
                arcToRelative(3.7f, 3.7f, 45f, isMoreThanHalf = false, isPositiveArc = false, -3.13f, -1.28f)
                lineToRelative(-21.57f, 3.13f)
                arcTo(6.35f, 6.35f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.21f, 224.43f)
                curveToRelative(-0.88f, -0.5f, -0.73f, -1.08f, 0.33f, -1.28f)
                lineToRelative(24.78f, -4.77f)
                arcToRelative(3.41f, 3.41f, 0f, isMoreThanHalf = false, isPositiveArc = true, 3.13f, 1.2f)
                lineToRelative(26.26f, 35.4f)
                curveToRelative(0.64f, 0.87f, 0.45f, 1.16f, -0.42f, 0.65f)
                arcToRelative(11.38f, 11.38f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2.79f, -2.45f)
                close()
            }
            path(fill = SolidColor(primaryColor)) {
                moveToRelative(199.64f, 2.59f)
                arcToRelative(2.69f, 2.69f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.21f, -2.22f)
                arcToRelative(3.66f, 3.66f, 0f, isMoreThanHalf = false, isPositiveArc = false, -3.29f, 0.06f)
                lineToRelative(-165.23f, 95.38f)
                arcToRelative(4.65f, 4.65f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.29f, 1.21f)
                arcToRelative(4.78f, 4.78f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.84f, 1.68f)
                lineToRelative(-3.92f, 17.36f)
                lineToRelative(3.18f, 1.82f)
                lineTo(195.68f, 20.55f)
                lineTo(199.6f, 3.19f)
                arcToRelative(2.82f, 2.82f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.05f, -0.6f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 0.2f,
                strokeAlpha = 0.2f
            ) {
                moveToRelative(199.6f, 3.19f)
                curveToRelative(0.23f, -1.06f, -0.33f, -1.47f, -1.27f, -0.93f)
                lineToRelative(-165.23f, 95.38f)
                arcToRelative(5.11f, 5.11f, 0f, isMoreThanHalf = false, isPositiveArc = false, -2.12f, 2.88f)
                lineToRelative(-3.92f, 17.36f)
                lineTo(195.68f, 20.55f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 0.1f,
                strokeAlpha = 0.1f
            ) {
                moveToRelative(31.83f, 98.85f)
                arcToRelative(4.59f, 4.59f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.83f, 1.68f)
                lineToRelative(-3.92f, 17.36f)
                lineToRelative(-3.18f, -1.83f)
                lineToRelative(3.92f, -17.36f)
                arcToRelative(4.78f, 4.78f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.84f, -1.68f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFFFFFFF)),
                fillAlpha = 0.4f,
                strokeAlpha = 0.4f
            ) {
                moveToRelative(199.64f, 2.58f)
                curveToRelative(-0.08f, -0.59f, -0.59f, -0.78f, -1.32f, -0.32f)
                lineToRelative(-165.23f, 95.37f)
                arcToRelative(4.47f, 4.47f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.29f, 1.21f)
                lineToRelative(-3.18f, -1.84f)
                arcToRelative(4.65f, 4.65f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.29f, -1.21f)
                lineToRelative(165.23f, -95.38f)
                arcToRelative(3.66f, 3.66f, 0f, isMoreThanHalf = false, isPositiveArc = true, 3.29f, -0.06f)
                arcToRelative(2.69f, 2.69f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.21f, 2.22f)
                close()
            }
            path(fill = SolidColor(primaryColor)) {
                moveToRelative(203.68f, 6.51f)
                lineToRelative(0f, 0f)
                lineTo(203.51f, 5.06f)
                arcToRelative(2.04f, 2.04f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.05f, -0.21f)
                arcToRelative(0.2f, 0.2f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, -0.06f)
                lineToRelative(0f, 0f)
                arcToRelative(4.44f, 4.44f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.81f, -2.58f)
                arcToRelative(3.63f, 3.63f, 0f, isMoreThanHalf = false, isPositiveArc = false, -3.29f, 0.06f)
                lineToRelative(-165.23f, 95.38f)
                arcToRelative(2.94f, 2.94f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.14f, 1.3f)
                lineToRelative(0f, 0f)
                arcToRelative(2.94f, 2.94f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.28f, 1.61f)
                lineToRelative(3.13f, 21.81f)
                lineToRelative(3.18f, 1.84f)
                lineToRelative(168.56f, -97.33f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 0.1f,
                strokeAlpha = 0.1f
            ) {
                moveToRelative(38f, 124.19f)
                lineToRelative(-3.18f, -1.84f)
                lineToRelative(-3.13f, -21.81f)
                arcToRelative(2.94f, 2.94f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.28f, -1.61f)
                lineToRelative(3.19f, 1.83f)
                arcToRelative(2.89f, 2.89f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.29f, 1.61f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFFFFFFF)),
                fillAlpha = 0.4f,
                strokeAlpha = 0.4f
            ) {
                moveToRelative(203.42f, 4.78f)
                arcToRelative(1.21f, 1.21f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.91f, -0.69f)
                lineToRelative(-165.23f, 95.38f)
                arcToRelative(2.89f, 2.89f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.14f, 1.3f)
                lineTo(32.02f, 98.94f)
                arcToRelative(3f, 3f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.14f, -1.31f)
                lineToRelative(165.23f, -95.37f)
                arcToRelative(3.63f, 3.63f, 0f, isMoreThanHalf = false, isPositiveArc = true, 3.29f, -0.06f)
                arcToRelative(4.41f, 4.41f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.75f, 2.58f)
                close()
            }
            path(fill = SolidColor(primaryColor)) {
                moveToRelative(81.86f, 229.39f)
                lineToRelative(-1.97f, -13.87f)
                arcToRelative(0.99f, 0.99f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.57f, -0.78f)
                lineToRelative(-14.4f, 8.32f)
                arcToRelative(2.49f, 2.49f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.14f, 2.35f)
                lineTo(64.75f, 239.31f)
                arcToRelative(0.98f, 0.98f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.57f, 0.78f)
                lineToRelative(14.4f, -8.32f)
                arcToRelative(2.48f, 2.48f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.14f, -2.38f)
                close()
            }
            path(fill = SolidColor(Color(0xFFEBEBEB))) {
                moveToRelative(104.13f, 216.55f)
                lineToRelative(-1.98f, -13.86f)
                arcToRelative(0.98f, 0.98f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.57f, -0.78f)
                lineToRelative(-14.4f, 8.31f)
                arcToRelative(2.5f, 2.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.14f, 2.35f)
                lineToRelative(1.98f, 13.87f)
                arcToRelative(0.98f, 0.98f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.57f, 0.78f)
                lineToRelative(14.4f, -8.32f)
                arcToRelative(2.48f, 2.48f, 45f, isMoreThanHalf = false, isPositiveArc = false, 1.14f, -2.35f)
                close()
            }
            path(fill = SolidColor(Color(0xFFEBEBEB))) {
                moveTo(126.41f, 203.71f)
                lineTo(124.42f, 189.85f)
                arcToRelative(0.98f, 0.98f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.57f, -0.78f)
                lineToRelative(-14.41f, 8.32f)
                arcToRelative(2.5f, 2.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.14f, 2.35f)
                lineToRelative(1.98f, 13.87f)
                arcToRelative(0.98f, 0.98f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.57f, 0.78f)
                lineToRelative(14.4f, -8.32f)
                arcToRelative(2.48f, 2.48f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.14f, -2.36f)
                close()
            }
            path(fill = SolidColor(Color(0xFFEBEBEB))) {
                moveToRelative(148.7f, 190.87f)
                lineToRelative(-1.98f, -13.87f)
                arcToRelative(0.98f, 0.98f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.57f, -0.78f)
                lineTo(130.69f, 184.55f)
                arcToRelative(2.48f, 2.48f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.14f, 2.35f)
                lineToRelative(1.98f, 13.86f)
                arcToRelative(0.98f, 0.98f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.57f, 0.78f)
                lineToRelative(14.4f, -8.31f)
                arcToRelative(2.5f, 2.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.2f, -2.36f)
                close()
            }
            path(fill = SolidColor(Color(0xFFEBEBEB))) {
                moveTo(170.95f, 178.03f)
                lineTo(168.97f, 164.13f)
                arcToRelative(0.98f, 0.98f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.57f, -0.78f)
                lineToRelative(-14.4f, 8.32f)
                arcToRelative(2.48f, 2.48f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.14f, 2.35f)
                lineToRelative(1.98f, 13.86f)
                arcToRelative(0.98f, 0.98f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.57f, 0.78f)
                lineTo(169.84f, 180.37f)
                arcToRelative(2.5f, 2.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.1f, -2.33f)
                close()
            }
            path(fill = SolidColor(Color(0xFFEBEBEB))) {
                moveToRelative(193.22f, 165.19f)
                lineToRelative(-1.98f, -13.87f)
                arcToRelative(0.98f, 0.98f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.57f, -0.78f)
                lineToRelative(-14.4f, 8.32f)
                arcToRelative(2.48f, 2.48f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.14f, 2.35f)
                lineTo(176.11f, 175.09f)
                arcToRelative(0.98f, 0.98f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.57f, 0.78f)
                lineToRelative(14.4f, -8.32f)
                arcToRelative(2.49f, 2.49f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.14f, -2.37f)
                close()
            }
            path(fill = SolidColor(Color(0xFFEBEBEB))) {
                moveToRelative(215.49f, 152.39f)
                lineToRelative(-1.98f, -13.87f)
                arcToRelative(0.98f, 0.98f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.57f, -0.78f)
                lineToRelative(-14.4f, 8.32f)
                arcToRelative(2.49f, 2.49f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.14f, 2.35f)
                lineToRelative(1.97f, 13.87f)
                arcToRelative(0.99f, 0.99f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.57f, 0.78f)
                lineTo(214.34f, 154.73f)
                arcToRelative(2.49f, 2.49f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.14f, -2.35f)
                close()
            }
            path(fill = SolidColor(primaryColor)) {
                moveTo(78f, 202.26f)
                lineTo(76.01f, 188.41f)
                arcToRelative(0.99f, 0.99f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.57f, -0.78f)
                lineToRelative(-14.4f, 8.32f)
                arcToRelative(2.49f, 2.49f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.14f, 2.35f)
                lineToRelative(1.97f, 13.87f)
                arcToRelative(0.99f, 0.99f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.57f, 0.78f)
                lineToRelative(14.4f, -8.32f)
                arcToRelative(2.52f, 2.52f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.14f, -2.36f)
                close()
            }
            path(fill = SolidColor(Color(0xFFEBEBEB))) {
                moveToRelative(100.27f, 189.42f)
                lineToRelative(-1.98f, -13.87f)
                arcToRelative(0.99f, 0.99f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.57f, -0.78f)
                lineToRelative(-14.4f, 8.32f)
                arcToRelative(2.49f, 2.49f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.14f, 2.35f)
                lineToRelative(1.97f, 13.87f)
                arcToRelative(0.99f, 0.99f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.57f, 0.78f)
                lineToRelative(14.4f, -8.32f)
                arcToRelative(2.52f, 2.52f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.14f, -2.35f)
                close()
            }
            path(fill = SolidColor(Color(0xFFEBEBEB))) {
                moveToRelative(122.54f, 176.57f)
                lineToRelative(-1.98f, -13.87f)
                arcToRelative(0.99f, 0.99f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.57f, -0.78f)
                lineToRelative(-14.4f, 8.32f)
                arcToRelative(2.49f, 2.49f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.14f, 2.35f)
                lineToRelative(1.97f, 13.87f)
                arcToRelative(0.99f, 0.99f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.57f, 0.78f)
                lineToRelative(14.4f, -8.32f)
                arcToRelative(2.52f, 2.52f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.14f, -2.35f)
                close()
            }
            path(fill = SolidColor(Color(0xFFEBEBEB))) {
                moveToRelative(144.78f, 163.73f)
                lineToRelative(-1.98f, -13.87f)
                arcToRelative(0.99f, 0.99f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.57f, -0.78f)
                lineToRelative(-14.39f, 8.33f)
                arcToRelative(2.5f, 2.5f, 135f, isMoreThanHalf = false, isPositiveArc = false, -1.14f, 2.35f)
                lineToRelative(1.97f, 13.87f)
                arcToRelative(0.99f, 0.99f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.57f, 0.78f)
                lineToRelative(14.4f, -8.32f)
                arcToRelative(2.51f, 2.51f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.13f, -2.36f)
                close()
            }
            path(fill = SolidColor(Color(0xFFEBEBEB))) {
                moveToRelative(167.08f, 150.89f)
                lineToRelative(-1.98f, -13.86f)
                arcToRelative(0.98f, 0.98f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.57f, -0.78f)
                lineTo(149.12f, 144.55f)
                arcToRelative(2.5f, 2.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.14f, 2.35f)
                lineToRelative(1.97f, 13.87f)
                arcToRelative(0.99f, 0.99f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.57f, 0.78f)
                lineTo(165.93f, 153.22f)
                arcToRelative(2.51f, 2.51f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.15f, -2.33f)
                close()
            }
            path(fill = SolidColor(Color(0xFFEBEBEB))) {
                moveTo(189.35f, 138.05f)
                lineTo(187.37f, 124.19f)
                arcToRelative(0.98f, 0.98f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.57f, -0.78f)
                lineTo(171.41f, 131.74f)
                arcToRelative(2.48f, 2.48f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.14f, 2.35f)
                lineToRelative(1.97f, 13.86f)
                arcToRelative(0.99f, 0.99f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.57f, 0.78f)
                lineToRelative(14.4f, -8.32f)
                arcToRelative(2.51f, 2.51f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.14f, -2.36f)
                close()
            }
            path(fill = SolidColor(Color(0xFFEBEBEB))) {
                moveToRelative(211.62f, 125.21f)
                lineToRelative(-1.98f, -13.87f)
                arcToRelative(0.98f, 0.98f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.57f, -0.78f)
                lineToRelative(-14.41f, 8.32f)
                arcToRelative(2.49f, 2.49f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.13f, 2.35f)
                lineToRelative(1.97f, 13.86f)
                arcToRelative(0.99f, 0.99f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.57f, 0.78f)
                lineToRelative(14.4f, -8.31f)
                arcToRelative(2.53f, 2.53f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.14f, -2.35f)
                close()
            }
            path(fill = SolidColor(primaryColor)) {
                moveToRelative(74.12f, 175.09f)
                lineToRelative(-1.97f, -13.87f)
                arcToRelative(0.99f, 0.99f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.57f, -0.78f)
                lineTo(56.16f, 168.83f)
                arcToRelative(2.51f, 2.51f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.14f, 2.35f)
                lineToRelative(1.98f, 13.86f)
                arcToRelative(0.98f, 0.98f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.57f, 0.78f)
                lineToRelative(14.41f, -8.38f)
                arcToRelative(2.5f, 2.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.14f, -2.35f)
                close()
            }
            path(fill = SolidColor(Color(0xFFEBEBEB))) {
                moveToRelative(96.39f, 162.28f)
                lineToRelative(-1.97f, -13.87f)
                arcToRelative(0.99f, 0.99f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.57f, -0.78f)
                lineToRelative(-14.4f, 8.32f)
                arcToRelative(2.51f, 2.51f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.14f, 2.35f)
                lineToRelative(1.98f, 13.87f)
                arcToRelative(0.99f, 0.99f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.57f, 0.78f)
                lineToRelative(14.4f, -8.32f)
                arcToRelative(2.49f, 2.49f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.13f, -2.35f)
                close()
            }
            path(fill = SolidColor(Color(0xFFEBEBEB))) {
                moveToRelative(118.66f, 149.44f)
                lineToRelative(-1.97f, -13.87f)
                arcToRelative(0.99f, 0.99f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.57f, -0.78f)
                lineToRelative(-14.4f, 8.32f)
                arcToRelative(2.52f, 2.52f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.14f, 2.35f)
                lineToRelative(1.98f, 13.87f)
                arcToRelative(0.99f, 0.99f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.57f, 0.78f)
                lineToRelative(14.4f, -8.32f)
                arcToRelative(2.49f, 2.49f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.13f, -2.35f)
                close()
            }
            path(fill = SolidColor(Color(0xFFEBEBEB))) {
                moveToRelative(140.93f, 136.6f)
                lineToRelative(-1.97f, -13.87f)
                arcToRelative(0.99f, 0.99f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.57f, -0.78f)
                lineToRelative(-14.4f, 8.32f)
                arcToRelative(2.52f, 2.52f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.14f, 2.35f)
                lineToRelative(1.98f, 13.87f)
                arcToRelative(0.99f, 0.99f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.57f, 0.78f)
                lineToRelative(14.4f, -8.32f)
                arcToRelative(2.49f, 2.49f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.13f, -2.35f)
                close()
            }
            path(fill = SolidColor(Color(0xFFEBEBEB))) {
                moveTo(163.2f, 123.76f)
                lineTo(161.23f, 109.89f)
                arcToRelative(0.99f, 0.99f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.57f, -0.78f)
                lineToRelative(-14.4f, 8.32f)
                arcToRelative(2.52f, 2.52f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.14f, 2.35f)
                lineToRelative(1.98f, 13.87f)
                arcToRelative(0.99f, 0.99f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.57f, 0.78f)
                lineToRelative(14.4f, -8.32f)
                arcToRelative(2.49f, 2.49f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.13f, -2.35f)
                close()
            }
            path(fill = SolidColor(Color(0xFFEBEBEB))) {
                moveToRelative(185.5f, 110.88f)
                lineToRelative(-1.97f, -13.87f)
                arcToRelative(0.99f, 0.99f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.57f, -0.78f)
                lineTo(167.49f, 104.62f)
                arcToRelative(2.52f, 2.52f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.14f, 2.35f)
                lineToRelative(1.98f, 13.87f)
                arcToRelative(0.99f, 0.99f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.57f, 0.78f)
                lineTo(184.34f, 113.23f)
                arcToRelative(2.48f, 2.48f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.17f, -2.35f)
                close()
            }
            path(fill = SolidColor(Color(0xFFEBEBEB))) {
                moveToRelative(207.74f, 98.07f)
                lineToRelative(-1.97f, -13.86f)
                arcToRelative(0.99f, 0.99f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.57f, -0.78f)
                lineToRelative(-14.4f, 8.31f)
                arcToRelative(2.51f, 2.51f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.14f, 2.35f)
                lineToRelative(1.98f, 13.87f)
                arcToRelative(0.99f, 0.99f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.57f, 0.78f)
                lineTo(206.65f, 100.4f)
                arcToRelative(2.48f, 2.48f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.1f, -2.33f)
                close()
            }
            path(fill = SolidColor(primaryColor)) {
                moveToRelative(70.25f, 147.98f)
                lineToRelative(-1.98f, -13.86f)
                arcToRelative(0.98f, 0.98f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.57f, -0.78f)
                lineToRelative(-14.4f, 8.31f)
                arcToRelative(2.5f, 2.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.14f, 2.35f)
                lineTo(53.16f, 157.87f)
                arcToRelative(0.98f, 0.98f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.57f, 0.78f)
                lineToRelative(14.4f, -8.32f)
                arcToRelative(2.48f, 2.48f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.12f, -2.35f)
                close()
            }
            path(fill = SolidColor(Color(0xFFEBEBEB))) {
                moveToRelative(92.52f, 135.16f)
                lineToRelative(-1.98f, -13.86f)
                arcToRelative(0.98f, 0.98f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.57f, -0.78f)
                lineToRelative(-14.4f, 8.32f)
                arcToRelative(2.48f, 2.48f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.14f, 2.35f)
                lineToRelative(1.98f, 13.86f)
                arcToRelative(0.98f, 0.98f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.57f, 0.78f)
                lineTo(91.39f, 137.51f)
                arcToRelative(2.48f, 2.48f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.14f, -2.35f)
                close()
            }
            path(fill = SolidColor(Color(0xFFEBEBEB))) {
                moveToRelative(114.79f, 122.31f)
                lineToRelative(-1.98f, -13.87f)
                arcToRelative(0.98f, 0.98f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.57f, -0.78f)
                lineToRelative(-14.4f, 8.32f)
                arcToRelative(2.48f, 2.48f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.14f, 2.35f)
                lineToRelative(1.98f, 13.86f)
                arcToRelative(0.98f, 0.98f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.57f, 0.78f)
                lineToRelative(14.4f, -8.31f)
                arcToRelative(2.5f, 2.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.14f, -2.35f)
                close()
            }
            path(fill = SolidColor(Color(0xFFEBEBEB))) {
                moveToRelative(137.06f, 109.47f)
                lineToRelative(-1.98f, -13.87f)
                arcToRelative(0.98f, 0.98f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.57f, -0.78f)
                lineToRelative(-14.4f, 8.32f)
                arcToRelative(2.48f, 2.48f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.14f, 2.35f)
                lineToRelative(1.97f, 13.87f)
                arcToRelative(0.99f, 0.99f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.57f, 0.78f)
                lineToRelative(14.4f, -8.32f)
                arcToRelative(2.5f, 2.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.14f, -2.35f)
                close()
            }
            path(fill = SolidColor(Color(0xFFEBEBEB))) {
                moveToRelative(159.33f, 96.62f)
                lineToRelative(-1.98f, -13.87f)
                arcToRelative(0.98f, 0.98f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.57f, -0.78f)
                lineToRelative(-14.4f, 8.32f)
                arcToRelative(2.48f, 2.48f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.14f, 2.35f)
                lineToRelative(1.97f, 13.87f)
                arcToRelative(0.99f, 0.99f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.57f, 0.78f)
                lineToRelative(14.4f, -8.32f)
                arcToRelative(2.49f, 2.49f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.14f, -2.35f)
                close()
            }
            path(fill = SolidColor(Color(0xFFEBEBEB))) {
                moveToRelative(181.59f, 83.78f)
                lineToRelative(-1.98f, -13.87f)
                arcToRelative(0.98f, 0.98f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.57f, -0.78f)
                lineToRelative(-14.4f, 8.32f)
                arcToRelative(2.49f, 2.49f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.14f, 2.35f)
                lineToRelative(1.98f, 13.86f)
                arcToRelative(0.99f, 0.99f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.57f, 0.78f)
                lineToRelative(14.4f, -8.32f)
                arcToRelative(2.49f, 2.49f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.14f, -2.34f)
                close()
            }
            path(fill = SolidColor(Color(0xFFEBEBEB))) {
                moveToRelative(203.88f, 70.95f)
                lineToRelative(-1.98f, -13.87f)
                arcToRelative(0.98f, 0.98f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.57f, -0.78f)
                lineToRelative(-14.4f, 8.32f)
                arcToRelative(2.49f, 2.49f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.14f, 2.35f)
                lineToRelative(1.97f, 13.87f)
                arcToRelative(0.99f, 0.99f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.57f, 0.78f)
                lineTo(202.73f, 73.29f)
                arcToRelative(2.49f, 2.49f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.14f, -2.35f)
                close()
            }
            path(fill = SolidColor(Color(0xFFE6E6E6))) {
                moveToRelative(66.83f, 123.47f)
                lineToRelative(-0.5f, -3.54f)
                arcToRelative(0.98f, 0.98f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.57f, -0.78f)
                lineToRelative(-14.41f, 8.33f)
                arcToRelative(2.49f, 2.49f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.14f, 2.35f)
                lineToRelative(0.49f, 3.54f)
                arcToRelative(0.99f, 0.99f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.57f, 0.78f)
                lineTo(65.69f, 125.81f)
                arcToRelative(2.51f, 2.51f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.14f, -2.33f)
                close()
            }
            path(fill = SolidColor(Color(0xFFE6E6E6))) {
                moveToRelative(89.1f, 110.63f)
                lineToRelative(-0.5f, -3.54f)
                arcToRelative(0.98f, 0.98f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.57f, -0.78f)
                lineToRelative(-14.41f, 8.33f)
                arcToRelative(2.49f, 2.49f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.14f, 2.35f)
                lineToRelative(0.49f, 3.54f)
                arcToRelative(0.99f, 0.99f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.57f, 0.78f)
                lineToRelative(14.41f, -8.32f)
                arcToRelative(2.51f, 2.51f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.14f, -2.36f)
                close()
            }
            path(fill = SolidColor(Color(0xFFE6E6E6))) {
                moveToRelative(111.37f, 97.79f)
                lineToRelative(-0.5f, -3.54f)
                arcToRelative(0.99f, 0.99f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.57f, -0.78f)
                lineToRelative(-14.41f, 8.33f)
                arcToRelative(2.49f, 2.49f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.14f, 2.35f)
                lineToRelative(0.49f, 3.54f)
                arcToRelative(0.98f, 0.98f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.57f, 0.78f)
                lineToRelative(14.41f, -8.32f)
                arcToRelative(2.51f, 2.51f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.14f, -2.36f)
                close()
            }
            path(fill = SolidColor(Color(0xFFE6E6E6))) {
                moveToRelative(133.64f, 84.95f)
                lineToRelative(-0.5f, -3.55f)
                arcToRelative(0.99f, 0.99f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.57f, -0.78f)
                lineTo(117.16f, 88.96f)
                arcToRelative(2.49f, 2.49f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.14f, 2.35f)
                lineToRelative(0.49f, 3.54f)
                arcToRelative(0.98f, 0.98f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.57f, 0.78f)
                lineToRelative(14.41f, -8.31f)
                arcToRelative(2.53f, 2.53f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.14f, -2.36f)
                close()
            }
            path(fill = SolidColor(Color(0xFFE6E6E6))) {
                moveToRelative(155.91f, 72.11f)
                lineToRelative(-0.5f, -3.55f)
                arcToRelative(0.99f, 0.99f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.57f, -0.78f)
                lineToRelative(-14.41f, 8.33f)
                arcToRelative(2.5f, 2.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.14f, 2.35f)
                lineToRelative(0.49f, 3.54f)
                arcToRelative(0.98f, 0.98f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.57f, 0.78f)
                lineToRelative(14.41f, -8.31f)
                arcToRelative(2.53f, 2.53f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.14f, -2.36f)
                close()
            }
            path(fill = SolidColor(Color(0xFFE6E6E6))) {
                moveToRelative(178.18f, 59.27f)
                lineToRelative(-0.5f, -3.54f)
                arcToRelative(0.98f, 0.98f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.57f, -0.78f)
                lineToRelative(-14.41f, 8.32f)
                arcToRelative(2.5f, 2.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.14f, 2.35f)
                lineToRelative(0.49f, 3.55f)
                arcToRelative(0.99f, 0.99f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.57f, 0.78f)
                lineToRelative(14.41f, -8.32f)
                arcToRelative(2.52f, 2.52f, 135f, isMoreThanHalf = false, isPositiveArc = false, 1.14f, -2.36f)
                close()
            }
            path(fill = SolidColor(Color(0xFFE6E6E6))) {
                moveToRelative(200.45f, 46.43f)
                lineToRelative(-0.49f, -3.54f)
                arcToRelative(0.98f, 0.98f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.57f, -0.78f)
                lineToRelative(-14.4f, 8.32f)
                arcToRelative(2.48f, 2.48f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.14f, 2.35f)
                lineToRelative(0.49f, 3.54f)
                arcToRelative(0.99f, 0.99f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.57f, 0.78f)
                lineToRelative(14.41f, -8.32f)
                arcToRelative(2.5f, 2.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.13f, -2.35f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 0.2f,
                strokeAlpha = 0.2f
            ) {
                moveToRelative(50.03f, 102.57f)
                arcToRelative(5.69f, 5.69f, 135f, isMoreThanHalf = false, isPositiveArc = true, 2.69f, -5.23f)
                curveToRelative(1.75f, -1.01f, 3.39f, -0.31f, 3.66f, 1.57f)
                arcToRelative(5.66f, 5.66f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2.7f, 5.23f)
                curveToRelative(-1.75f, 1.01f, -3.38f, 0.31f, -3.66f, -1.57f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 0.2f,
                strokeAlpha = 0.2f
            ) {
                moveToRelative(72.33f, 89.74f)
                arcToRelative(5.67f, 5.67f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2.69f, -5.23f)
                curveToRelative(1.75f, -1.02f, 3.39f, -0.32f, 3.66f, 1.57f)
                arcToRelative(5.65f, 5.65f, 135f, isMoreThanHalf = false, isPositiveArc = true, -2.7f, 5.22f)
                curveToRelative(-1.75f, 0.98f, -3.39f, 0.28f, -3.66f, -1.56f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 0.2f,
                strokeAlpha = 0.2f
            ) {
                moveToRelative(94.57f, 76.86f)
                arcToRelative(5.67f, 5.67f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2.69f, -5.23f)
                curveToRelative(1.75f, -1.02f, 3.4f, -0.32f, 3.66f, 1.57f)
                arcToRelative(5.66f, 5.66f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2.69f, 5.22f)
                curveToRelative(-1.75f, 1.01f, -3.4f, 0.31f, -3.66f, -1.56f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 0.2f,
                strokeAlpha = 0.2f
            ) {
                moveToRelative(139.1f, 51.15f)
                arcToRelative(5.67f, 5.67f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2.69f, -5.23f)
                curveToRelative(1.75f, -1.01f, 3.39f, -0.31f, 3.66f, 1.57f)
                arcToRelative(5.64f, 5.64f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2.69f, 5.23f)
                curveToRelative(-1.75f, 1f, -3.39f, 0.31f, -3.66f, -1.57f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 0.2f,
                strokeAlpha = 0.2f
            ) {
                moveToRelative(161.39f, 38.29f)
                arcToRelative(5.65f, 5.65f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2.69f, -5.23f)
                curveToRelative(1.75f, -1.02f, 3.39f, -0.32f, 3.66f, 1.57f)
                arcToRelative(5.66f, 5.66f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2.7f, 5.23f)
                curveToRelative(-1.75f, 1f, -3.39f, 0.31f, -3.66f, -1.57f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 0.2f,
                strokeAlpha = 0.2f
            ) {
                moveToRelative(183.63f, 25.44f)
                arcToRelative(5.67f, 5.67f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2.69f, -5.23f)
                curveToRelative(1.75f, -1.02f, 3.4f, -0.32f, 3.66f, 1.57f)
                arcToRelative(5.66f, 5.66f, 135f, isMoreThanHalf = false, isPositiveArc = true, -2.69f, 5.22f)
                curveToRelative(-1.79f, 1.01f, -3.36f, 0.31f, -3.66f, -1.56f)
                close()
            }
            path(fill = SolidColor(Color(0xFFF5F5F5))) {
                moveToRelative(47.73f, 81.41f)
                curveToRelative(-2.35f, -1.37f, -4.65f, -1.57f, -6.41f, -0.56f)
                curveToRelative(-1.75f, 1.01f, -2.73f, 3.08f, -2.73f, 5.83f)
                arcToRelative(14.21f, 14.21f, 45f, isMoreThanHalf = false, isPositiveArc = false, 0.56f, 3.8f)
                lineToRelative(1.39f, -0.78f)
                arcToRelative(12.62f, 12.62f, 45f, isMoreThanHalf = false, isPositiveArc = true, -0.39f, -3f)
                curveToRelative(0f, -2.16f, 0.69f, -3.75f, 1.94f, -4.47f)
                arcToRelative(3.34f, 3.34f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.68f, -0.42f)
                arcToRelative(6.5f, 6.5f, 45f, isMoreThanHalf = false, isPositiveArc = true, 3.13f, 0.98f)
                curveToRelative(4.17f, 2.41f, 7.56f, 8.28f, 7.56f, 13.09f)
                arcToRelative(8.54f, 8.54f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.05f, 1.03f)
                arcToRelative(4.32f, 4.32f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1.88f, 3.45f)
                arcToRelative(3.6f, 3.6f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2.35f, 0.38f)
                arcToRelative(4.45f, 4.45f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.2f, 1.57f)
                arcToRelative(5.69f, 5.69f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.88f, 0.07f)
                arcToRelative(4.8f, 4.8f, 0f, isMoreThanHalf = false, isPositiveArc = false, 2.46f, -0.64f)
                arcToRelative(5.55f, 5.55f, 0f, isMoreThanHalf = false, isPositiveArc = false, 2.56f, -4.03f)
                arcToRelative(10.18f, 10.18f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.16f, -1.8f)
                curveToRelative(0.01f, -5.36f, -3.74f, -11.82f, -8.33f, -14.48f)
                close()
            }
            path(fill = SolidColor(Color(0xFFF5F5F5))) {
                moveToRelative(70.02f, 68.55f)
                curveToRelative(-2.35f, -1.37f, -4.65f, -1.57f, -6.41f, -0.56f)
                curveToRelative(-1.75f, 1.01f, -2.73f, 3.08f, -2.73f, 5.83f)
                arcToRelative(14.21f, 14.21f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.56f, 3.8f)
                lineToRelative(1.39f, -0.78f)
                arcToRelative(12.62f, 12.62f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.39f, -3f)
                curveToRelative(0f, -2.16f, 0.69f, -3.75f, 1.94f, -4.47f)
                arcToRelative(3.34f, 3.34f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.68f, -0.42f)
                arcToRelative(6.5f, 6.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 3.13f, 0.98f)
                curveToRelative(4.17f, 2.41f, 7.56f, 8.28f, 7.56f, 13.09f)
                arcToRelative(8.54f, 8.54f, 135f, isMoreThanHalf = false, isPositiveArc = true, -0.05f, 1.03f)
                arcToRelative(4.32f, 4.32f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1.88f, 3.45f)
                arcToRelative(3.6f, 3.6f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2.35f, 0.38f)
                arcToRelative(4.45f, 4.45f, 135f, isMoreThanHalf = false, isPositiveArc = false, -0.2f, 1.57f)
                arcToRelative(5.69f, 5.69f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.88f, 0.07f)
                arcToRelative(4.8f, 4.8f, 0f, isMoreThanHalf = false, isPositiveArc = false, 2.46f, -0.64f)
                arcToRelative(5.55f, 5.55f, 0f, isMoreThanHalf = false, isPositiveArc = false, 2.56f, -4.03f)
                arcToRelative(10.18f, 10.18f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.16f, -1.8f)
                curveToRelative(0.01f, -5.34f, -3.74f, -11.82f, -8.33f, -14.48f)
                close()
            }
            path(fill = SolidColor(Color(0xFFF5F5F5))) {
                moveToRelative(92.26f, 55.7f)
                curveToRelative(-2.35f, -1.37f, -4.65f, -1.57f, -6.41f, -0.56f)
                curveToRelative(-1.75f, 1.01f, -2.73f, 3.08f, -2.73f, 5.83f)
                arcToRelative(14.21f, 14.21f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.56f, 3.8f)
                lineToRelative(1.39f, -0.78f)
                arcToRelative(12.62f, 12.62f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.39f, -3f)
                curveToRelative(0f, -2.16f, 0.69f, -3.75f, 1.94f, -4.47f)
                arcToRelative(3.34f, 3.34f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.68f, -0.42f)
                arcToRelative(6.5f, 6.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 3.13f, 0.98f)
                curveToRelative(4.17f, 2.41f, 7.56f, 8.28f, 7.56f, 13.09f)
                arcToRelative(8.54f, 8.54f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.05f, 1.03f)
                arcToRelative(4.32f, 4.32f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1.88f, 3.45f)
                arcToRelative(3.6f, 3.6f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2.35f, 0.38f)
                arcToRelative(4.45f, 4.45f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.2f, 1.57f)
                arcToRelative(5.69f, 5.69f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.88f, 0.07f)
                arcToRelative(4.8f, 4.8f, 0f, isMoreThanHalf = false, isPositiveArc = false, 2.46f, -0.64f)
                arcToRelative(5.55f, 5.55f, 0f, isMoreThanHalf = false, isPositiveArc = false, 2.56f, -4.03f)
                arcToRelative(10.18f, 10.18f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.16f, -1.8f)
                curveToRelative(0.01f, -5.34f, -3.74f, -11.82f, -8.33f, -14.48f)
                close()
            }
            path(fill = SolidColor(Color(0xFFF5F5F5))) {
                moveToRelative(136.8f, 30.03f)
                curveToRelative(-2.35f, -1.37f, -4.65f, -1.57f, -6.41f, -0.56f)
                curveToRelative(-1.75f, 1.01f, -2.73f, 3.08f, -2.73f, 5.83f)
                arcToRelative(14.21f, 14.21f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.56f, 3.8f)
                lineToRelative(1.39f, -0.78f)
                arcToRelative(12.62f, 12.62f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.39f, -3f)
                curveToRelative(0f, -2.16f, 0.69f, -3.75f, 1.94f, -4.47f)
                arcToRelative(3.34f, 3.34f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.68f, -0.42f)
                arcToRelative(6.5f, 6.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 3.13f, 0.98f)
                curveToRelative(4.17f, 2.41f, 7.56f, 8.28f, 7.56f, 13.09f)
                arcToRelative(8.54f, 8.54f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.05f, 1.03f)
                arcToRelative(4.32f, 4.32f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1.88f, 3.45f)
                arcToRelative(3.6f, 3.6f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2.35f, 0.38f)
                arcToRelative(4.45f, 4.45f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.2f, 1.57f)
                arcToRelative(5.69f, 5.69f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.88f, 0.07f)
                arcToRelative(4.8f, 4.8f, 0f, isMoreThanHalf = false, isPositiveArc = false, 2.46f, -0.64f)
                arcToRelative(5.55f, 5.55f, 0f, isMoreThanHalf = false, isPositiveArc = false, 2.56f, -4.03f)
                arcToRelative(10.18f, 10.18f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.16f, -1.8f)
                curveToRelative(0.01f, -5.34f, -3.74f, -11.82f, -8.33f, -14.48f)
                close()
            }
            path(fill = SolidColor(Color(0xFFF5F5F5))) {
                moveToRelative(159.08f, 17.16f)
                curveToRelative(-2.35f, -1.37f, -4.65f, -1.57f, -6.41f, -0.56f)
                curveToRelative(-1.75f, 1.01f, -2.73f, 3.08f, -2.73f, 5.83f)
                arcToRelative(14.21f, 14.21f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.56f, 3.8f)
                lineToRelative(1.39f, -0.78f)
                arcToRelative(12.62f, 12.62f, 135f, isMoreThanHalf = false, isPositiveArc = true, -0.39f, -3f)
                curveToRelative(0f, -2.16f, 0.69f, -3.75f, 1.94f, -4.47f)
                arcToRelative(3.34f, 3.34f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.68f, -0.42f)
                arcToRelative(6.5f, 6.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 3.13f, 0.98f)
                curveToRelative(4.17f, 2.41f, 7.56f, 8.28f, 7.56f, 13.09f)
                arcToRelative(8.54f, 8.54f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.05f, 1.03f)
                arcToRelative(4.32f, 4.32f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1.88f, 3.45f)
                arcToRelative(3.6f, 3.6f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2.35f, 0.38f)
                arcToRelative(4.45f, 4.45f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.2f, 1.57f)
                arcToRelative(5.69f, 5.69f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.88f, 0.07f)
                arcToRelative(4.8f, 4.8f, 0f, isMoreThanHalf = false, isPositiveArc = false, 2.46f, -0.64f)
                arcToRelative(5.55f, 5.55f, 0f, isMoreThanHalf = false, isPositiveArc = false, 2.56f, -4.03f)
                arcToRelative(10.18f, 10.18f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.16f, -1.8f)
                curveTo(167.42f, 26.31f, 163.68f, 19.82f, 159.08f, 17.16f)
                close()
            }
            path(fill = SolidColor(Color(0xFFF5F5F5))) {
                moveToRelative(181.33f, 4.31f)
                curveToRelative(-2.35f, -1.37f, -4.65f, -1.57f, -6.41f, -0.56f)
                curveToRelative(-1.75f, 1.01f, -2.73f, 3.08f, -2.73f, 5.83f)
                arcToRelative(14.21f, 14.21f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.56f, 3.8f)
                lineToRelative(1.39f, -0.78f)
                arcTo(12.62f, 12.62f, 135f, isMoreThanHalf = false, isPositiveArc = true, 173.76f, 9.6f)
                curveToRelative(0f, -2.16f, 0.69f, -3.75f, 1.94f, -4.47f)
                arcToRelative(3.34f, 3.34f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.68f, -0.42f)
                arcToRelative(6.5f, 6.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 3.13f, 0.98f)
                curveToRelative(4.17f, 2.41f, 7.56f, 8.28f, 7.56f, 13.09f)
                arcToRelative(8.54f, 8.54f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.05f, 1.03f)
                arcToRelative(4.32f, 4.32f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1.88f, 3.45f)
                arcToRelative(3.6f, 3.6f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2.35f, 0.38f)
                arcToRelative(4.45f, 4.45f, 45f, isMoreThanHalf = false, isPositiveArc = false, -0.2f, 1.57f)
                arcToRelative(5.69f, 5.69f, 135f, isMoreThanHalf = false, isPositiveArc = false, 0.88f, 0.07f)
                arcToRelative(4.8f, 4.8f, 0f, isMoreThanHalf = false, isPositiveArc = false, 2.46f, -0.64f)
                arcToRelative(5.55f, 5.55f, 45f, isMoreThanHalf = false, isPositiveArc = false, 2.56f, -4.03f)
                arcToRelative(10.18f, 10.18f, 67.99f, isMoreThanHalf = false, isPositiveArc = false, 0.16f, -1.8f)
                curveToRelative(0.02f, -5.34f, -3.73f, -11.82f, -8.32f, -14.48f)
                close()
            }
        }.build()

        return _IllCalendar!!
    }

@Suppress("ObjectPropertyName")
private var _IllCalendar: ImageVector? = null
