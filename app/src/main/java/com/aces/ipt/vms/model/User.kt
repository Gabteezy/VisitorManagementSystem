package com.aces.ipt.vms.model

import android.os.Parcel
import android.os.Parcelable
import java.text.SimpleDateFormat
import java.util.Date


data class User(
    val uid: String? = null,
    val email: String? = null,
    val type: String? = null,
    val firstname: String? = null,
    val lastname: String? = null,
    val sex: String? = null,
    val address: String? = null,
    val city: String? = null,
    val state: String? = null,
    val zipCode: String? = null,
    val country: String? = null,
    val phone: String? = null,
    val verified: Boolean? = null,
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
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uid)
        parcel.writeString(email)
        parcel.writeString(type)
        parcel.writeString(firstname)
        parcel.writeString(lastname)
        parcel.writeString(sex)
        parcel.writeString(address)
        parcel.writeString(city)
        parcel.writeString(state)
        parcel.writeString(zipCode)
        parcel.writeString(country)
        parcel.writeString(phone)
        parcel.writeValue(verified)
        parcel.writeString(datetimeCreated)
        parcel.writeString(datetimeUpdated)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }


}
