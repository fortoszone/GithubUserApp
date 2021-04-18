package com.fort0.consumerapp

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Favorites(
    var uname: String = "",
    var userid: String = "",
    var userpic: String = ""
) : Parcelable