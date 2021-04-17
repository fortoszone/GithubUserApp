package com.fort0.githubuserapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GhUserModel(
    var uname: String = "",
    val fname: String = "",
    var userpic: String = "",
    var userid: String = "",
    val location: String = "",
    val company: String = "",
    val following: String = "",
    val followers: String = "",
    val repository: String = ""
) : Parcelable