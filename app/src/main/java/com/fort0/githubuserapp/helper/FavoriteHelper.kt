package com.fort0.githubuserapp.helper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.fort0.githubuserapp.db.DatabaseUser.UserColumns.Companion.TABLE_NAME
import com.fort0.githubuserapp.db.DatabaseUser.UserColumns.Companion.USERNAME
import java.sql.SQLException

class FavoriteHelper(context: Context) {
    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: FavoriteHelper? = null
        private lateinit var dataBaseHelper: DatabaseHelper
        lateinit var database: SQLiteDatabase

        fun getInstance(context: Context): FavoriteHelper = INSTANCE ?: synchronized(this) {
            INSTANCE ?: FavoriteHelper(context)
        }
    }

    init {
        dataBaseHelper = DatabaseHelper(context)
    }

    private var INSTANCE: FavoriteHelper? = null
    fun getInstance(context: Context): FavoriteHelper =
        INSTANCE ?: synchronized(this) {
            INSTANCE ?: FavoriteHelper(context)
        }

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }

    fun close() {
        dataBaseHelper.close()
        if (database.isOpen)
            database.close()
    }

    fun queryAll(): Cursor {
        open()
        return database.rawQuery(
            "SELECT $USERNAME FROM $DATABASE_TABLE ORDER BY $USERNAME ASC",
            null
        )
    }

    fun queryById(id: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$USERNAME = ?",
            arrayOf(id),
            null,
            null,
            null,
            null
        )
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(
            DATABASE_TABLE,
            null,
            values
        )
    }

    fun update(id: String, values: ContentValues?): Int {
        return database.update(
            DATABASE_TABLE,
            values,
            "$USERNAME = ?",
            arrayOf(id)
        )
    }

    fun deleteById(id: String): Int {
        return database.delete(
            DATABASE_TABLE,
            "$USERNAME = '$id'",
            null
        )
    }
}