package com.example.thiagoevoa.estudoandroid.model

import android.os.Parcel
import android.os.Parcelable

data class Professional(var _id: String?, var cpf_cnpj: String?, var name: String?) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeString(cpf_cnpj)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Professional> {
        override fun createFromParcel(parcel: Parcel): Professional {
            return Professional(parcel)
        }

        override fun newArray(size: Int): Array<Professional?> {
            return arrayOfNulls(size)
        }
    }
}