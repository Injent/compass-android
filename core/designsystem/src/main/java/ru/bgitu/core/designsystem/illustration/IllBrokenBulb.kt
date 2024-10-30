package ru.bgitu.core.designsystem.illustration

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.bgitu.core.designsystem.theme.LocalNewColorScheme

val AppIllustrations.BrokenBulb: ImageVector
    @Composable get() {
        if (_BrokenBulb != null) {
            return _BrokenBulb!!
        }
        
        val primaryColor = LocalNewColorScheme.current.foreground
        
        _BrokenBulb = ImageVector.Builder(
            name = "BrokenBulb",
            defaultWidth = 327.24.dp,
            defaultHeight = 327.24.dp,
            viewportWidth = 327.24f,
            viewportHeight = 327.24f
        ).apply {
            path(fill = SolidColor(primaryColor)) {
                moveToRelative(318.22f, 74.33f)
                curveToRelative(1.15f, 16.37f, -5.57f, 40.88f, -5.57f, 40.88f)
                lineToRelative(-27.23f, -11.21f)
                lineToRelative(-13.82f, 34.92f)
                lineToRelative(-34.24f, -12.59f)
                lineToRelative(-3.84f, 7.65f)
                lineToRelative(-5.13f, -1.48f)
                lineToRelative(-6.49f, 21.55f)
                lineToRelative(-27.5f, -7.72f)
                curveToRelative(0f, 0f, 11.24f, 21.09f, 35.61f, 30.66f)
                arcToRelative(77.34f, 77.34f, 0f, isMoreThanHalf = false, isPositiveArc = false, 97f, -64.53f)
                curveToRelative(1.07f, -8.73f, -1.45f, -30.45f, -8.79f, -38.13f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFFFFFFF)),
                fillAlpha = 0.5f,
                strokeAlpha = 0.5f
            ) {
                moveToRelative(318.22f, 74.33f)
                curveToRelative(1.15f, 16.37f, -5.57f, 40.88f, -5.57f, 40.88f)
                lineToRelative(-27.23f, -11.21f)
                lineToRelative(-13.82f, 34.92f)
                lineToRelative(-34.24f, -12.59f)
                lineToRelative(-3.84f, 7.65f)
                lineToRelative(-5.13f, -1.48f)
                lineToRelative(-6.49f, 21.55f)
                lineToRelative(-27.5f, -7.72f)
                curveToRelative(0f, 0f, 11.24f, 21.09f, 35.61f, 30.66f)
                arcToRelative(77.34f, 77.34f, 0f, isMoreThanHalf = false, isPositiveArc = false, 97f, -64.53f)
                curveToRelative(1.07f, -8.73f, -1.45f, -30.45f, -8.79f, -38.13f)
                close()
            }
            path(fill = SolidColor(primaryColor)) {
                moveToRelative(230.01f, 177.02f)
                curveToRelative(-8.2f, -10.6f, -13.06f, -25.13f, -13.83f, -40.2f)
                arcToRelative(173.26f, 173.26f, 0f, isMoreThanHalf = false, isPositiveArc = false, 45.2f, 8f)
                arcToRelative(148.59f, 148.59f, 0f, isMoreThanHalf = false, isPositiveArc = true, 6.53f, -17.24f)
                curveToRelative(11.16f, 4f, 30.65f, 10.21f, 30.65f, 10.21f)
                lineToRelative(3.86f, -5.59f)
                curveToRelative(0f, 0f, 2.46f, 0.47f, 4f, 0.95f)
                curveToRelative(3.91f, -16.76f, 3.81f, -25.45f, 3.77f, -41.58f)
                curveToRelative(8f, 7.32f, 12.32f, 12.64f, 16.82f, 20.9f)
                arcToRelative(77.34f, 77.34f, 0f, isMoreThanHalf = false, isPositiveArc = true, -97f, 64.53f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFFFFFFF)),
                fillAlpha = 0.85f,
                strokeAlpha = 0.85f
            ) {
                moveToRelative(230.01f, 177.02f)
                curveToRelative(-8.2f, -10.6f, -13.06f, -25.13f, -13.83f, -40.2f)
                arcToRelative(173.26f, 173.26f, 0f, isMoreThanHalf = false, isPositiveArc = false, 45.2f, 8f)
                arcToRelative(148.59f, 148.59f, 0f, isMoreThanHalf = false, isPositiveArc = true, 6.53f, -17.24f)
                curveToRelative(11.16f, 4f, 30.65f, 10.21f, 30.65f, 10.21f)
                lineToRelative(3.86f, -5.59f)
                curveToRelative(0f, 0f, 2.46f, 0.47f, 4f, 0.95f)
                curveToRelative(3.91f, -16.76f, 3.81f, -25.45f, 3.77f, -41.58f)
                curveToRelative(8f, 7.32f, 12.32f, 12.64f, 16.82f, 20.9f)
                arcToRelative(77.34f, 77.34f, 0f, isMoreThanHalf = false, isPositiveArc = true, -97f, 64.53f)
                close()
            }
            path(fill = SolidColor(primaryColor)) {
                moveTo(327.01f, 112.49f)
                arcTo(67.75f, 67.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, 316.44f, 97.72f)
                curveToRelative(-1.66f, 9.73f, -3.79f, 17.51f, -3.79f, 17.51f)
                lineToRelative(-3f, -1.24f)
                arcToRelative(134.23f, 134.23f, 0f, isMoreThanHalf = false, isPositiveArc = true, -3.22f, 19.18f)
                curveToRelative(-1.54f, -0.48f, -4f, -0.95f, -4f, -0.95f)
                lineToRelative(-3.86f, 5.59f)
                curveToRelative(0f, 0f, -12.7f, -4f, -23.47f, -7.7f)
                lineToRelative(-3.49f, 8.83f)
                lineToRelative(-7.27f, -2.67f)
                quadToRelative(-1.61f, 4.23f, -2.95f, 8.57f)
                arcToRelative(173.12f, 173.12f, 0f, isMoreThanHalf = false, isPositiveArc = true, -35.14f, -5.16f)
                lineToRelative(-4.34f, 14.4f)
                lineToRelative(-3.21f, -0.9f)
                arcToRelative(66.39f, 66.39f, 0f, isMoreThanHalf = false, isPositiveArc = false, 11.32f, 23.84f)
                arcToRelative(76.82f, 76.82f, 0f, isMoreThanHalf = false, isPositiveArc = false, 38.62f, 0.52f)
                quadToRelative(3.52f, -0.87f, 7f, -2.08f)
                curveToRelative(1.16f, -0.4f, 2.32f, -0.83f, 3.47f, -1.29f)
                quadToRelative(3.47f, -1.39f, 6.84f, -3.13f)
                arcToRelative(76.71f, 76.71f, 0f, isMoreThanHalf = false, isPositiveArc = false, 38.1f, -45.19f)
                curveToRelative(0.23f, -0.73f, 0.45f, -1.45f, 0.65f, -2.19f)
                curveToRelative(0.42f, -1.46f, 0.79f, -2.93f, 1.12f, -4.42f)
                curveToRelative(0.16f, -0.74f, 0.32f, -1.49f, 0.46f, -2.24f)
                curveToRelative(0.29f, -1.49f, 0.53f, -3f, 0.72f, -4.51f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFFFFFFF)),
                fillAlpha = 0.7f,
                strokeAlpha = 0.7f
            ) {
                moveTo(327.01f, 112.49f)
                arcTo(67.75f, 67.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, 316.44f, 97.72f)
                curveToRelative(-1.66f, 9.73f, -3.79f, 17.51f, -3.79f, 17.51f)
                lineToRelative(-3f, -1.24f)
                arcToRelative(134.23f, 134.23f, 0f, isMoreThanHalf = false, isPositiveArc = true, -3.22f, 19.18f)
                curveToRelative(-1.54f, -0.48f, -4f, -0.95f, -4f, -0.95f)
                lineToRelative(-3.86f, 5.59f)
                curveToRelative(0f, 0f, -12.7f, -4f, -23.47f, -7.7f)
                lineToRelative(-3.49f, 8.83f)
                lineToRelative(-7.27f, -2.67f)
                quadToRelative(-1.61f, 4.23f, -2.95f, 8.57f)
                arcToRelative(173.12f, 173.12f, 0f, isMoreThanHalf = false, isPositiveArc = true, -35.14f, -5.16f)
                lineToRelative(-4.34f, 14.4f)
                lineToRelative(-3.21f, -0.9f)
                arcToRelative(66.39f, 66.39f, 0f, isMoreThanHalf = false, isPositiveArc = false, 11.32f, 23.84f)
                arcToRelative(76.82f, 76.82f, 0f, isMoreThanHalf = false, isPositiveArc = false, 38.62f, 0.52f)
                quadToRelative(3.52f, -0.87f, 7f, -2.08f)
                curveToRelative(1.16f, -0.4f, 2.32f, -0.83f, 3.47f, -1.29f)
                quadToRelative(3.47f, -1.39f, 6.84f, -3.13f)
                arcToRelative(76.71f, 76.71f, 0f, isMoreThanHalf = false, isPositiveArc = false, 38.1f, -45.19f)
                curveToRelative(0.23f, -0.73f, 0.45f, -1.45f, 0.65f, -2.19f)
                curveToRelative(0.42f, -1.46f, 0.79f, -2.93f, 1.12f, -4.42f)
                curveToRelative(0.16f, -0.74f, 0.32f, -1.49f, 0.46f, -2.24f)
                curveToRelative(0.29f, -1.49f, 0.53f, -3f, 0.72f, -4.51f)
                close()
            }
            path(fill = SolidColor(primaryColor)) {
                moveToRelative(216.37f, 189.79f)
                arcToRelative(75.37f, 75.37f, 0f, isMoreThanHalf = false, isPositiveArc = true, -36.11f, 49.09f)
                curveToRelative(-52.87f, 30.52f, -88.09f, -7.3f, -109.4f, 5f)
                lineToRelative(-15.31f, 8.84f)
                lineToRelative(-26.53f, -45.95f)
                lineToRelative(15.31f, -8.84f)
                curveToRelative(21.31f, -12.31f, 5.49f, -61.33f, 58.36f, -91.85f)
                arcToRelative(76.48f, 76.48f, 0f, isMoreThanHalf = false, isPositiveArc = true, 44.64f, -10.06f)
                curveToRelative(-3.45f, 10.77f, -3.74f, 19.11f, -4.2f, 25.06f)
                arcToRelative(147.4f, 147.4f, 0f, isMoreThanHalf = false, isPositiveArc = false, 17f, 4.47f)
                quadToRelative(-1.87f, 19.08f, -3.77f, 38.17f)
                curveToRelative(11.1f, 1.91f, 35.51f, 8.29f, 35.51f, 8.29f)
                lineToRelative(-1.54f, 4.59f)
                lineToRelative(4.69f, 1.13f)
                lineToRelative(-0.7f, 12.16f)
                curveToRelative(0f, 0f, 11.98f, 2.38f, 22.05f, -0.1f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFFFFFFF)),
                fillAlpha = 0.5f,
                strokeAlpha = 0.5f
            ) {
                moveToRelative(216.37f, 189.79f)
                arcToRelative(75.37f, 75.37f, 0f, isMoreThanHalf = false, isPositiveArc = true, -36.11f, 49.09f)
                curveToRelative(-52.87f, 30.52f, -88.09f, -7.3f, -109.4f, 5f)
                lineToRelative(-15.31f, 8.84f)
                lineToRelative(-26.53f, -45.95f)
                lineToRelative(15.31f, -8.84f)
                curveToRelative(21.31f, -12.31f, 5.49f, -61.33f, 58.36f, -91.85f)
                arcToRelative(76.48f, 76.48f, 0f, isMoreThanHalf = false, isPositiveArc = true, 44.64f, -10.06f)
                curveToRelative(-3.45f, 10.77f, -3.74f, 19.11f, -4.2f, 25.06f)
                arcToRelative(147.4f, 147.4f, 0f, isMoreThanHalf = false, isPositiveArc = false, 17f, 4.47f)
                quadToRelative(-1.87f, 19.08f, -3.77f, 38.17f)
                curveToRelative(11.1f, 1.91f, 35.51f, 8.29f, 35.51f, 8.29f)
                lineToRelative(-1.54f, 4.59f)
                lineToRelative(4.69f, 1.13f)
                lineToRelative(-0.7f, 12.16f)
                curveToRelative(0f, 0f, 11.98f, 2.38f, 22.05f, -0.1f)
                close()
            }
            path(fill = SolidColor(primaryColor)) {
                moveToRelative(216.37f, 189.79f)
                arcToRelative(75.37f, 75.37f, 0f, isMoreThanHalf = false, isPositiveArc = true, -36.11f, 49.09f)
                curveToRelative(-52.87f, 30.52f, -88.09f, -7.3f, -109.4f, 5f)
                lineToRelative(-15.31f, 8.84f)
                lineToRelative(-26.53f, -45.95f)
                lineToRelative(15.31f, -8.84f)
                curveToRelative(21.31f, -12.31f, 5.49f, -61.33f, 58.36f, -91.85f)
                arcToRelative(76.48f, 76.48f, 0f, isMoreThanHalf = false, isPositiveArc = true, 44.64f, -10.06f)
                curveToRelative(-8f, 4.89f, -13.14f, 9.47f, -20.07f, 17.79f)
                curveToRelative(16.11f, -0.81f, 24.78f, -1.32f, 41.69f, 1.79f)
                curveToRelative(-0.4f, 1.55f, -0.75f, 4f, -0.75f, 4f)
                lineToRelative(5.76f, 3.6f)
                curveToRelative(0f, 0f, -5.23f, 19.74f, -8.73f, 31.08f)
                arcToRelative(149.23f, 149.23f, 0f, isMoreThanHalf = false, isPositiveArc = false, 17.52f, 5.7f)
                arcToRelative(173.06f, 173.06f, 0f, isMoreThanHalf = false, isPositiveArc = true, -5.86f, 45.51f)
                curveToRelative(15.01f, -1.45f, 29.29f, -7f, 39.48f, -15.7f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFFFFFFF)),
                fillAlpha = 0.85f,
                strokeAlpha = 0.85f
            ) {
                moveToRelative(216.37f, 189.79f)
                arcToRelative(75.37f, 75.37f, 0f, isMoreThanHalf = false, isPositiveArc = true, -36.11f, 49.09f)
                curveToRelative(-52.87f, 30.52f, -88.09f, -7.3f, -109.4f, 5f)
                lineToRelative(-15.31f, 8.84f)
                lineToRelative(-26.53f, -45.95f)
                lineToRelative(15.31f, -8.84f)
                curveToRelative(21.31f, -12.31f, 5.49f, -61.33f, 58.36f, -91.85f)
                arcToRelative(76.48f, 76.48f, 0f, isMoreThanHalf = false, isPositiveArc = true, 44.64f, -10.06f)
                curveToRelative(-8f, 4.89f, -13.14f, 9.47f, -20.07f, 17.79f)
                curveToRelative(16.11f, -0.81f, 24.78f, -1.32f, 41.69f, 1.79f)
                curveToRelative(-0.4f, 1.55f, -0.75f, 4f, -0.75f, 4f)
                lineToRelative(5.76f, 3.6f)
                curveToRelative(0f, 0f, -5.23f, 19.74f, -8.73f, 31.08f)
                arcToRelative(149.23f, 149.23f, 0f, isMoreThanHalf = false, isPositiveArc = false, 17.52f, 5.7f)
                arcToRelative(173.06f, 173.06f, 0f, isMoreThanHalf = false, isPositiveArc = true, -5.86f, 45.51f)
                curveToRelative(15.01f, -1.45f, 29.29f, -7f, 39.48f, -15.7f)
                close()
            }
            path(fill = SolidColor(primaryColor)) {
                moveToRelative(176.89f, 205.52f)
                arcToRelative(173.58f, 173.58f, 0f, isMoreThanHalf = false, isPositiveArc = false, 5.64f, -35.89f)
                curveToRelative(-8.1f, -2f, -19.51f, -4.77f, -26.15f, -5.91f)
                quadToRelative(1.89f, -19.08f, 3.77f, -38.17f)
                arcToRelative(147.4f, 147.4f, 0f, isMoreThanHalf = false, isPositiveArc = true, -17f, -4.47f)
                curveToRelative(0.18f, -2.28f, 0.33f, -4.91f, 0.64f, -7.89f)
                curveToRelative(-5f, 0f, -10.17f, 0.3f, -16.51f, 0.62f)
                curveToRelative(6.93f, -8.32f, 12f, -12.9f, 20.07f, -17.79f)
                arcToRelative(76.48f, 76.48f, 0f, isMoreThanHalf = false, isPositiveArc = false, -44.64f, 10.06f)
                curveToRelative(-52.87f, 30.52f, -37f, 79.54f, -58.36f, 91.85f)
                lineToRelative(-15.31f, 8.84f)
                lineToRelative(26.53f, 45.95f)
                lineToRelative(15.27f, -8.84f)
                curveToRelative(21.31f, -12.3f, 56.53f, 25.52f, 109.4f, -5f)
                arcToRelative(75.37f, 75.37f, 0f, isMoreThanHalf = false, isPositiveArc = false, 36.11f, -49.09f)
                curveToRelative(-10.17f, 8.7f, -24.45f, 14.25f, -39.46f, 15.73f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFFFFFFF)),
                fillAlpha = 0.7f,
                strokeAlpha = 0.7f
            ) {
                moveToRelative(176.89f, 205.52f)
                arcToRelative(173.58f, 173.58f, 0f, isMoreThanHalf = false, isPositiveArc = false, 5.64f, -35.89f)
                curveToRelative(-8.1f, -2f, -19.51f, -4.77f, -26.15f, -5.91f)
                quadToRelative(1.89f, -19.08f, 3.77f, -38.17f)
                arcToRelative(147.4f, 147.4f, 0f, isMoreThanHalf = false, isPositiveArc = true, -17f, -4.47f)
                curveToRelative(0.18f, -2.28f, 0.33f, -4.91f, 0.64f, -7.89f)
                curveToRelative(-5f, 0f, -10.17f, 0.3f, -16.51f, 0.62f)
                curveToRelative(6.93f, -8.32f, 12f, -12.9f, 20.07f, -17.79f)
                arcToRelative(76.48f, 76.48f, 0f, isMoreThanHalf = false, isPositiveArc = false, -44.64f, 10.06f)
                curveToRelative(-52.87f, 30.52f, -37f, 79.54f, -58.36f, 91.85f)
                lineToRelative(-15.31f, 8.84f)
                lineToRelative(26.53f, 45.95f)
                lineToRelative(15.27f, -8.84f)
                curveToRelative(21.31f, -12.3f, 56.53f, 25.52f, 109.4f, -5f)
                arcToRelative(75.37f, 75.37f, 0f, isMoreThanHalf = false, isPositiveArc = false, 36.11f, -49.09f)
                curveToRelative(-10.17f, 8.7f, -24.45f, 14.25f, -39.46f, 15.73f)
                close()
            }
            path(
                fill = SolidColor(primaryColor),
                fillAlpha = 0.15f,
                strokeAlpha = 0.15f
            ) {
                moveToRelative(44.33f, 197.93f)
                lineToRelative(-15.31f, 8.84f)
                lineToRelative(5.89f, 10.2f)
                curveToRelative(7.52f, -6.29f, 15.84f, -13.44f, 19.3f, -17.1f)
                curveToRelative(7f, -7.38f, 3.62f, -26.85f, 8.58f, -44.57f)
                curveToRelative(-6.29f, 19.03f, -7.1f, 36.03f, -18.46f, 42.63f)
                close()
            }
            path(
                fill = SolidColor(primaryColor),
                fillAlpha = 0.15f,
                strokeAlpha = 0.15f
            ) {
                moveToRelative(50.24f, 243.52f)
                lineToRelative(5.35f, 9.17f)
                lineToRelative(15.25f, -8.81f)
                curveToRelative(11.17f, -6.45f, 26.17f, 0.87f, 45.52f, 4.91f)
                curveTo(98.84f, 244.17f, 83.84f, 231.94f, 74.12f, 234.33f)
                curveToRelative(-4.89f, 1.19f, -15.22f, 4.87f, -24.41f, 8.28f)
                close()
            }
            path(fill = SolidColor(Color(0xFF455A64))) {
                moveToRelative(39.15f, 224.03f)
                arcToRelative(0.79f, 0.79f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.65f, -0.33f)
                arcToRelative(0.81f, 0.81f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.19f, -1.12f)
                lineToRelative(26.86f, -19f)
                arcToRelative(39.94f, 39.94f, 0f, isMoreThanHalf = false, isPositiveArc = false, 11.53f, -12.7f)
                lineToRelative(14.93f, -26.06f)
                arcToRelative(11.2f, 11.2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 8.93f, -5.61f)
                curveToRelative(4.1f, -0.29f, 7.34f, 0.16f, 9.67f, 1.36f)
                lineToRelative(0.47f, -0.2f)
                curveToRelative(6.51f, -2.5f, 15.53f, -2.63f, 20.77f, 1.76f)
                arcToRelative(23.08f, 23.08f, 0f, isMoreThanHalf = false, isPositiveArc = true, 4.18f, -0.84f)
                arcToRelative(16.55f, 16.55f, 0f, isMoreThanHalf = false, isPositiveArc = true, 6.94f, 0.5f)
                arcToRelative(9.4f, 9.4f, 0f, isMoreThanHalf = false, isPositiveArc = true, 5.54f, 4.46f)
                arcToRelative(0.8f, 0.8f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1.43f, 0.73f)
                arcToRelative(7.77f, 7.77f, 0f, isMoreThanHalf = false, isPositiveArc = false, -4.6f, -3.67f)
                arcToRelative(15.08f, 15.08f, 0f, isMoreThanHalf = false, isPositiveArc = false, -6.27f, -0.43f)
                arcToRelative(21.85f, 21.85f, 0f, isMoreThanHalf = false, isPositiveArc = false, -3f, 0.55f)
                arcToRelative(12.5f, 12.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 3.08f, 8.09f)
                arcToRelative(8.17f, 8.17f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1.45f, 4.81f)
                arcToRelative(7f, 7f, 0f, isMoreThanHalf = false, isPositiveArc = true, -6.53f, 2.59f)
                arcToRelative(7.05f, 7.05f, 0f, isMoreThanHalf = false, isPositiveArc = true, -5.7f, -4.76f)
                arcToRelative(8.29f, 8.29f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2f, -7.56f)
                arcToRelative(14.06f, 14.06f, 0f, isMoreThanHalf = false, isPositiveArc = true, 5.44f, -3.89f)
                curveToRelative(-4.68f, -3.27f, -12.12f, -3.13f, -17.81f, -1.12f)
                curveToRelative(3.65f, 2.92f, 3.87f, 6.44f, 2.91f, 8.63f)
                arcToRelative(5.72f, 5.72f, 0f, isMoreThanHalf = false, isPositiveArc = true, -6.55f, 3.26f)
                arcToRelative(4.57f, 4.57f, 0f, isMoreThanHalf = false, isPositiveArc = true, -3.94f, -4.21f)
                curveToRelative(-0.23f, -2.76f, 1.55f, -5.87f, 4.21f, -7.77f)
                arcToRelative(20f, 20f, 0f, isMoreThanHalf = false, isPositiveArc = false, -7.83f, -0.75f)
                arcToRelative(9.57f, 9.57f, 0f, isMoreThanHalf = false, isPositiveArc = false, -7.65f, 4.81f)
                lineToRelative(-14.93f, 26.06f)
                arcToRelative(41.65f, 41.65f, 0f, isMoreThanHalf = false, isPositiveArc = true, -12f, 13.2f)
                curveToRelative(-6.07f, 4.31f, -15.54f, 11f, -26.85f, 19.05f)
                arcToRelative(0.86f, 0.86f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.46f, 0.16f)
                close()
                moveTo(131.47f, 163.91f)
                arcToRelative(13f, 13f, 0f, isMoreThanHalf = false, isPositiveArc = false, -5.62f, 3.74f)
                curveToRelative(-1.24f, 1.48f, -2.33f, 3.85f, -1.66f, 6.07f)
                arcToRelative(5.46f, 5.46f, 0f, isMoreThanHalf = false, isPositiveArc = false, 4.4f, 3.63f)
                arcToRelative(5.33f, 5.33f, 0f, isMoreThanHalf = false, isPositiveArc = false, 5f, -1.94f)
                arcToRelative(6.73f, 6.73f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.07f, -3.92f)
                arcToRelative(10.91f, 10.91f, 0f, isMoreThanHalf = false, isPositiveArc = false, -3.19f, -7.58f)
                close()
                moveTo(110.55f, 162.33f)
                curveToRelative(-2.81f, 1.58f, -4.47f, 4.62f, -4.28f, 6.81f)
                arcToRelative(3f, 3f, 0f, isMoreThanHalf = false, isPositiveArc = false, 2.66f, 2.77f)
                arcToRelative(4.14f, 4.14f, 0f, isMoreThanHalf = false, isPositiveArc = false, 4.76f, -2.33f)
                curveToRelative(0.81f, -1.83f, 0.47f, -4.66f, -2.89f, -7.08f)
                arcTo(2.2f, 2.2f, 0f, isMoreThanHalf = false, isPositiveArc = false, 110.55f, 162.33f)
                close()
            }
            path(fill = SolidColor(Color(0xFF455A64))) {
                moveToRelative(46.5f, 236.76f)
                arcToRelative(0.82f, 0.82f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.73f, -0.47f)
                arcToRelative(0.8f, 0.8f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.4f, -1.06f)
                curveToRelative(12.61f, -5.78f, 23.15f, -10.63f, 29.91f, -13.74f)
                arcToRelative(41.71f, 41.71f, 0f, isMoreThanHalf = false, isPositiveArc = true, 17.44f, -3.79f)
                lineToRelative(30f, 0.1f)
                verticalLineToRelative(0f)
                arcToRelative(9.58f, 9.58f, 0f, isMoreThanHalf = false, isPositiveArc = false, 7.95f, -4.22f)
                arcToRelative(20.1f, 20.1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 3.37f, -7.15f)
                curveToRelative(-3f, 1.35f, -6.56f, 1.34f, -8.84f, -0.24f)
                arcToRelative(4.55f, 4.55f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1.67f, -5.51f)
                arcToRelative(5.71f, 5.71f, 0f, isMoreThanHalf = false, isPositiveArc = true, 6.1f, -4.05f)
                curveToRelative(2.38f, 0.26f, 5.31f, 2.22f, 6f, 6.83f)
                arcToRelative(10.61f, 10.61f, 0f, isMoreThanHalf = false, isPositiveArc = false, 3.94f, -7f)
                arcToRelative(0.8f, 0.8f, 0f, isMoreThanHalf = true, isPositiveArc = true, 1.59f, 0.15f)
                arcToRelative(12.57f, 12.57f, 0f, isMoreThanHalf = false, isPositiveArc = true, -5f, 8.55f)
                lineToRelative(-0.4f, 0.31f)
                curveToRelative(-0.13f, 2.61f, -1.35f, 5.65f, -3.66f, 9f)
                arcToRelative(11.18f, 11.18f, 0f, isMoreThanHalf = false, isPositiveArc = true, -9.28f, 4.93f)
                verticalLineToRelative(0f)
                lineToRelative(-30f, -0.1f)
                horizontalLineToRelative(-0.13f)
                arcToRelative(40f, 40f, 0f, isMoreThanHalf = false, isPositiveArc = false, -16.64f, 3.65f)
                curveToRelative(-6.76f, 3.11f, -17.3f, 7.95f, -29.92f, 13.73f)
                arcToRelative(0.76f, 0.76f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.43f, 0.08f)
                close()
                moveTo(129.76f, 198.19f)
                arcToRelative(4.17f, 4.17f, 0f, isMoreThanHalf = false, isPositiveArc = false, -3.94f, 3f)
                arcToRelative(3f, 3f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.07f, 3.69f)
                curveToRelative(1.8f, 1.26f, 5.26f, 1.34f, 8f, -0.31f)
                verticalLineToRelative(-0.29f)
                curveToRelative(-0.42f, -4.13f, -2.69f, -5.83f, -4.68f, -6.05f)
                arcToRelative(3.55f, 3.55f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.45f, -0.04f)
                close()
            }
            path(fill = SolidColor(primaryColor)) {
                moveToRelative(55.63f, 252.85f)
                curveToRelative(3.41f, -2f, 5.52f, -6.2f, 5.52f, -12.22f)
                curveToRelative(0f, -12f, -8.45f, -26.67f, -18.87f, -32.68f)
                curveToRelative(-5.21f, -3f, -9.93f, -3.3f, -13.34f, -1.32f)
                lineToRelative(-7.86f, 4.53f)
                curveToRelative(-3.41f, 2f, -5.52f, 6.21f, -5.52f, 12.23f)
                curveToRelative(0f, 12f, 8.46f, 26.67f, 18.87f, 32.68f)
                curveToRelative(5.22f, 3f, 9.93f, 3.29f, 13.35f, 1.32f)
                close()
            }
            path(fill = SolidColor(Color(0xFF37474F))) {
                moveToRelative(12.84f, 225.92f)
                curveToRelative(0f, -11.5f, 8.07f, -16.16f, 18f, -10.41f)
                curveToRelative(9.93f, 5.75f, 18f, 19.73f, 18f, 31.23f)
                curveToRelative(0f, 11.5f, -8.07f, 16.16f, -18f, 10.41f)
                curveToRelative(-9.93f, -5.75f, -18f, -19.73f, -18f, -31.23f)
                close()
            }
            path(fill = SolidColor(Color(0xFF455A64))) {
                moveToRelative(10.2f, 227.93f)
                curveToRelative(0f, -11.24f, 7.89f, -15.79f, 17.62f, -10.18f)
                curveToRelative(9.73f, 5.61f, 17.62f, 19.28f, 17.62f, 30.52f)
                curveToRelative(0f, 11.24f, -7.89f, 15.79f, -17.62f, 10.17f)
                curveToRelative(-9.73f, -5.62f, -17.62f, -19.28f, -17.62f, -30.51f)
                close()
            }
            path(fill = SolidColor(Color(0xFF37474F))) {
                moveToRelative(7.56f, 229.93f)
                curveToRelative(0f, -11f, 7.7f, -15.42f, 17.2f, -9.93f)
                curveToRelative(9.5f, 5.49f, 17.2f, 18.83f, 17.2f, 29.8f)
                curveToRelative(0f, 10.97f, -7.7f, 15.42f, -17.2f, 9.93f)
                curveToRelative(-9.5f, -5.49f, -17.2f, -18.83f, -17.2f, -29.8f)
                close()
            }
            path(fill = SolidColor(Color(0xFF455A64))) {
                moveToRelative(4.91f, 231.94f)
                curveToRelative(0f, -10.71f, 7.52f, -15.05f, 16.79f, -9.7f)
                curveToRelative(9.27f, 5.35f, 16.79f, 18.38f, 16.79f, 29.08f)
                curveToRelative(0f, 10.7f, -7.52f, 15.05f, -16.79f, 9.7f)
                curveToRelative(-9.27f, -5.35f, -16.79f, -18.38f, -16.79f, -29.08f)
                close()
            }
            path(fill = SolidColor(Color(0xFF37474F))) {
                moveToRelative(2.27f, 233.94f)
                curveToRelative(0f, -10.44f, 7.33f, -14.67f, 16.37f, -9.45f)
                curveToRelative(9.04f, 5.22f, 16.38f, 17.92f, 16.38f, 28.36f)
                curveToRelative(0f, 10.44f, -7.33f, 14.68f, -16.38f, 9.48f)
                curveToRelative(-9.05f, -5.2f, -16.37f, -17.95f, -16.37f, -28.39f)
                close()
            }
            path(fill = SolidColor(Color(0xFF455A64))) {
                moveToRelative(0f, 235.93f)
                curveToRelative(0f, -10.07f, 7.07f, -14.15f, 15.79f, -9.12f)
                curveToRelative(8.72f, 5.03f, 15.79f, 17.28f, 15.79f, 27.35f)
                curveToRelative(0f, 10.07f, -7.07f, 14.17f, -15.74f, 9.11f)
                curveToRelative(-8.67f, -5.06f, -15.84f, -17.28f, -15.84f, -27.34f)
                close()
            }
            path(fill = SolidColor(Color(0xFF263238))) {
                moveToRelative(2.51f, 240.4f)
                curveToRelative(0f, -6.81f, 4.75f, -9.59f, 10.66f, -6.2f)
                curveToRelative(5.91f, 3.39f, 10.71f, 11.66f, 10.73f, 18.47f)
                curveToRelative(0.02f, 6.81f, -4.76f, 9.58f, -10.66f, 6.19f)
                curveToRelative(-5.9f, -3.39f, -10.71f, -11.66f, -10.73f, -18.46f)
                close()
            }
            path(fill = SolidColor(Color(0xFF263238))) {
                moveToRelative(17.9f, 260.33f)
                lineToRelative(-1.23f, -2.12f)
                arcToRelative(7.57f, 7.57f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.12f, -4.28f)
                curveToRelative(0f, -5.54f, -3.92f, -12.26f, -8.73f, -15f)
                arcToRelative(7.48f, 7.48f, 0f, isMoreThanHalf = false, isPositiveArc = false, -4.26f, -1.15f)
                lineToRelative(-1.23f, -2.11f)
                lineToRelative(-2.44f, 4.66f)
                verticalLineToRelative(0f)
                arcToRelative(8.28f, 8.28f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.74f, 3.63f)
                curveToRelative(0f, 5.54f, 3.92f, 12.26f, 8.73f, 15f)
                arcToRelative(8f, 8f, 0f, isMoreThanHalf = false, isPositiveArc = false, 3.52f, 1.15f)
                verticalLineToRelative(0f)
                close()
            }
            path(fill = SolidColor(Color(0xFF37474F))) {
                moveToRelative(1.08f, 246.22f)
                curveToRelative(0f, -4.05f, 2.82f, -5.69f, 6.33f, -3.68f)
                arcToRelative(14f, 14f, 0f, isMoreThanHalf = false, isPositiveArc = true, 6.37f, 11f)
                curveToRelative(0f, 4.05f, -2.82f, 5.69f, -6.33f, 3.68f)
                arcToRelative(14f, 14f, 0f, isMoreThanHalf = false, isPositiveArc = true, -6.37f, -11f)
                close()
            }
        }.build()

        return _BrokenBulb!!
    }

@Suppress("ObjectPropertyName")
private var _BrokenBulb: ImageVector? = null
