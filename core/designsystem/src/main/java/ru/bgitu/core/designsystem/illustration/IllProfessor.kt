package ru.bgitu.core.designsystem.illustration

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.bgitu.core.designsystem.theme.LocalNewColorScheme

val AppIllustrations.Proffessor: ImageVector
    @Composable get() {
        if (_IllProfessor != null) {
            return _IllProfessor!!
        }
        val primaryColor = LocalNewColorScheme.current.foreground
        
        _IllProfessor = ImageVector.Builder(
            name = "IllProfessor",
            defaultWidth = 256.dp,
            defaultHeight = 256.dp,
            viewportWidth = 256f,
            viewportHeight = 256f
        ).apply {
            path(fill = SolidColor(Color(0xFF000000))) {
                moveTo(114.84f, 230.17f)
                lineToRelative(-0.3f, 2.02f)
                lineToRelative(4.26f, -1.48f)
                lineToRelative(-1.31f, -2.89f)
                close()
            }
            path(fill = SolidColor(Color(0xFF263238))) {
                moveToRelative(110.38f, 224.45f)
                curveToRelative(0f, 0f, -0.24f, 2.74f, -0.3f, 3.6f)
                curveToRelative(-0.06f, 0.87f, 3.55f, 3.65f, 4.46f, 4.11f)
                lineToRelative(0.3f, -2.02f)
                arcToRelative(15.83f, 15.83f, 45f, isMoreThanHalf = false, isPositiveArc = true, 2.03f, 3.33f)
                curveToRelative(0.68f, 1.25f, 1.52f, 3.61f, 3.43f, 5.26f)
                curveToRelative(2.84f, 2.43f, 7.32f, 3.75f, 11.92f, 4.04f)
                curveToRelative(4.6f, 0.29f, 7.26f, -0.76f, 8.6f, -1.74f)
                curveToRelative(1.34f, -0.98f, 1.78f, -2.12f, 1.52f, -3.3f)
                curveTo(142.08f, 236.55f, 110.38f, 224.45f, 110.38f, 224.45f)
                close()
            }
            path(fill = SolidColor(Color(0xFF37474F))) {
                moveToRelative(122.95f, 215.43f)
                lineToRelative(0.09f, -1.59f)
                curveToRelative(0.85f, 0f, 0.9f, 2.28f, 2.08f, 4.36f)
                arcToRelative(40.95f, 40.95f, 135f, isMoreThanHalf = false, isPositiveArc = false, 6.26f, 8.46f)
                curveToRelative(4.56f, 4.38f, 8.66f, 7.16f, 10.69f, 9.13f)
                curveToRelative(2.72f, 2.64f, -2.28f, 6.59f, -9.79f, 5.97f)
                curveToRelative(-3.73f, -0.3f, -10.3f, -2.36f, -12.11f, -4.61f)
                curveToRelative(-1.8f, -2.25f, -2.86f, -5.72f, -4.31f, -7.36f)
                curveToRelative(-1.45f, -1.64f, -4.61f, -3.33f, -5.48f, -5.32f)
                curveToRelative(-0.48f, -1.08f, -0.15f, -3.57f, 0.21f, -5.68f)
                curveToRelative(0.33f, -1.93f, 0.46f, -4.91f, 1.52f, -4.64f)
                lineToRelative(0.05f, 1f)
                curveToRelative(0.43f, 0.87f, 1.43f, 2.28f, 4.43f, 2.88f)
                curveToRelative(1.52f, 0.31f, 6f, 0.56f, 6.35f, -2.59f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 0.15f,
                strokeAlpha = 0.15f
            ) {
                moveToRelative(119.04f, 218.17f)
                curveToRelative(1.44f, -0.08f, 3f, -0.52f, 3.63f, -1.79f)
                lineToRelative(0f, -0.05f)
                arcToRelative(3.65f, 3.65f, 45f, isMoreThanHalf = false, isPositiveArc = false, 0.89f, 3.09f)
                curveToRelative(1.3f, 1.6f, 9.51f, 12.93f, 9.51f, 12.93f)
                curveToRelative(0f, 0f, -7.67f, -9.35f, -9.13f, -11.12f)
                curveToRelative(-2.18f, -2.6f, -2.88f, -2.92f, -4.93f, -3.04f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 0.15f,
                strokeAlpha = 0.15f
            ) {
                moveToRelative(120.87f, 221.16f)
                arcToRelative(0.46f, 0.46f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.52f, 0.4f)
                arcToRelative(0.46f, 0.46f, 45f, isMoreThanHalf = false, isPositiveArc = true, -0.39f, -0.52f)
                arcToRelative(0.46f, 0.46f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.52f, -0.4f)
                arcToRelative(0.46f, 0.46f, 135f, isMoreThanHalf = false, isPositiveArc = true, 0.39f, 0.52f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 0.15f,
                strokeAlpha = 0.15f
            ) {
                moveToRelative(122.99f, 223.69f)
                arcToRelative(0.46f, 0.46f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.52f, 0.4f)
                arcToRelative(0.46f, 0.46f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.12f, -0.91f)
                arcToRelative(0.46f, 0.46f, 135f, isMoreThanHalf = false, isPositiveArc = true, 0.4f, 0.52f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 0.15f,
                strokeAlpha = 0.15f
            ) {
                moveToRelative(125.14f, 226.18f)
                arcToRelative(0.46f, 0.46f, 0f, isMoreThanHalf = true, isPositiveArc = true, -0.4f, -0.52f)
                arcToRelative(0.46f, 0.46f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.4f, 0.52f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 0.15f,
                strokeAlpha = 0.15f
            ) {
                moveToRelative(127.3f, 228.69f)
                arcToRelative(0.46f, 0.46f, 45f, isMoreThanHalf = false, isPositiveArc = true, -0.52f, 0.4f)
                arcToRelative(0.46f, 0.46f, 45f, isMoreThanHalf = true, isPositiveArc = true, 0.52f, -0.4f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 0.15f,
                strokeAlpha = 0.15f
            ) {
                moveToRelative(129.46f, 231.2f)
                arcToRelative(0.47f, 0.47f, 135f, isMoreThanHalf = false, isPositiveArc = true, -0.52f, 0.4f)
                arcToRelative(0.46f, 0.46f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.13f, -0.91f)
                arcToRelative(0.46f, 0.46f, 135f, isMoreThanHalf = false, isPositiveArc = true, 0.4f, 0.52f)
                close()
            }
            path(fill = SolidColor(Color(0xFF263238))) {
                moveToRelative(120.36f, 221.28f)
                arcToRelative(0.16f, 0.16f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.11f, -0.05f)
                arcToRelative(0.19f, 0.19f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, -0.27f)
                arcToRelative(7.61f, 7.61f, 66.53f, isMoreThanHalf = false, isPositiveArc = true, 5.25f, -1.92f)
                lineToRelative(0f, 0f)
                arcToRelative(0.17f, 0.17f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.16f, 0.2f)
                arcToRelative(0.18f, 0.18f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.2f, 0.17f)
                curveToRelative(-2.72f, -0.23f, -4.94f, 1.8f, -4.97f, 1.83f)
                arcToRelative(0.17f, 0.17f, 45f, isMoreThanHalf = false, isPositiveArc = true, -0.13f, 0.05f)
                close()
            }
            path(fill = SolidColor(Color(0xFF263238))) {
                moveToRelative(122.51f, 223.78f)
                arcToRelative(0.19f, 0.19f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.11f, -0.32f)
                arcToRelative(7.61f, 7.61f, 96.2f, isMoreThanHalf = false, isPositiveArc = true, 5.26f, -1.92f)
                lineToRelative(0f, 0f)
                arcToRelative(0.18f, 0.18f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, 0.37f)
                arcToRelative(7.29f, 7.29f, 135f, isMoreThanHalf = false, isPositiveArc = false, -4.97f, 1.83f)
                arcToRelative(0.19f, 0.19f, 100.56f, isMoreThanHalf = false, isPositiveArc = true, -0.18f, 0.05f)
                close()
            }
            path(fill = SolidColor(Color(0xFF263238))) {
                moveToRelative(124.67f, 226.3f)
                arcToRelative(0.16f, 0.16f, 135f, isMoreThanHalf = false, isPositiveArc = true, -0.12f, -0.06f)
                arcToRelative(0.18f, 0.18f, 135f, isMoreThanHalf = false, isPositiveArc = true, 0f, -0.26f)
                arcToRelative(7.56f, 7.56f, 135f, isMoreThanHalf = false, isPositiveArc = true, 5.25f, -1.92f)
                lineToRelative(0f, 0f)
                arcToRelative(0.18f, 0.18f, 135f, isMoreThanHalf = false, isPositiveArc = true, 0f, 0.37f)
                curveToRelative(-2.72f, -0.23f, -4.94f, 1.8f, -4.97f, 1.83f)
                arcToRelative(0.18f, 0.18f, 135f, isMoreThanHalf = false, isPositiveArc = true, -0.16f, 0.05f)
                close()
            }
            path(fill = SolidColor(Color(0xFF263238))) {
                moveToRelative(126.82f, 228.81f)
                arcToRelative(0.14f, 0.14f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.11f, -0.06f)
                arcToRelative(0.17f, 0.17f, 45f, isMoreThanHalf = false, isPositiveArc = true, 0f, -0.26f)
                arcToRelative(7.52f, 7.52f, 0f, isMoreThanHalf = false, isPositiveArc = true, 5.25f, -1.92f)
                lineToRelative(0f, 0f)
                arcToRelative(0.19f, 0.19f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, 0.37f)
                curveToRelative(-2.72f, -0.23f, -4.94f, 1.8f, -4.97f, 1.83f)
                arcToRelative(0.17f, 0.17f, 135f, isMoreThanHalf = false, isPositiveArc = true, -0.17f, 0.05f)
                close()
            }
            path(fill = SolidColor(Color(0xFF263238))) {
                moveToRelative(128.98f, 231.3f)
                arcToRelative(0.17f, 0.17f, 45f, isMoreThanHalf = false, isPositiveArc = true, -0.11f, -0.06f)
                arcToRelative(0.18f, 0.18f, 135f, isMoreThanHalf = false, isPositiveArc = true, 0f, -0.26f)
                arcToRelative(7.61f, 7.61f, 0f, isMoreThanHalf = false, isPositiveArc = true, 5.25f, -1.92f)
                lineToRelative(0f, 0f)
                arcToRelative(0.19f, 0.19f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.17f, 0.21f)
                arcToRelative(0.19f, 0.19f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.21f, 0.17f)
                curveToRelative(-2.72f, -0.23f, -4.94f, 1.8f, -4.97f, 1.83f)
                arcToRelative(0.17f, 0.17f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.13f, 0.05f)
                close()
            }
            path(fill = SolidColor(Color(0xFF263238))) {
                moveToRelative(80.3f, 243.16f)
                curveToRelative(-0.76f, 3.57f, -1.67f, 5.88f, -3.26f, 7.8f)
                curveToRelative(-1.6f, 1.92f, -4.37f, 2.06f, -6.4f, 1.57f)
                arcToRelative(8.8f, 8.8f, 135f, isMoreThanHalf = false, isPositiveArc = true, -6.61f, -6.94f)
                curveToRelative(-0.59f, -2.49f, 0f, -5.32f, 1.02f, -8.58f)
                close()
            }
            path(fill = SolidColor(Color(0xFF37474F))) {
                moveToRelative(73.16f, 215.33f)
                curveToRelative(-1.02f, -0.07f, -2.05f, 4.72f, -3.35f, 9.48f)
                curveToRelative(-1.4f, 5.1f, -3.42f, 7.76f, -4.46f, 11.19f)
                curveToRelative(-1.45f, 4.75f, -1.44f, 6.98f, -0.17f, 10.18f)
                curveToRelative(1.26f, 3.19f, 8.21f, 7.27f, 11.82f, 3.51f)
                curveToRelative(2.94f, -3.04f, 3.61f, -6.84f, 4.12f, -11.76f)
                arcToRelative(124.72f, 124.72f, 109.92f, isMoreThanHalf = false, isPositiveArc = true, 1.64f, -12.4f)
                curveToRelative(0.76f, -3.61f, 1.52f, -7.11f, 0.71f, -7.64f)
                curveToRelative(-0.14f, 0.52f, -0.31f, 1.19f, -0.46f, 1.7f)
                curveToRelative(-0.11f, 0.4f, -0.51f, 2.36f, -1.08f, 2.28f)
                arcToRelative(7.8f, 7.8f, 135f, isMoreThanHalf = false, isPositiveArc = true, 0.2f, -1.38f)
                arcToRelative(1.64f, 1.64f, 45f, isMoreThanHalf = false, isPositiveArc = false, -0.1f, -1.22f)
                arcToRelative(1.95f, 1.95f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.04f, -0.66f)
                arcToRelative(28.34f, 28.34f, 0f, isMoreThanHalf = false, isPositiveArc = false, -2.93f, -0.91f)
                arcToRelative(14.08f, 14.08f, 135f, isMoreThanHalf = false, isPositiveArc = false, -1.52f, -0.29f)
                arcToRelative(16.47f, 16.47f, 135f, isMoreThanHalf = false, isPositiveArc = false, -2.65f, -0.29f)
                arcToRelative(0.9f, 0.9f, 135f, isMoreThanHalf = false, isPositiveArc = false, -0.62f, 0.41f)
                arcToRelative(1.7f, 1.7f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.14f, 0.55f)
                arcToRelative(4.37f, 4.37f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.27f, 0.98f)
                curveToRelative(-0.06f, 0.14f, -0.19f, 0.3f, -0.33f, 0.24f)
                curveToRelative(-0.14f, -0.05f, -0.14f, -0.2f, -0.12f, -0.32f)
                curveToRelative(0.09f, -0.59f, 0.23f, -1f, 0.36f, -1.68f)
                curveToRelative(0.06f, -0.31f, 0.17f, -0.76f, 0.23f, -1.06f)
                curveToRelative(0.05f, -0.3f, 0.12f, -0.61f, 0.19f, -0.92f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 0.15f,
                strokeAlpha = 0.15f
            ) {
                moveToRelative(68.84f, 237.75f)
                arcToRelative(0.36f, 0.36f, 135f, isMoreThanHalf = false, isPositiveArc = false, 0.69f, 0.17f)
                arcToRelative(0.36f, 0.36f, 135f, isMoreThanHalf = true, isPositiveArc = false, -0.69f, -0.17f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 0.15f,
                strokeAlpha = 0.15f
            ) {
                moveToRelative(69.69f, 234.45f)
                arcToRelative(0.35f, 0.35f, 135f, isMoreThanHalf = true, isPositiveArc = false, 0.43f, -0.26f)
                arcToRelative(0.36f, 0.36f, 135f, isMoreThanHalf = false, isPositiveArc = false, -0.43f, 0.26f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 0.15f,
                strokeAlpha = 0.15f
            ) {
                moveToRelative(70.48f, 231.3f)
                arcToRelative(0.37f, 0.37f, 135f, isMoreThanHalf = false, isPositiveArc = false, 0.26f, 0.43f)
                arcToRelative(0.36f, 0.36f, 135f, isMoreThanHalf = false, isPositiveArc = false, 0.17f, -0.69f)
                arcToRelative(0.37f, 0.37f, 135f, isMoreThanHalf = false, isPositiveArc = false, -0.43f, 0.26f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 0.15f,
                strokeAlpha = 0.15f
            ) {
                moveToRelative(71.26f, 228.26f)
                arcToRelative(0.36f, 0.36f, 135f, isMoreThanHalf = false, isPositiveArc = false, 0.26f, 0.43f)
                arcToRelative(0.35f, 0.35f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.43f, -0.26f)
                arcToRelative(0.36f, 0.36f, 45f, isMoreThanHalf = true, isPositiveArc = false, -0.69f, -0.17f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 0.15f,
                strokeAlpha = 0.15f
            ) {
                moveToRelative(79.16f, 229.94f)
                arcToRelative(0.36f, 0.36f, 45f, isMoreThanHalf = false, isPositiveArc = false, 0.26f, 0.43f)
                arcToRelative(0.36f, 0.36f, 135f, isMoreThanHalf = false, isPositiveArc = false, 0.17f, -0.69f)
                arcToRelative(0.36f, 0.36f, 45f, isMoreThanHalf = false, isPositiveArc = false, -0.43f, 0.26f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 0.15f,
                strokeAlpha = 0.15f
            ) {
                moveToRelative(78.37f, 233.04f)
                arcToRelative(0.36f, 0.36f, 45f, isMoreThanHalf = false, isPositiveArc = false, 0.26f, 0.43f)
                arcToRelative(0.35f, 0.35f, 135f, isMoreThanHalf = true, isPositiveArc = false, -0.26f, -0.43f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 0.15f,
                strokeAlpha = 0.15f
            ) {
                moveToRelative(77.57f, 236.16f)
                arcToRelative(0.35f, 0.35f, 135f, isMoreThanHalf = true, isPositiveArc = false, 0.43f, -0.26f)
                arcToRelative(0.36f, 0.36f, 45f, isMoreThanHalf = false, isPositiveArc = false, -0.43f, 0.26f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 0.15f,
                strokeAlpha = 0.15f
            ) {
                moveToRelative(76.76f, 239.47f)
                arcToRelative(0.36f, 0.36f, 45f, isMoreThanHalf = false, isPositiveArc = false, 0.69f, 0.17f)
                arcToRelative(0.36f, 0.36f, 45f, isMoreThanHalf = false, isPositiveArc = false, -0.69f, -0.17f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 0.15f,
                strokeAlpha = 0.15f
            ) {
                moveTo(78.46f, 217.37f)
                lineToRelative(-4.93f, 23.24f)
                lineToRelative(6.23f, -23.02f)
                close()
            }
            path(fill = SolidColor(Color(0xFF263238))) {
                moveToRelative(69.16f, 238.02f)
                lineToRelative(0.05f, 0f)
                curveToRelative(2.4f, -0.17f, 6.33f, 0.67f, 7.78f, 1.67f)
                arcToRelative(0.18f, 0.18f, 135f, isMoreThanHalf = false, isPositiveArc = false, 0.25f, -0.05f)
                arcToRelative(0.19f, 0.19f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.05f, -0.25f)
                curveToRelative(-1.52f, -1.06f, -5.5f, -1.9f, -8.01f, -1.73f)
                arcToRelative(0.18f, 0.18f, 45f, isMoreThanHalf = false, isPositiveArc = false, 0f, 0.36f)
                close()
            }
            path(fill = SolidColor(Color(0xFF263238))) {
                moveToRelative(69.99f, 234.71f)
                arcToRelative(0.13f, 0.13f, 135f, isMoreThanHalf = false, isPositiveArc = false, 0.09f, 0f)
                curveToRelative(1.9f, -0.57f, 6.08f, 0.54f, 7.73f, 1.67f)
                arcToRelative(0.18f, 0.18f, 135f, isMoreThanHalf = true, isPositiveArc = false, 0.21f, -0.3f)
                curveToRelative(-1.71f, -1.19f, -6.04f, -2.33f, -8.05f, -1.72f)
                arcToRelative(0.19f, 0.19f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.12f, 0.22f)
                arcToRelative(0.2f, 0.2f, 45f, isMoreThanHalf = false, isPositiveArc = false, 0.14f, 0.12f)
                close()
            }
            path(fill = SolidColor(Color(0xFF263238))) {
                moveToRelative(70.78f, 231.59f)
                lineToRelative(0.09f, 0f)
                curveToRelative(1.9f, -0.57f, 6.08f, 0.54f, 7.74f, 1.67f)
                arcToRelative(0.18f, 0.18f, 45f, isMoreThanHalf = true, isPositiveArc = false, 0.21f, -0.3f)
                curveToRelative(-1.72f, -1.19f, -6.04f, -2.33f, -8.05f, -1.72f)
                arcToRelative(0.18f, 0.18f, 45f, isMoreThanHalf = false, isPositiveArc = false, 0f, 0.35f)
                close()
            }
            path(fill = SolidColor(Color(0xFF263238))) {
                moveToRelative(71.57f, 228.51f)
                arcToRelative(0.18f, 0.18f, 135f, isMoreThanHalf = false, isPositiveArc = false, 0.09f, 0f)
                curveToRelative(1.9f, -0.58f, 6.08f, 0.53f, 7.74f, 1.67f)
                arcToRelative(0.17f, 0.17f, 135f, isMoreThanHalf = false, isPositiveArc = false, 0.25f, -0.05f)
                arcToRelative(0.18f, 0.18f, 45f, isMoreThanHalf = false, isPositiveArc = false, -0.05f, -0.25f)
                curveToRelative(-1.72f, -1.19f, -6.04f, -2.32f, -8.05f, -1.72f)
                arcToRelative(0.18f, 0.18f, 45f, isMoreThanHalf = false, isPositiveArc = false, -0.12f, 0.23f)
                arcToRelative(0.17f, 0.17f, 135f, isMoreThanHalf = false, isPositiveArc = false, 0.13f, 0.12f)
                close()
            }
            path(fill = SolidColor(Color(0xFF263238))) {
                moveToRelative(81.51f, 103.05f)
                curveToRelative(-3.28f, 29.81f, -5.97f, 70.46f, -5.97f, 70.46f)
                curveToRelative(-0.28f, 2.28f, -2.01f, 6.08f, -3.45f, 17.26f)
                curveToRelative(-1.52f, 11.77f, -3.14f, 33.86f, -3.14f, 33.86f)
                curveToRelative(0f, 0f, 8.13f, 4.7f, 14.89f, 2.67f)
                curveToRelative(0f, 0f, 7.56f, -31.25f, 11.41f, -45.06f)
                curveToRelative(3.66f, -13.07f, 9.5f, -37.88f, 9.5f, -37.88f)
                lineToRelative(2.71f, 24.81f)
                curveToRelative(0f, 0f, -0.76f, 8.5f, -0.65f, 14.45f)
                curveToRelative(0.08f, 6.94f, 3.62f, 35.85f, 3.62f, 35.85f)
                curveToRelative(0f, 0f, 8.82f, 5.57f, 15.93f, 0.46f)
                curveToRelative(0f, 0f, 2.48f, -41.36f, 2.93f, -49.7f)
                curveToRelative(0.88f, -16.34f, -0.05f, -48.1f, 0f, -69.21f)
                close()
            }
            path(fill = SolidColor(Color(0xFF000000))) {
                moveToRelative(92.6f, 137.82f)
                quadToRelative(-1.27f, 10.72f, -2.75f, 21.4f)
                curveToRelative(-0.49f, 3.56f, -0.99f, 7.13f, -1.52f, 10.65f)
                lineToRelative(-0.76f, 5.32f)
                lineToRelative(-0.4f, 2.67f)
                curveToRelative(-0.13f, 0.89f, -0.33f, 1.76f, -0.5f, 2.65f)
                quadToRelative(-2.09f, 10.58f, -4.35f, 21.13f)
                quadToRelative(-2.26f, 10.55f, -4.77f, 21.04f)
                quadToRelative(1.73f, -10.65f, 3.72f, -21.24f)
                quadToRelative(1.99f, -10.59f, 4.14f, -21.1f)
                curveToRelative(0.17f, -0.88f, 0.38f, -1.76f, 0.51f, -2.65f)
                lineToRelative(0.41f, -2.66f)
                lineToRelative(0.84f, -5.32f)
                quadToRelative(0.84f, -5.32f, 1.73f, -10.65f)
                quadToRelative(1.75f, -10.66f, 3.7f, -21.25f)
                close()
            }
            path(fill = SolidColor(Color(0xFF000000))) {
                moveToRelative(125.75f, 136.8f)
                curveToRelative(0.55f, 6.71f, 0.76f, 13.44f, 0.82f, 20.16f)
                curveToRelative(0.06f, 6.72f, 0.14f, 13.47f, -0.32f, 20.17f)
                curveToRelative(-0.46f, 6.7f, -0.76f, 13.42f, -1.21f, 20.13f)
                curveToRelative(-0.45f, 6.71f, -0.97f, 13.42f, -1.57f, 20.11f)
                quadToRelative(0.15f, -10.08f, 0.56f, -20.15f)
                curveToRelative(0.25f, -6.72f, 0.58f, -13.42f, 0.92f, -20.13f)
                lineToRelative(0.28f, -5.03f)
                lineToRelative(0.07f, -1.25f)
                lineToRelative(0f, -1.25f)
                lineToRelative(0.05f, -2.52f)
                lineToRelative(0.18f, -10.08f)
                lineToRelative(0.14f, -10.07f)
                curveToRelative(0.09f, -3.36f, 0.08f, -6.72f, 0.08f, -10.08f)
                close()
            }
            path(fill = SolidColor(Color(0xFF000000))) {
                moveToRelative(104.75f, 144.36f)
                lineToRelative(3.42f, -16.27f)
                curveToRelative(0f, 0f, 8.14f, -0.94f, 12.42f, -4.56f)
                curveToRelative(0f, 0f, -2.84f, 4.85f, -10.54f, 6.55f)
                lineToRelative(-3.35f, 14.37f)
                lineToRelative(0.76f, 24.7f)
                close()
            }
            path(fill = SolidColor(Color(0xFFEAEAEA))) {
                moveToRelative(194.75f, 53.04f)
                lineToRelative(9.19f, -8.91f)
                arcToRelative(2.94f, 2.94f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2.28f, 2.21f)
                lineToRelative(-9f, 8.89f)
                close()
            }
            path(fill = SolidColor(Color(0xFFFFA8A7))) {
                moveToRelative(156.7f, 66.03f)
                arcToRelative(105.29f, 105.29f, 45f, isMoreThanHalf = false, isPositiveArc = false, -7.03f, 9.99f)
                curveToRelative(-2.57f, 4.34f, 6.02f, 9.69f, 8.54f, 8.41f)
                arcToRelative(41.96f, 41.96f, 135f, isMoreThanHalf = false, isPositiveArc = false, 6.33f, -4.03f)
                curveToRelative(4.37f, -3.32f, 18.53f, -14.56f, 18.53f, -14.56f)
                curveToRelative(0f, 0f, 3.1f, 1.38f, 5.99f, 0.35f)
                curveToRelative(2.89f, -1.03f, 10.53f, -9.95f, 10.53f, -9.95f)
                curveToRelative(0f, 0f, -2.38f, -0.83f, -5.14f, -1.66f)
                curveToRelative(1.63f, -1.24f, 1.93f, -2.82f, 1.47f, -3.16f)
                curveToRelative(-0.62f, -0.45f, -2.87f, 0.24f, -4.41f, 0.27f)
                curveToRelative(-2.56f, 0.05f, -5.32f, -0.93f, -5.99f, -0.76f)
                curveToRelative(-1.92f, 0.57f, -5.26f, 3.8f, -8.37f, 5.45f)
                curveToRelative(-7.15f, 3.8f, -20.46f, 9.64f, -20.46f, 9.64f)
                close()
            }
            path(fill = SolidColor(Color(0xFFDC7F81))) {
                moveToRelative(183.06f, 65.84f)
                arcToRelative(6.28f, 6.28f, 135f, isMoreThanHalf = false, isPositiveArc = true, -1.42f, -3.8f)
                arcToRelative(4.44f, 4.44f, 0f, isMoreThanHalf = false, isPositiveArc = false, 4.72f, -1.52f)
                curveToRelative(1.79f, -2.44f, 1.25f, -3.65f, 3.68f, -4.04f)
                arcToRelative(13.17f, 13.17f, 135f, isMoreThanHalf = false, isPositiveArc = false, 4.4f, -1.64f)
                lineToRelative(4.8f, 1.57f)
                curveToRelative(0f, 0f, -3.89f, 5.6f, -5.58f, 7.45f)
                curveToRelative(-1.7f, 1.85f, -5.3f, 4.27f, -10.6f, 1.98f)
                close()
            }
            path(fill = SolidColor(Color(0xFFF28F8F))) {
                moveToRelative(195.7f, 53.05f)
                curveToRelative(0f, 0f, 3.21f, 0.52f, 4.68f, 1.65f)
                curveToRelative(0.64f, 0.49f, 0.94f, 1.1f, 0.14f, 2.43f)
                arcToRelative(2.85f, 2.85f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2.14f, 1.17f)
                arcToRelative(2.33f, 2.33f, 45f, isMoreThanHalf = false, isPositiveArc = true, 0.2f, 2.18f)
                arcToRelative(2.92f, 2.92f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2.81f, 1.62f)
                arcToRelative(1.81f, 1.81f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.36f, 1.79f)
                arcToRelative(3.14f, 3.14f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2.62f, 0.76f)
                arcToRelative(2.76f, 2.76f, 135f, isMoreThanHalf = false, isPositiveArc = true, -1.02f, 1.75f)
                curveToRelative(-0.85f, 0.55f, -3.44f, 0.62f, -5.45f, 0.19f)
                curveToRelative(-2.01f, -0.43f, -1.67f, -2.28f, -0.87f, -2.86f)
                arcToRelative(4.23f, 4.23f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2.21f, -0.71f)
                arcToRelative(2.08f, 2.08f, 45f, isMoreThanHalf = false, isPositiveArc = true, 0.05f, -1.87f)
                curveToRelative(0.55f, -0.9f, 2.87f, -0.76f, 2.87f, -0.76f)
                curveToRelative(0f, 0f, -1.13f, -1.22f, -0.46f, -2.28f)
                curveToRelative(0.66f, -1.06f, 3.41f, -0.87f, 3.41f, -0.87f)
                arcToRelative(1.43f, 1.43f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.93f, -1.52f)
                curveToRelative(1.54f, -0.8f, 2.78f, -1.73f, 3.08f, -2.66f)
                close()
            }
            path(fill = SolidColor(Color(0xFFFAFAFA))) {
                moveToRelative(118.22f, 44.15f)
                curveToRelative(3.89f, 0.48f, 6.08f, 1.09f, 8.37f, 2.28f)
                curveToRelative(3.27f, 1.7f, 3.92f, 2.33f, 7.75f, 5.4f)
                curveToRelative(5.55f, 4.44f, 19.42f, 15.07f, 19.42f, 15.07f)
                arcToRelative(112.79f, 112.79f, 135f, isMoreThanHalf = false, isPositiveArc = false, -2.84f, 20.26f)
                curveToRelative(-7.37f, -3.08f, -18.35f, -11.84f, -32.33f, -22.82f)
                close()
            }
            path(fill = SolidColor(Color(0xFFE6E6E6))) {
                moveToRelative(154.81f, 66.19f)
                curveToRelative(0.46f, 0f, 1.58f, -0.83f, 2.14f, -0.28f)
                arcToRelative(15.38f, 15.38f, 45f, isMoreThanHalf = false, isPositiveArc = false, -2.28f, 9.75f)
                curveToRelative(0.44f, 5.9f, 3.17f, 8.46f, 5.15f, 7.91f)
                lineToRelative(1.45f, -0.9f)
                curveToRelative(0f, 0f, 0.37f, 0.76f, -1.68f, 2.47f)
                curveToRelative(-2.05f, 1.71f, -6.08f, 3.45f, -8.43f, 2.52f)
                curveToRelative(-2.96f, -1.16f, -3.59f, -2.24f, -3.95f, -3.61f)
                curveToRelative(-0.96f, -3.57f, 0f, -8.45f, 3.15f, -14.1f)
                curveToRelative(1.73f, -3.16f, 3.32f, -3.75f, 4.45f, -3.76f)
                close()
            }
            path(fill = SolidColor(Color(0xFFE6E6E6))) {
                moveToRelative(80.61f, 49.89f)
                curveToRelative(0f, 0f, -1.81f, 1.48f, 0.33f, 12.17f)
                curveToRelative(1.68f, 8.37f, 3.61f, 19.09f, 3.75f, 22.75f)
                curveToRelative(0.21f, 5.62f, -1.32f, 19.37f, -1.32f, 19.37f)
                curveToRelative(12.93f, 6.59f, 39.39f, 5.01f, 44.11f, -4.08f)
                curveToRelative(0f, 0f, -1.12f, -32.8f, -2.63f, -42.39f)
                curveToRelative(-1.16f, -7.34f, -3.64f, -11.41f, -6.31f, -13.51f)
                lineToRelative(-9.08f, -1.29f)
                lineToRelative(-10.94f, -0.47f)
                close()
            }
            path(fill = SolidColor(Color(0xFF37474F))) {
                moveToRelative(92.81f, 10.7f)
                curveToRelative(0f, 0f, -2.36f, 0.17f, -3.91f, 3.14f)
                curveToRelative(-0.76f, 1.52f, -1.35f, 4.22f, 0.76f, 12.01f)
                arcToRelative(31.59f, 31.59f, 0f, isMoreThanHalf = false, isPositiveArc = false, 3.73f, 8.6f)
                arcToRelative(3.8f, 3.8f, 83.8f, isMoreThanHalf = false, isPositiveArc = false, 2.24f, 1.79f)
                lineToRelative(-0.14f, -5.99f)
                lineToRelative(-0.21f, -4.17f)
                curveToRelative(0f, 0f, 2.68f, -3.66f, 2.97f, -6.65f)
                curveToRelative(0.38f, -3.86f, -0.43f, -5.43f, -0.43f, -5.43f)
                close()
            }
            path(fill = SolidColor(Color(0xFF37474F))) {
                moveToRelative(91.97f, 10.9f)
                curveToRelative(0.59f, -3.36f, 4.87f, -7.37f, 13.1f, -7.79f)
                curveToRelative(6.75f, -0.35f, 10.69f, 1.98f, 13.03f, 5.92f)
                curveToRelative(2.34f, 3.93f, 0.76f, 9.03f, 0.76f, 9.03f)
                lineToRelative(-21.19f, -1.44f)
                close()
            }
            path(fill = SolidColor(Color(0xFFE0E0E0))) {
                moveToRelative(94.2f, 11.89f)
                curveToRelative(0f, 1.21f, -0.65f, 2.56f, -2.51f, 3.8f)
                arcToRelative(5.32f, 5.32f, 120.81f, isMoreThanHalf = false, isPositiveArc = false, -2.61f, 4.44f)
                arcToRelative(5.15f, 5.15f, 45f, isMoreThanHalf = false, isPositiveArc = true, 2.73f, -3.34f)
                arcToRelative(12.49f, 12.49f, 0f, isMoreThanHalf = false, isPositiveArc = false, 2.56f, -1.86f)
                curveToRelative(0f, 0f, -0.17f, 1.57f, -2.74f, 3.61f)
                arcToRelative(5.57f, 5.57f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.86f, 5.25f)
                arcToRelative(4.85f, 4.85f, 135f, isMoreThanHalf = false, isPositiveArc = true, 1.72f, -3.8f)
                curveToRelative(1.91f, -1.69f, 4.28f, -2.48f, 4.21f, -4.97f)
                arcToRelative(4.15f, 4.15f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.51f, -3.14f)
                close()
            }
            path(fill = SolidColor(Color(0xFFFFA8A7))) {
                moveToRelative(90.01f, 31.99f)
                curveToRelative(2.34f, 1.22f, 4.52f, -0.94f, 4.52f, -0.94f)
                lineToRelative(0.44f, 12.34f)
                curveToRelative(0f, 0f, 0.24f, 2.38f, 4.03f, 4.48f)
                curveToRelative(5.38f, 2.97f, 7.7f, 3.69f, 10f, 6.84f)
                curveToRelative(1.74f, -5.32f, 3.7f, -8.3f, 0.26f, -12.34f)
                lineToRelative(0f, -2.81f)
                arcToRelative(19.85f, 19.85f, 135f, isMoreThanHalf = false, isPositiveArc = false, 3.73f, -0.11f)
                curveToRelative(2.75f, -0.43f, 4.47f, -2.61f, 5.32f, -5.57f)
                curveToRelative(1.22f, -4.4f, 1.7f, -11.41f, 0.07f, -18.93f)
                arcToRelative(6.43f, 6.43f, 0f, isMoreThanHalf = false, isPositiveArc = false, -2.28f, -3.69f)
                curveToRelative(-5.41f, -4.23f, -11.51f, -1.15f, -16.1f, 0.63f)
                arcToRelative(8.67f, 8.67f, 0f, isMoreThanHalf = false, isPositiveArc = true, -5.78f, 0f)
                curveToRelative(1.6f, 1.77f, 2.37f, 3.48f, 2.42f, 11.99f)
                arcToRelative(5.22f, 5.22f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1.19f, 1.52f)
                curveToRelative(-1.01f, 0.87f, -1.83f, -1.38f, -2.67f, -2.28f)
                arcToRelative(3.46f, 3.46f, 45f, isMoreThanHalf = false, isPositiveArc = false, -5.38f, 1.22f)
                arcToRelative(5.68f, 5.68f, 0f, isMoreThanHalf = false, isPositiveArc = false, 2.62f, 7.64f)
                close()
            }
            path(fill = SolidColor(Color(0xFFF28F8F))) {
                moveToRelative(114.47f, 14.4f)
                arcToRelative(4.15f, 4.15f, 0f, isMoreThanHalf = false, isPositiveArc = false, -2.56f, -0.1f)
                arcToRelative(15.32f, 15.32f, 0f, isMoreThanHalf = false, isPositiveArc = true, -3.04f, 0.41f)
                curveToRelative(-1.77f, 0f, -3.95f, -0.76f, -5.61f, 0.08f)
                curveToRelative(0f, 0f, -0.05f, 0.09f, 0f, 0.08f)
                arcToRelative(7.96f, 7.96f, 135f, isMoreThanHalf = false, isPositiveArc = true, 2.36f, -0.15f)
                curveToRelative(0.96f, 0f, 2.18f, 0.44f, 3.14f, 0.43f)
                curveToRelative(1.94f, 0f, 3.8f, -0.82f, 5.73f, -0.68f)
                curveToRelative(0.01f, 0f, 0.03f, -0.08f, -0.01f, -0.08f)
                close()
            }
            path(fill = SolidColor(Color(0xFFF28F8F))) {
                moveToRelative(112.19f, 12.71f)
                curveToRelative(-0.62f, 0.11f, -1.25f, 0.2f, -1.87f, 0.32f)
                arcToRelative(7.9f, 7.9f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1.94f, 0.16f)
                curveToRelative(-0.59f, 0f, -1.16f, -0.14f, -1.75f, -0.2f)
                arcToRelative(3.2f, 3.2f, 135f, isMoreThanHalf = false, isPositiveArc = false, -1.67f, 0.24f)
                curveToRelative(0f, 0f, 0f, 0.05f, 0f, 0.05f)
                curveToRelative(1.2f, -0.24f, 2.43f, 0.21f, 3.64f, 0.23f)
                arcToRelative(9.26f, 9.26f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.72f, -0.15f)
                arcToRelative(10.83f, 10.83f, 45f, isMoreThanHalf = false, isPositiveArc = false, 1.87f, -0.57f)
                curveToRelative(0f, 0f, 0.03f, -0.08f, 0f, -0.07f)
                close()
            }
            path(fill = SolidColor(Color(0xFFF28F8F))) {
                moveToRelative(102.08f, 21.44f)
                arcToRelative(2.85f, 2.85f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.44f, 0f)
                arcToRelative(2.44f, 2.44f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.48f, 0.11f)
                arcToRelative(2.89f, 2.89f, 45f, isMoreThanHalf = false, isPositiveArc = false, -0.41f, 0.14f)
                lineToRelative(-0.2f, 0.08f)
                arcToRelative(0.43f, 0.43f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.22f, 0.28f)
                curveToRelative(0f, 0.24f, 0.51f, 0.05f, 0.59f, 0f)
                curveToRelative(0.4f, -0.14f, 0.82f, -0.11f, 1.22f, -0.22f)
                curveToRelative(0.24f, -0.06f, 0.15f, -0.37f, -0.06f, -0.39f)
                close()
            }
            path(fill = SolidColor(Color(0xFFF28F8F))) {
                moveToRelative(102.28f, 22.33f)
                lineToRelative(-0.11f, 0f)
                arcToRelative(0.76f, 0.76f, 66.52f, isMoreThanHalf = false, isPositiveArc = false, -0.33f, 0.08f)
                lineToRelative(-0.39f, 0.14f)
                lineToRelative(-0.38f, 0.14f)
                arcToRelative(0.67f, 0.67f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.37f, 0.27f)
                arcToRelative(0.11f, 0.11f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, 0.08f)
                arcToRelative(0.11f, 0.11f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.08f, 0.08f)
                lineToRelative(0.13f, 0f)
                lineToRelative(0.94f, -0.27f)
                arcToRelative(0.97f, 0.97f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.46f, -0.21f)
                arcToRelative(0.3f, 0.3f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.07f, -0.11f)
                arcToRelative(0.14f, 0.14f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.1f, -0.2f)
                close()
            }
            path(fill = SolidColor(Color(0xFFF28F8F))) {
                moveToRelative(116.88f, 20.64f)
                arcToRelative(2.21f, 2.21f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.45f, 0f)
                arcToRelative(3.57f, 3.57f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.48f, 0.1f)
                arcToRelative(4.2f, 4.2f, 135f, isMoreThanHalf = false, isPositiveArc = true, 0.4f, 0.14f)
                arcToRelative(1.57f, 1.57f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.2f, 0.09f)
                arcToRelative(0.43f, 0.43f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.22f, 0.27f)
                curveToRelative(0f, 0.24f, -0.51f, 0.05f, -0.59f, 0f)
                curveToRelative(-0.4f, -0.14f, -0.82f, -0.12f, -1.22f, -0.23f)
                curveToRelative(-0.24f, -0.07f, -0.15f, -0.37f, 0.06f, -0.37f)
                close()
            }
            path(fill = SolidColor(Color(0xFFF28F8F))) {
                moveToRelative(116.67f, 21.51f)
                arcToRelative(0.25f, 0.25f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.11f, 0f)
                arcToRelative(0.76f, 0.76f, 113.48f, isMoreThanHalf = false, isPositiveArc = true, 0.33f, 0.07f)
                lineToRelative(0.4f, 0.14f)
                lineToRelative(0.37f, 0.13f)
                arcToRelative(0.66f, 0.66f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.37f, 0.28f)
                arcToRelative(0.11f, 0.11f, 45f, isMoreThanHalf = false, isPositiveArc = true, 0f, 0.08f)
                curveToRelative(0f, 0.04f, -0.05f, 0.07f, -0.08f, 0.08f)
                arcToRelative(0.25f, 0.25f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.13f, 0f)
                lineToRelative(-0.94f, -0.26f)
                arcToRelative(0.99f, 0.99f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.46f, -0.22f)
                arcToRelative(0.22f, 0.22f, 135f, isMoreThanHalf = false, isPositiveArc = true, -0.07f, -0.11f)
                arcToRelative(0.13f, 0.13f, 45f, isMoreThanHalf = false, isPositiveArc = true, 0.1f, -0.19f)
                close()
            }
            path(fill = SolidColor(Color(0xFF37474F))) {
                moveToRelative(104.96f, 18.45f)
                lineToRelative(-3.04f, 1.31f)
                arcToRelative(1.73f, 1.73f, 135f, isMoreThanHalf = false, isPositiveArc = true, 0.9f, -2.23f)
                arcToRelative(1.61f, 1.61f, 45f, isMoreThanHalf = false, isPositiveArc = true, 2.14f, 0.92f)
                close()
            }
            path(fill = SolidColor(Color(0xFF37474F))) {
                moveTo(116.47f, 18.63f)
                lineTo(113.26f, 17.7f)
                arcToRelative(1.6f, 1.6f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2.01f, -1.16f)
                arcToRelative(1.76f, 1.76f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.2f, 2.1f)
                close()
            }
            path(fill = SolidColor(Color(0xFF263238))) {
                moveToRelative(103.22f, 21.75f)
                arcToRelative(1.35f, 1.35f, 0f, isMoreThanHalf = true, isPositiveArc = false, 1.3f, -1.39f)
                arcToRelative(1.35f, 1.35f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.3f, 1.39f)
                close()
            }
            path(fill = SolidColor(Color(0xFF263238))) {
                moveTo(113.26f, 21.27f)
                arcTo(1.35f, 1.35f, 0f, isMoreThanHalf = true, isPositiveArc = false, 114.57f, 19.88f)
                arcTo(1.22f, 1.22f, 0f, isMoreThanHalf = false, isPositiveArc = false, 113.26f, 21.27f)
                close()
            }
            path(fill = SolidColor(Color(0xFFF28F8F))) {
                moveToRelative(108.15f, 18.76f)
                lineToRelative(1.1f, 8.58f)
                lineToRelative(4.56f, -1.67f)
                arcToRelative(20.07f, 20.07f, 0f, isMoreThanHalf = false, isPositiveArc = false, -5.66f, -6.91f)
                close()
            }
            path(fill = SolidColor(Color(0xFF37474F))) {
                moveToRelative(96.53f, 23.16f)
                curveToRelative(0.57f, 5.86f, 2.61f, 6.16f, 4.71f, 6.4f)
                curveToRelative(2.78f, 0.32f, 4.07f, -1.24f, 9f, -1.48f)
                curveToRelative(3f, -0.15f, 3.98f, 0.69f, 5.71f, 1.31f)
                curveToRelative(1.29f, 0.46f, 2.28f, 0.24f, 2.73f, -0.9f)
                arcToRelative(19.59f, 19.59f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.63f, -6.38f)
                arcToRelative(47.71f, 47.71f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.24f, 9.62f)
                curveToRelative(-0.27f, 2.78f, -1.16f, 5.19f, -2.62f, 6.56f)
                curveToRelative(-2.02f, 1.89f, -6.39f, 2.84f, -11.97f, 1.89f)
                curveToRelative(-5.58f, -0.94f, -7.77f, -3.13f, -8.53f, -5.05f)
                curveToRelative(-2.47f, -6.21f, -1.77f, -9.25f, -2.56f, -10.4f)
                curveToRelative(-0.78f, -1.16f, 2.66f, -1.55f, 2.66f, -1.55f)
                close()
            }
            path(fill = SolidColor(Color(0xFFF28F8F))) {
                moveToRelative(109.66f, 29.86f)
                curveToRelative(-2.82f, 0.08f, -3.72f, 0.64f, -3.76f, 2.58f)
                curveToRelative(-0.05f, 2.77f, 0.13f, 3.7f, 1.19f, 3.8f)
                curveToRelative(1.25f, 0.11f, 2.17f, 0.14f, 2.17f, -0.65f)
                arcToRelative(3.04f, 3.04f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.27f, -1.76f)
                curveToRelative(-0.46f, -0.5f, -0.31f, -0.59f, 0.93f, -0.62f)
                curveToRelative(1.24f, -0.03f, 1.23f, 0.18f, 0.88f, 0.59f)
                curveToRelative(-0.25f, 0.29f, -0.18f, 1.24f, -0.15f, 1.78f)
                curveToRelative(0.03f, 0.54f, 0.17f, 0.82f, 1.95f, 0.56f)
                curveToRelative(1.52f, -0.23f, 1.22f, -1.46f, 1.07f, -3.8f)
                curveToRelative(-0.11f, -1.64f, -0.82f, -2.57f, -4.02f, -2.47f)
                close()
            }
            path(fill = SolidColor(Color(0xFF9A4A4D))) {
                moveToRelative(107.01f, 30.43f)
                lineToRelative(4.62f, 0.4f)
                arcToRelative(2.28f, 2.28f, 56.4f, isMoreThanHalf = false, isPositiveArc = true, -2.46f, 2.18f)
                arcToRelative(2.43f, 2.43f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2.17f, -2.59f)
                close()
            }
            path(fill = SolidColor(Color(0xFFF28F8F))) {
                moveToRelative(107.65f, 31.1f)
                arcToRelative(2.18f, 2.18f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.56f, 0.08f)
                arcToRelative(2.4f, 2.4f, 0f, isMoreThanHalf = false, isPositiveArc = false, 2.08f, 1.84f)
                arcToRelative(2.16f, 2.16f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.76f, -0.08f)
                arcToRelative(2.12f, 2.12f, 0f, isMoreThanHalf = false, isPositiveArc = false, -2.29f, -1.84f)
                close()
            }
            path(fill = SolidColor(primaryColor)) {
                moveToRelative(110.93f, 55.32f)
                curveToRelative(0f, 0f, 2.14f, -3.73f, 2.2f, -5.24f)
                lineToRelative(-2.37f, -2.62f)
                lineToRelative(-0.94f, 6.17f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFFFFFFF)),
                fillAlpha = 0.3f,
                strokeAlpha = 0.3f
            ) {
                moveToRelative(110.93f, 55.32f)
                curveToRelative(0f, 0f, 2.14f, -3.73f, 2.2f, -5.24f)
                lineToRelative(-2.37f, -2.62f)
                lineToRelative(-0.94f, 6.17f)
                close()
            }
            path(fill = SolidColor(primaryColor)) {
                moveTo(106.46f, 56.38f)
                lineToRelative(1.35f, -2.74f)
                lineToRelative(-4.08f, -3f)
                lineToRelative(-3.57f, 2.23f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFFFFFFF)),
                fillAlpha = 0.3f,
                strokeAlpha = 0.3f
            ) {
                moveTo(106.46f, 56.38f)
                lineToRelative(1.35f, -2.74f)
                lineToRelative(-4.08f, -3f)
                lineToRelative(-3.57f, 2.23f)
                close()
            }
            path(fill = SolidColor(primaryColor)) {
                moveToRelative(105.86f, 85.52f)
                curveToRelative(0.76f, -12.56f, 2.18f, -23.97f, 2.05f, -27.04f)
                lineToRelative(2.14f, -0.13f)
                curveToRelative(1.35f, 3.72f, 5.49f, 11.16f, 6.4f, 27.17f)
                curveToRelative(0f, 2.86f, -2.84f, 9f, -4.89f, 9f)
                curveToRelative(-2.05f, 0f, -5.82f, -7.16f, -5.71f, -9f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFFFFFFF)),
                fillAlpha = 0.6f,
                strokeAlpha = 0.6f
            ) {
                moveToRelative(105.86f, 85.52f)
                curveToRelative(0.76f, -12.56f, 2.18f, -23.97f, 2.05f, -27.04f)
                lineToRelative(2.14f, -0.13f)
                curveToRelative(1.35f, 3.72f, 5.49f, 11.16f, 6.4f, 27.17f)
                curveToRelative(0f, 2.86f, -2.84f, 9f, -4.89f, 9f)
                curveToRelative(-2.05f, 0f, -5.82f, -7.16f, -5.71f, -9f)
                close()
            }
            path(
                fill = SolidColor(primaryColor),
                fillAlpha = 0.5f,
                strokeAlpha = 0.5f
            ) {
                moveToRelative(110.22f, 58.81f)
                curveToRelative(-0.05f, -0.15f, -0.11f, -0.3f, -0.17f, -0.45f)
                lineToRelative(0f, 0f)
                lineToRelative(-2.14f, 0.13f)
                lineToRelative(0f, 0f)
                curveToRelative(0f, 0.26f, 0f, 0.59f, 0f, 0.96f)
                arcToRelative(3.67f, 3.67f, 0f, isMoreThanHalf = false, isPositiveArc = false, 2.31f, -0.64f)
                close()
            }
            path(fill = SolidColor(primaryColor)) {
                moveToRelative(107.8f, 53.64f)
                arcToRelative(9.21f, 9.21f, 135f, isMoreThanHalf = false, isPositiveArc = true, 2.02f, 0f)
                arcToRelative(4.84f, 4.84f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.11f, 1.69f)
                arcToRelative(7.26f, 7.26f, 135f, isMoreThanHalf = false, isPositiveArc = true, -0.87f, 3.04f)
                arcToRelative(2.57f, 2.57f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2.14f, 0.13f)
                arcToRelative(4.46f, 4.46f, 45f, isMoreThanHalf = false, isPositiveArc = true, -1.46f, -2.11f)
                arcToRelative(4.03f, 4.03f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.35f, -2.75f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFFFFFFF)),
                fillAlpha = 0.6f,
                strokeAlpha = 0.6f
            ) {
                moveToRelative(107.8f, 53.64f)
                arcToRelative(9.21f, 9.21f, 135f, isMoreThanHalf = false, isPositiveArc = true, 2.02f, 0f)
                arcToRelative(4.84f, 4.84f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.11f, 1.69f)
                arcToRelative(7.26f, 7.26f, 135f, isMoreThanHalf = false, isPositiveArc = true, -0.87f, 3.04f)
                arcToRelative(2.57f, 2.57f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2.14f, 0.13f)
                arcToRelative(4.46f, 4.46f, 45f, isMoreThanHalf = false, isPositiveArc = true, -1.46f, -2.11f)
                arcToRelative(4.03f, 4.03f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.35f, -2.75f)
                close()
            }
            path(fill = SolidColor(primaryColor)) {
                moveTo(80.94f, 104.12f)
                lineTo(80.64f, 109.62f)
                curveToRelative(0f, 0f, 7.8f, 8.23f, 26.53f, 7.13f)
                curveToRelative(18.73f, -1.1f, 22.24f, -9.63f, 22.24f, -9.63f)
                lineToRelative(0f, -4.84f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 0.2f,
                strokeAlpha = 0.2f
            ) {
                moveTo(80.94f, 104.12f)
                lineTo(80.64f, 109.62f)
                curveToRelative(0f, 0f, 7.8f, 8.23f, 26.53f, 7.13f)
                curveToRelative(18.73f, -1.1f, 22.24f, -9.63f, 22.24f, -9.63f)
                lineToRelative(0f, -4.84f)
                close()
            }
            path(fill = SolidColor(primaryColor)) {
                moveToRelative(93.7f, 44.43f)
                arcToRelative(131.03f, 131.03f, 0f, isMoreThanHalf = false, isPositiveArc = false, 16.52f, 20.91f)
                curveToRelative(3.8f, -12.69f, 0.68f, -18.25f, -0.81f, -22.43f)
                curveToRelative(0f, 0f, 6.26f, 0.64f, 9.08f, 1.32f)
                curveToRelative(4.11f, 1.72f, 8.78f, 1.15f, 10.7f, 33.71f)
                curveToRelative(0.18f, 3.13f, 0.66f, 15.41f, 0.84f, 21.08f)
                arcToRelative(7.61f, 7.61f, 96.2f, isMoreThanHalf = false, isPositiveArc = true, -1.84f, 5.28f)
                curveToRelative(-3.37f, 3.8f, -10.43f, 7.35f, -18.46f, 8.37f)
                curveToRelative(-9.22f, 1.16f, -22.24f, -0.66f, -27.74f, -6.33f)
                arcToRelative(5.92f, 5.92f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1.64f, -4.12f)
                lineToRelative(1.23f, -17.74f)
                lineToRelative(-3.8f, -34.18f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFFFFFFF)),
                fillAlpha = 0.15f,
                strokeAlpha = 0.15f
            ) {
                moveToRelative(90.62f, 87.68f)
                lineToRelative(6.98f, 13.32f)
                lineToRelative(6.98f, -11.48f)
                lineToRelative(-6.98f, -13.48f)
                close()
                moveTo(81.32f, 82.06f)
                lineTo(81.59f, 84.46f)
                lineTo(81.16f, 90.54f)
                lineTo(84.75f, 98.46f)
                lineTo(90.62f, 87.65f)
                lineTo(84.71f, 74.91f)
                close()
                moveTo(104.58f, 89.52f)
                lineTo(111.74f, 100.53f)
                lineTo(117.83f, 87.68f)
                lineTo(110.98f, 75.33f)
                close()
                moveTo(117.75f, 87.68f)
                lineTo(123.84f, 97.42f)
                lineTo(128.32f, 85f)
                lineTo(123.16f, 72.83f)
                close()
                moveTo(129.33f, 80.62f)
                lineTo(128.35f, 85f)
                lineTo(129.64f, 87.89f)
                curveToRelative(-0.1f, -2.71f, -0.21f, -5.31f, -0.3f, -7.27f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 0.1f,
                strokeAlpha = 0.1f
            ) {
                moveToRelative(84.37f, 82.44f)
                lineToRelative(0f, 0f)
                arcToRelative(0.24f, 0.24f, 135f, isMoreThanHalf = false, isPositiveArc = false, 0.22f, -0.14f)
                lineToRelative(6.03f, -13.1f)
                lineToRelative(6.76f, 14.32f)
                arcToRelative(0.24f, 0.24f, 135f, isMoreThanHalf = false, isPositiveArc = false, 0.22f, 0.14f)
                lineToRelative(0f, 0f)
                arcToRelative(0.24f, 0.24f, 135f, isMoreThanHalf = false, isPositiveArc = false, 0.22f, -0.14f)
                lineToRelative(6.37f, -13.69f)
                lineToRelative(6.77f, 12.93f)
                arcToRelative(0.23f, 0.23f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.23f, 0.13f)
                arcToRelative(0.24f, 0.24f, 45f, isMoreThanHalf = false, isPositiveArc = false, 0.21f, -0.15f)
                lineToRelative(5.76f, -14.12f)
                lineToRelative(6.75f, 12.57f)
                arcToRelative(0.25f, 0.25f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.23f, 0.13f)
                arcToRelative(0.26f, 0.26f, 135f, isMoreThanHalf = false, isPositiveArc = false, 0.22f, -0.16f)
                lineToRelative(4.3f, -10.89f)
                curveToRelative(0f, -0.37f, -0.06f, -0.76f, -0.1f, -1.08f)
                lineToRelative(-4.47f, 11.3f)
                lineToRelative(-6.74f, -12.57f)
                arcToRelative(0.22f, 0.22f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.23f, -0.13f)
                arcToRelative(0.27f, 0.27f, 135f, isMoreThanHalf = false, isPositiveArc = false, -0.21f, 0.15f)
                lineToRelative(-5.76f, 14.12f)
                lineToRelative(-6.76f, -12.93f)
                arcToRelative(0.24f, 0.24f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.22f, -0.14f)
                arcToRelative(0.26f, 0.26f, 45f, isMoreThanHalf = false, isPositiveArc = false, -0.21f, 0.14f)
                lineToRelative(-6.35f, 13.69f)
                lineToRelative(-6.77f, -14.27f)
                arcToRelative(0.26f, 0.26f, 135f, isMoreThanHalf = false, isPositiveArc = false, -0.23f, -0.14f)
                arcToRelative(0.21f, 0.21f, 45f, isMoreThanHalf = false, isPositiveArc = false, -0.22f, 0.14f)
                lineToRelative(-6.03f, 13.06f)
                lineToRelative(-4.07f, -8.75f)
                lineToRelative(0.17f, 1.52f)
                lineToRelative(3.68f, 7.9f)
                arcToRelative(0.25f, 0.25f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.22f, 0.15f)
                close()
                moveTo(129.92f, 95.27f)
                lineTo(128.92f, 98.57f)
                lineTo(123.91f, 90.16f)
                arcToRelative(0.26f, 0.26f, 45f, isMoreThanHalf = false, isPositiveArc = false, -0.24f, -0.12f)
                arcToRelative(0.26f, 0.26f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.21f, 0.17f)
                lineToRelative(-4.84f, 14.45f)
                lineToRelative(-6.93f, -12.53f)
                arcToRelative(0.24f, 0.24f, 135f, isMoreThanHalf = false, isPositiveArc = false, -0.22f, -0.12f)
                arcToRelative(0.24f, 0.24f, 135f, isMoreThanHalf = false, isPositiveArc = false, -0.21f, 0.14f)
                lineToRelative(-7.08f, 14.56f)
                lineToRelative(-6.38f, -13.81f)
                arcToRelative(0.26f, 0.26f, 45f, isMoreThanHalf = false, isPositiveArc = false, -0.21f, -0.14f)
                arcToRelative(0.25f, 0.25f, 135f, isMoreThanHalf = false, isPositiveArc = false, -0.22f, 0.12f)
                lineToRelative(-6.91f, 11.74f)
                lineToRelative(-5.69f, -14.41f)
                arcToRelative(0.24f, 0.24f, 45f, isMoreThanHalf = false, isPositiveArc = false, -0.21f, -0.16f)
                arcToRelative(0.29f, 0.29f, 45f, isMoreThanHalf = false, isPositiveArc = false, -0.24f, 0.12f)
                lineToRelative(-3.51f, 6.08f)
                lineToRelative(-0.08f, 1.1f)
                lineToRelative(3.8f, -6.47f)
                lineToRelative(5.68f, 14.4f)
                arcToRelative(0.24f, 0.24f, 45f, isMoreThanHalf = false, isPositiveArc = false, 0.21f, 0.16f)
                arcToRelative(0.25f, 0.25f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.23f, -0.12f)
                lineToRelative(6.96f, -11.75f)
                lineToRelative(6.35f, 13.77f)
                arcToRelative(0.26f, 0.26f, 135f, isMoreThanHalf = false, isPositiveArc = false, 0.22f, 0.14f)
                lineToRelative(0f, 0f)
                arcToRelative(0.25f, 0.25f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.22f, -0.14f)
                lineToRelative(7.1f, -14.59f)
                lineToRelative(6.96f, 12.6f)
                arcToRelative(0.24f, 0.24f, 135f, isMoreThanHalf = false, isPositiveArc = false, 0.24f, 0.12f)
                arcToRelative(0.24f, 0.24f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.21f, -0.17f)
                lineToRelative(4.85f, -14.45f)
                lineToRelative(5.03f, 8.44f)
                arcToRelative(0.26f, 0.26f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.24f, 0.12f)
                arcToRelative(0.26f, 0.26f, 45f, isMoreThanHalf = false, isPositiveArc = false, 0.21f, -0.17f)
                lineToRelative(0.76f, -2.46f)
                curveToRelative(0.01f, -0.48f, -0.05f, -0.99f, -0.06f, -1.51f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 0.2f,
                strokeAlpha = 0.2f
            ) {
                moveToRelative(110.71f, 69.63f)
                curveToRelative(5.98f, -7.4f, 4.33f, -20.53f, 1.44f, -26.39f)
                lineToRelative(-2.71f, -0.33f)
                curveToRelative(1.48f, 4.21f, 4.56f, 9.74f, 0.81f, 22.43f)
                arcToRelative(131.03f, 131.03f, 0f, isMoreThanHalf = false, isPositiveArc = true, -16.53f, -20.91f)
                lineToRelative(-2.71f, 1f)
                curveToRelative(2.27f, 5.1f, 11.85f, 19.61f, 19.71f, 24.21f)
                close()
            }
            path(fill = SolidColor(Color(0xFFF5F5F5))) {
                moveToRelative(108.15f, 53.61f)
                lineToRelative(-0.3f, 0f)
                arcToRelative(13.17f, 13.17f, 135f, isMoreThanHalf = false, isPositiveArc = false, -2.56f, -2.15f)
                curveToRelative(-0.59f, -0.29f, -2.92f, 3.92f, -3.35f, 5f)
                curveToRelative(-0.43f, 1.09f, -0.35f, 1.08f, -1.86f, 0.15f)
                arcToRelative(26.15f, 26.15f, 135f, isMoreThanHalf = false, isPositiveArc = true, -6.88f, -6.21f)
                arcToRelative(15.35f, 15.35f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2.59f, -4.87f)
                curveToRelative(0f, 0f, 1.36f, -2.72f, 1.83f, -3.63f)
                curveToRelative(0.89f, -1.74f, 1.41f, -2.44f, 2.38f, -2.33f)
                lineToRelative(0.05f, 1.11f)
                curveToRelative(0.22f, 1.48f, 1.82f, 2.86f, 3.97f, 4.56f)
                curveToRelative(2.15f, 1.7f, 5.57f, 3.33f, 6.4f, 4.08f)
                arcToRelative(29.55f, 29.55f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2.91f, 4.28f)
                close()
            }
            path(fill = SolidColor(Color(0xFFF5F5F5))) {
                moveToRelative(115.12f, 53.52f)
                curveToRelative(-0.21f, 0.62f, -2.28f, -4.08f, -3.25f, -3.95f)
                curveToRelative(-0.46f, 0.05f, -1.58f, 2.65f, -2.4f, 4.04f)
                lineToRelative(-0.15f, 0f)
                arcTo(23.83f, 23.83f, 135f, isMoreThanHalf = false, isPositiveArc = false, 110.22f, 49.23f)
                curveToRelative(0.18f, -1.16f, -0.95f, -6.84f, -0.95f, -6.84f)
                lineToRelative(0f, -1.88f)
                curveToRelative(0.3f, 0f, 0.62f, 0f, 0.9f, 0f)
                arcToRelative(5.38f, 5.38f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.76f, 0.94f)
                curveToRelative(0.98f, 1.52f, 3.51f, 4.93f, 3.88f, 6.74f)
                arcToRelative(15.55f, 15.55f, 135f, isMoreThanHalf = false, isPositiveArc = true, 0.3f, 5.33f)
                close()
            }
            path(fill = SolidColor(primaryColor)) {
                moveTo(130.24f, 114.68f)
                lineToRelative(10.65f, -32.6f)
                lineToRelative(-24.83f, 5.41f)
                lineToRelative(-10.65f, 32.59f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 0.45f,
                strokeAlpha = 0.45f
            ) {
                moveTo(130.24f, 114.68f)
                lineToRelative(10.65f, -32.6f)
                lineToRelative(-24.83f, 5.41f)
                lineToRelative(-10.65f, 32.59f)
                close()
            }
            path(fill = SolidColor(Color(0xFFF5F5F5))) {
                moveTo(116.43f, 88.22f)
                lineTo(140.59f, 83f)
                arcToRelative(7.2f, 7.2f, 45f, isMoreThanHalf = false, isPositiveArc = false, 3.04f, 3.28f)
                lineToRelative(-24.01f, 5.21f)
                curveToRelative(0f, 0f, -0.93f, -0.57f, -3.19f, -3.27f)
                close()
            }
            path(fill = SolidColor(primaryColor)) {
                moveToRelative(121.27f, 91.95f)
                arcToRelative(3.65f, 3.65f, 0f, isMoreThanHalf = false, isPositiveArc = true, -3.16f, -1.01f)
                lineToRelative(-2.28f, -2.49f)
                curveToRelative(-0.71f, -0.76f, -0.44f, -1.6f, 0.6f, -1.83f)
                lineToRelative(23.88f, -5.15f)
                lineToRelative(0.56f, 0.62f)
                lineToRelative(-23.58f, 5.14f)
                arcToRelative(0.61f, 0.61f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.36f, 1.1f)
                lineToRelative(2.09f, 2.28f)
                arcToRelative(2.2f, 2.2f, 45f, isMoreThanHalf = false, isPositiveArc = false, 1.9f, 0.61f)
                lineToRelative(23.58f, -5.12f)
                lineToRelative(0.63f, 0.7f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 0.35f,
                strokeAlpha = 0.35f
            ) {
                moveToRelative(121.27f, 91.95f)
                arcToRelative(3.65f, 3.65f, 0f, isMoreThanHalf = false, isPositiveArc = true, -3.16f, -1.01f)
                lineToRelative(-2.28f, -2.49f)
                curveToRelative(-0.71f, -0.76f, -0.44f, -1.6f, 0.6f, -1.83f)
                lineToRelative(23.88f, -5.15f)
                lineToRelative(0.56f, 0.62f)
                lineToRelative(-23.58f, 5.14f)
                arcToRelative(0.61f, 0.61f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.36f, 1.1f)
                lineToRelative(2.09f, 2.28f)
                arcToRelative(2.2f, 2.2f, 45f, isMoreThanHalf = false, isPositiveArc = false, 1.9f, 0.61f)
                lineToRelative(23.58f, -5.12f)
                lineToRelative(0.63f, 0.7f)
                close()
            }
            path(fill = SolidColor(primaryColor)) {
                moveToRelative(145.14f, 86.8f)
                lineToRelative(-10.65f, 32.59f)
                lineToRelative(-23.88f, 5.15f)
                arcToRelative(3.69f, 3.69f, 45f, isMoreThanHalf = false, isPositiveArc = true, -3.16f, -1.02f)
                lineTo(105.2f, 121.02f)
                arcToRelative(1.16f, 1.16f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.36f, -1.13f)
                lineToRelative(10.65f, -32.6f)
                arcToRelative(1.15f, 1.15f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.35f, 1.13f)
                lineToRelative(2.28f, 2.49f)
                arcToRelative(3.68f, 3.68f, 135f, isMoreThanHalf = false, isPositiveArc = false, 3.16f, 1.02f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 0.3f,
                strokeAlpha = 0.3f
            ) {
                moveToRelative(145.14f, 86.8f)
                lineToRelative(-10.65f, 32.59f)
                lineToRelative(-23.88f, 5.15f)
                arcToRelative(3.69f, 3.69f, 45f, isMoreThanHalf = false, isPositiveArc = true, -3.16f, -1.02f)
                lineTo(105.2f, 121.02f)
                arcToRelative(1.16f, 1.16f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.36f, -1.13f)
                lineToRelative(10.65f, -32.6f)
                arcToRelative(1.15f, 1.15f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.35f, 1.13f)
                lineToRelative(2.28f, 2.49f)
                arcToRelative(3.68f, 3.68f, 135f, isMoreThanHalf = false, isPositiveArc = false, 3.16f, 1.02f)
                close()
            }
            path(fill = SolidColor(primaryColor)) {
                moveToRelative(119.56f, 91.83f)
                arcToRelative(3.29f, 3.29f, 45f, isMoreThanHalf = false, isPositiveArc = true, -1.46f, -0.89f)
                lineToRelative(-2.28f, -2.49f)
                arcToRelative(1.15f, 1.15f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.35f, -1.13f)
                lineToRelative(-10.65f, 32.6f)
                arcToRelative(1.16f, 1.16f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.36f, 1.13f)
                lineToRelative(2.24f, 2.49f)
                arcToRelative(3.38f, 3.38f, 45f, isMoreThanHalf = false, isPositiveArc = false, 1.46f, 0.89f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 0.2f,
                strokeAlpha = 0.2f
            ) {
                moveToRelative(119.56f, 91.83f)
                arcToRelative(3.29f, 3.29f, 45f, isMoreThanHalf = false, isPositiveArc = true, -1.46f, -0.89f)
                lineToRelative(-2.28f, -2.49f)
                arcToRelative(1.15f, 1.15f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.35f, -1.13f)
                lineToRelative(-10.65f, 32.6f)
                arcToRelative(1.16f, 1.16f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.36f, 1.13f)
                lineToRelative(2.24f, 2.49f)
                arcToRelative(3.38f, 3.38f, 45f, isMoreThanHalf = false, isPositiveArc = false, 1.46f, 0.89f)
                close()
            }
            path(fill = SolidColor(Color(0xFFFFA8A7))) {
                moveToRelative(125.2f, 110.65f)
                arcToRelative(2.66f, 2.66f, 121.84f, isMoreThanHalf = false, isPositiveArc = false, -1.35f, -0.82f)
                curveToRelative(-0.16f, -0.05f, -0.32f, -0.08f, -0.49f, -0.11f)
                arcToRelative(16.12f, 16.12f, 0f, isMoreThanHalf = false, isPositiveArc = false, -3.09f, -0.17f)
                arcToRelative(42.76f, 42.76f, 135f, isMoreThanHalf = false, isPositiveArc = true, -6.47f, -0.23f)
                arcToRelative(25.9f, 25.9f, 45f, isMoreThanHalf = false, isPositiveArc = true, -5.06f, -1.29f)
                lineToRelative(1.1f, -3.38f)
                curveToRelative(-2.78f, -0.2f, -4.76f, -0.33f, -7.67f, 0.59f)
                curveToRelative(-1.08f, 0.34f, -3.12f, 0.72f, -4.18f, 0.34f)
                curveToRelative(-2.96f, -1.03f, -19.98f, -11.12f, -21.09f, -12.09f)
                lineToRelative(-16.45f, 6.41f)
                curveToRelative(0.76f, 5.02f, 14.45f, 10.14f, 24.89f, 14.06f)
                curveToRelative(1.41f, 0.52f, 11.74f, 3.8f, 14.94f, 4.56f)
                curveToRelative(2.14f, 0.51f, 5.23f, 1.38f, 7.41f, 1.78f)
                arcToRelative(19.52f, 19.52f, 135f, isMoreThanHalf = false, isPositiveArc = false, 4.84f, 0.29f)
                arcToRelative(20.86f, 20.86f, 45f, isMoreThanHalf = false, isPositiveArc = false, 4.76f, -0.64f)
                arcToRelative(5.71f, 5.71f, 45f, isMoreThanHalf = false, isPositiveArc = false, 3.28f, -2.33f)
                arcToRelative(7.12f, 7.12f, 45f, isMoreThanHalf = false, isPositiveArc = true, 0.65f, -1.05f)
                curveToRelative(0.36f, -0.41f, 0.87f, -0.67f, 1.19f, -1.12f)
                curveToRelative(0.43f, -0.62f, 0.35f, -1.52f, 0.76f, -2.11f)
                arcToRelative(4.88f, 4.88f, 45f, isMoreThanHalf = false, isPositiveArc = true, 1.44f, -1.04f)
                curveToRelative(0.59f, -0.36f, 1.07f, -1.06f, 0.59f, -1.66f)
                close()
            }
            path(fill = SolidColor(Color(0xFFFAFAFA))) {
                moveToRelative(79.8f, 49.54f)
                curveToRelative(-8.78f, 2.92f, -7.82f, 4.46f, -10.52f, 9.83f)
                curveToRelative(-2.58f, 5.13f, -9.06f, 32.15f, -8.85f, 39.23f)
                curveToRelative(4.72f, -3.89f, 11.14f, -5.11f, 16.44f, -5.1f)
                lineToRelative(5.55f, -16.01f)
                curveTo(84.44f, 71.86f, 86.8f, 57.9f, 79.8f, 49.54f)
                close()
            }
            path(fill = SolidColor(Color(0xFFF0F0F0))) {
                moveToRelative(78.08f, 90.95f)
                curveToRelative(0.46f, 0.24f, -0.25f, 0.96f, 0f, 1.89f)
                curveToRelative(0.25f, 0.93f, 1.05f, 1.52f, 0.85f, 2.01f)
                curveToRelative(0f, 0f, -6.08f, -0.96f, -10.32f, 2.18f)
                arcToRelative(10.48f, 10.48f, 135f, isMoreThanHalf = false, isPositiveArc = false, -4.39f, 7.45f)
                lineToRelative(1.14f, 0.76f)
                curveToRelative(0f, 0f, -0.56f, 0.6f, -2.89f, -0.76f)
                curveToRelative(-2.59f, -1.52f, -2.81f, -6.08f, -2.34f, -9.19f)
                curveToRelative(0.46f, -3.1f, 0.06f, -4.56f, 1.52f, -5.01f)
                curveToRelative(1.46f, -0.45f, 10.49f, -2.3f, 16.43f, 0.68f)
                close()
            }
        }.build()

        return _IllProfessor!!
    }

@Suppress("ObjectPropertyName")
private var _IllProfessor: ImageVector? = null
