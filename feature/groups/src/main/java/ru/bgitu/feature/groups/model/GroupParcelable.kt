package ru.bgitu.feature.groups.model

import android.os.Parcel
import android.os.Parcelable
import ru.bgitu.core.model.Group

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

internal fun GroupParcelable.toExternalModel() = Group(id = id, name = name)