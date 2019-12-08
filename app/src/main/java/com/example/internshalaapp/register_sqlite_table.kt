package com.example.internshalaapp

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log

data class Member(
    var id :Int?,
    var email : String,
    var password : String,
    var name : String
)


object loginTable {

    //First give this table a name
    val TABLE_NAME = "login"

    val CREATE_TABLE="""
        CREATE TABLE IF NOT EXISTS ${TABLE_NAME}
        (
        ID INTEGER PRIMARY KEY AUTOINCREMENT,
        EMAIL TEXT,
        PASSWORD TEXT,
        NAME TEXT
        )
    """.trimIndent()

    fun register(database : SQLiteDatabase, member: Member )
    {
        val row = ContentValues() //Here it is the row which is to be inserted into the table
        row.put("EMAIL",member.email)
        row.put("PASSWORD",member.password)
        row.put("NAME",member.name)

        //Now insert this table in the sql table
        database.insert(TABLE_NAME,null,row)
    }


    /**
     * This function is called to check whether the email with which the user is trying to login
     * is registered or not
     */
    fun search_for_login(database: SQLiteDatabase, username : String, password: String) : Boolean
    {
        var ans: Boolean = false

        val cursor : Cursor = database.query(TABLE_NAME,
            arrayOf("EMAIL","PASSWORD"), //Here we mention the columns for which cursor should get us data
            null,null,null,null,null)

        val emailCol = cursor.getColumnIndex("EMAIL")
        val passwordCol = cursor.getColumnIndex("PASSWORD")

        cursor.moveToFirst()
        while(cursor.moveToNext())
        {
            if(cursor.getString(emailCol).equals(username) && cursor.getString(passwordCol).equals(password))
            {
                ans = true
            }
        }
        return ans;
    }


    /**
     * This function is called to check whether the new registered email exist before or not
     */
    fun search_for_email(database: SQLiteDatabase, username : String) : Boolean
    {
        var ans: Boolean = false

        val cursor : Cursor = database.query(TABLE_NAME,
            arrayOf("EMAIL"),
            null,null,null,null,null)

        val emailCol = cursor.getColumnIndex("EMAIL")
        Log.i("tag2",username)
        cursor.moveToFirst()
        while(cursor.moveToNext())
        {
            if(cursor.getString(emailCol).equals(username))
            {
                Log.i("tag",username)
                ans = true
            }
        }
        return ans;
    }
}