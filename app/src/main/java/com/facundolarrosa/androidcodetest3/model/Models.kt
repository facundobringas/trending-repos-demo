package com.facundolarrosa.androidcodetest3.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class ApiResult<T>(val totalCount: Int,
                        val incompleteResults: Boolean,
                        val items: List<T> )

data class Repo(val name: String,
                val fullName: String,
                val owner: Owner,
                val description: String?,
                val language: String,
                @SerializedName("stargazers_count") val stargazersCount: Int,
                val watchers: Int,
                val forks: Int,
                val homepage: String?,
                val cloneUrl: String,
                val htmlUrl: String) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(Owner::class.java.classLoader),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(fullName)
        parcel.writeParcelable(owner, flags)
        parcel.writeString(description)
        parcel.writeString(language)
        parcel.writeInt(stargazersCount)
        parcel.writeInt(watchers)
        parcel.writeInt(forks)
        parcel.writeString(homepage)
        parcel.writeString(cloneUrl)
        parcel.writeString(htmlUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Repo> {
        override fun createFromParcel(parcel: Parcel): Repo {
            return Repo(parcel)
        }

        override fun newArray(size: Int): Array<Repo?> {
            return arrayOfNulls(size)
        }
    }
}

data class Owner(val login: String,
                 @SerializedName("avatar_url") val avatarUrl: String ) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(login)
        parcel.writeString(avatarUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Owner> {
        override fun createFromParcel(parcel: Parcel): Owner {
            return Owner(parcel)
        }

        override fun newArray(size: Int): Array<Owner?> {
            return arrayOfNulls(size)
        }
    }
}