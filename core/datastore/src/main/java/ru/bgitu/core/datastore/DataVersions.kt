package ru.bgitu.core.datastore

data class DataVersions(
    val scheduleDataVersion: Int,
    val lastForceUpdateVersion: Int,
    val currentAppVersionCode: Int,
    val newFeaturesVersion: Int,
    val guestAccountVersion: Int
)
