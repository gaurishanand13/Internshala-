package com.example.internshalaapp

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import java.text.FieldPosition

object registerWorkshops {

    //First give this table a name
    val TABLE_NAME = "workshop"

    val CREATE_TABLE="""
        CREATE TABLE IF NOT EXISTS ${TABLE_NAME}
        (
        ID INTEGER PRIMARY KEY AUTOINCREMENT,
        EMAIL TEXT,
        WORKSHOPS TEXT
        )
    """.trimIndent()

    fun register(database : SQLiteDatabase, username: String ,  position: Int )
    {
        //first find the previously registered workshops if they exist
        val cursor : Cursor = database.query(TABLE_NAME,
            arrayOf("ID","WORKSHOPS"), //Here we mention the columns for which cursor should get us data
            "EMAIL = ?", arrayOf(username),null,null,null)

        var string = ""
        var id = 0
        val workshopsCol = cursor.getColumnIndex("WORKSHOPS")
        var idCol = cursor.getColumnIndex("ID")
        cursor.moveToFirst()

        while (cursor.moveToNext())
        {
            string = string + cursor.getString(workshopsCol)
            id = cursor.getInt((idCol))
        }

        val row = ContentValues()
        row.put("EMAIL",username)
        row.put("WORKSHOPS",string + position)
        Log.i("row",string + "hello")
        val c = position.toChar()
        if(string.equals(""))
        {
            Log.i("row","reached here")
            database.insert(TABLE_NAME,null,row)
        }
        else if(string.contains(c))
        {
            //Then don't need to add it again
        }
        else
        {
            database.update(TABLE_NAME,row,"ID = ?", arrayOf(id.toString()))
        }
    }

    fun deRegister(database : SQLiteDatabase, username: String ,  position: Int )
    {
        //first find the previously registered all the workshops
        val cursor : Cursor = database.query(TABLE_NAME,
            arrayOf("ID","WORKSHOPS"), //Here we mention the columns for which cursor should get us data
            "EMAIL = ?", arrayOf(username),null,null,null)

        var string = ""
        var id = 0
        val workshopsCol = cursor.getColumnIndex("WORKSHOPS")
        var idCol = cursor.getColumnIndex("ID")
        cursor.moveToFirst()

        while (cursor.moveToNext())
        {
            string = string + cursor.getString(workshopsCol)
            id = cursor.getInt((idCol))
        }
        string = string.replace("${position}","")
        val row = ContentValues()
        row .put("EMAIL",username)
        row.put("WORKSHOPS",string)

        database.update(TABLE_NAME,row,"ID = ?", arrayOf(id.toString()))
    }

    fun getAllWorkShops(database : SQLiteDatabase, username: String, workshops : Array<Boolean>)
    {
        val cursor : Cursor = database.query(TABLE_NAME,
            arrayOf("WORKSHOPS"), //Here we mention the columns for which cursor should get us data
            "EMAIL = ?", arrayOf(username),null,null,null)

        var string = ""
        val workshopsCol = cursor.getColumnIndex("WORKSHOPS")
        cursor.moveToFirst()
        while (cursor.moveToNext())
        {
            string = string + cursor.getString(workshopsCol)
        }
        for(char in string)
        {
            val m = char - '0'
            workshops[m] = true
        }
    }
}