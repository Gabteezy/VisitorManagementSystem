package com.aces.ipt.vms.model

import android.os.Parcel
import android.os.Parcelable
import java.text.SimpleDateFormat
import java.util.Date


data class Appointment(
    val uid: String? = null,
    val appId: String? = null,
    val appNumber: String? = null,
    val appDate: String? = null,
    val appTime: String? = null,
    val appType: String? = null,
    val datetimeCreated: String? = SimpleDateFormat("yyyy-MM-d HH:mm:ss").format(Date()),
    val datetimeUpdated: String? = SimpleDateFormat("yyyy-MM-d HH:mm:ss").format(Date()),): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uid)
        parcel.writeString(appId)
        parcel.writeString(appNumber)
        parcel.writeString(appDate)
        parcel.writeString(appTime)
        parcel.writeString(appType)
        parcel.writeString(datetimeCreated)
        parcel.writeString(datetimeUpdated)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Appointment> {
        override fun createFromParcel(parcel: Parcel): Appointment {
            return Appointment(parcel)
        }

        override fun newArray(size: Int): Array<Appointment?> {
            return arrayOfNulls(size)
        }
    }
}