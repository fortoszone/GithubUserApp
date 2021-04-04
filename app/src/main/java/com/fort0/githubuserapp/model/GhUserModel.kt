package com.fort0.githubuserapp.model

import android.os.Parcelable
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GhUserModel(
    val fname: String = "",
    var uname: String = "",
    var userpic: String = "",
    var userid: String = "",
    val location: String = "",
    val company: String = "",
    val following: String = "",
    val followers: String = "",
    val repository: String = ""
) : Parcelable