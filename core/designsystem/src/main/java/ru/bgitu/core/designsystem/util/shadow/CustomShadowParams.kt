package ru.bgitu.core.designsystem.util.shadow

/**
 * Композитная тень дизайн системы
 *
 * @param name - название тени
 * @param layers - список теней
 */
data class CustomShadowParams(
    val name: String,
    val layers: List<Shadow>
)