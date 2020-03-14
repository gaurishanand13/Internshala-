package com.example.internshalaapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_dashboard_activity.*
import java.lang.reflect.Array

class Dashboard_activity : AppCompatActivity() {

    var workshopsRegistered  = arrayOf(false,false,false,
        false,false,false,
        false,false,false,
        false,false,false,
        false,false)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_activity)

        val databaseHelper = myDataBaseHelper(applicationContext)
        val db = databaseHelper.writableDatabase

        val sharedPref= getSharedPreferences("myPref", Context.MODE_PRIVATE)
        val login = sharedPref!!.getString("login","no")
        if(login.equals("yes"))
        {
            val username = sharedPref?.getString("username","")
            registerWorkshops.getAllWorkShops(db,username!!,workshopsRegistered)
        }
        var count = 0
        var pos = 0
        var string = ""
        for(x in workshopsRegistered)
        {
            if(x==true)
            {
                count++
                Log.i("dashboard s", pos.toString())
                string = string + pos
            }
            pos++
        }
        Log.i("dashboard",workshopsRegistered.toString())
        Log.i("dashboard",count.toString())
        Log.i("dashboard",string + "hello")
        dashboardRecyclerView.layoutManager = LinearLayoutManager(this)
        dashboardRecyclerView.adapter = myAdapter2(string,count)

    }
}
