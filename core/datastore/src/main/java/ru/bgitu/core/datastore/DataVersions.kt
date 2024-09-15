package ru.bgitu.core.datastore

data class DataVersions(
    val scheduleDataVersion: Int,
    val currentAppVersionCode: Int,
    val newFeaturesVersion: Int,
    val userDataVersion: Int
)
