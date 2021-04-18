package com.fort0.consumerapp

import android.net.Uri
import android.provider.BaseColumns

object Database {
    const val AUTH = "com.fort0.githubuserapp"
    const val SCHEME = "content"

    internal class UserColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "user"
            const val USERNAME = "login"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTH)
                .appendPath(TABLE_NAME)
                .build()

        }
    }
}