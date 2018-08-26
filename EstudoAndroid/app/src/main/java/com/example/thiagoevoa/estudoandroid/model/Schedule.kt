package com.example.thiagoevoa.estudoandroid.model

import android.os.Parcel
import android.os.Parcelable

data class Schedule(var _id: String? , var date: String, var initialTime: String, var finalTime: String,
                    var clientId: String, var professionalId: String) : Parcelable{
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeString(date)
        parcel.writeString(initialTime)
        parcel.writeString(finalTime)
        parcel.writeString(clientId)
        parcel.writeString(professionalId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Schedule> {
        override fun createFromParcel(parcel: Parcel): Schedule {
            return Schedule(parcel)
        }

        override fun newArray(size: Int): Array<Schedule?> {
            return arrayOfNulls(size)
        }
    }
}