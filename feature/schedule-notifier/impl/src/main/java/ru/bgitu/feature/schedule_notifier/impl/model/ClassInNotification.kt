package ru.bgitu.feature.schedule_notifier.impl.model

data class ClassInNotification(
    val subject: String,
    val location: String,
    val startTime: String,
    val endTime: String? = null,
    val shortMessage: String,
    val startsSoon: Boolean = false
) {
    constructor(
        shortMessage: String,
        fullMessage: String
    ) : this(fullMessage, "", "", null, shortMessage)

    fun isEmpty() = this.subject.isEmpty() && this.location.isEmpty()

    companion object {
        val Empty: ClassInNotification
            get() = ClassInNotification("", "")
    }
}