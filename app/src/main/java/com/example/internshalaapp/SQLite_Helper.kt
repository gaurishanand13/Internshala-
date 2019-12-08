package com.example.internshalaapp
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class myDataBaseHelper(context : Context) : SQLiteOpenHelper(context , "my database" , null , 1)
{

    //This function is called for the first time when the database is formed i.e when the app is executed for the first time in the device
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(loginTable.CREATE_TABLE)
        db?.execSQL(registerWorkshops.CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

}