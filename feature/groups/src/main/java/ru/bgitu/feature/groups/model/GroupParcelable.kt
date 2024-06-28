package ru.bgitu.feature.groups.model

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import ru.bgitu.core.model.Group

internal const val RESULT_PRIMARY_GROUP = "result_primary_group"
internal const val RESULT_SECONDARY_GROUP = "result_secondary_group"

internal data class GroupParcelable(
    val id: Int,
    val name: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        checkNotNull(parcel.readString()) { "Group name can't be null" }
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GroupParcelable> {
        override fun createFromParcel(parcel: Parcel): GroupParcelable {
            return GroupParcelable(parcel)
        }

        override fun newArray(size: Int): Array<GroupParcelable?> {
            return arrayOfNulls(size)
        }
    }
}

private fun GroupParcelable.toExternalModel() = Group(id = id, name = name)

fun SavedStateHandle.setPrimaryGroup(group: Group) {
    set(RESULT_PRIMARY_GROUP, GroupParcelable(id = group.id, name = group.name))
}

fun SavedStateHandle.getPrimaryGroup(): Group? {
    return get<GroupParcelable?>(RESULT_PRIMARY_GROUP)?.toExternalModel()
}

fun SavedStateHandle.setSecondaryGroup(group: Group) {
    set(RESULT_SECONDARY_GROUP, GroupParcelable(id = group.id, name = group.name))
}

fun SavedStateHandle.getSecondaryGroup(): Group? {
    return get<GroupParcelable?>(RESULT_SECONDARY_GROUP)?.toExternalModel()
}