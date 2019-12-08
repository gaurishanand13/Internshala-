package com.example.internshalaapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_workshop_activity.*
import kotlinx.android.synthetic.main.item_view.view.*
import java.lang.StringBuilder

class workshop_activity : AppCompatActivity() , FragmentInterface
{
    var adapter : myAdapter? = null
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.dashboard->
            {
                startActivity(Intent(this,Dashboard_activity::class.java))
            }
            R.id.logout->
            {
                sharedPref?.edit()?.putString("login","no")?.commit()
                Toast.makeText(this,"Logout Done",Toast.LENGTH_SHORT).show()
                login = "no"
                for(x in 0..10)
                {
                    workshopsRegistered[x] = false
                }
                adapter?.notifyDataSetChanged()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    var login : String? = "no"
    var sharedPref : SharedPreferences? = null


    var workshopsRegistered  = arrayOf(false,false,false,
        false,false,false,
        false,false,false,
        false,false,false,
        false,false)

    /**
     * All the Methods of fragments are rolled here
     */
    override fun updateLogin(username: String) {
        sharedPref?.edit()?.putString("login","yes")?.commit()
        sharedPref?.edit()?.putString("username",username)?.commit()
        login = "yes"
    }

    override fun login_done() {
        fragmentCantainer.visibility = View.GONE
        login = "yes"
        val username = sharedPref?.getString("username","")
        val databaseHelper = myDataBaseHelper(applicationContext)
        val db = databaseHelper.writableDatabase
        registerWorkshops.getAllWorkShops(db,username!!,workshopsRegistered)
        adapter?.notifyDataSetChanged()
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workshop_activity)

        val databaseHelper = myDataBaseHelper(applicationContext)
        val db = databaseHelper.writableDatabase

        sharedPref= getSharedPreferences("myPref",Context.MODE_PRIVATE)
        login = sharedPref!!.getString("login","no")
        Log.i("login",login)
        if(login.equals("yes"))
        {
            val username = sharedPref?.getString("username","")
            registerWorkshops.getAllWorkShops(db,username!!,workshopsRegistered)
            Log.i("tag",workshopsRegistered.toString())
        }

        workshopRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = myAdapter(this,workshopsRegistered)
        workshopRecyclerView.adapter = adapter

        adapter?.myInterface = object : adapterInterface{

            override fun update_color(itemView: View) {
                itemView.registerWorkshopButtonTextView.text = "REGISTERED"
                itemView.registerWorkshopButton.setCardBackgroundColor(resources.getColor(android.R.color.holo_green_light))
            }

            override fun update_color2(itemView: View) {
                itemView.registerWorkshopButtonTextView.text = "REGISTER"
                itemView.registerWorkshopButton.setCardBackgroundColor(resources.getColor(android.R.color.holo_blue_dark))

            }

            override fun register_workshop(position: Int, itemView: View) {

                //First check if the user is already logged in or not
                val username = sharedPref?.getString("username","")

                if(login.equals("yes"))
                {
                    //It means the user has already logged in. Therefore just register this workshop to user in the database
                    registerWorkshops.register(db,username!!,position)
                    workshopsRegistered[position] = true
                    adapter?.notifyDataSetChanged()
                    Toast.makeText(applicationContext,"Workshop registered",Toast.LENGTH_SHORT).show()
                }
                else
                {
                    //Now since we are not login We should use fragments to get it registered
                    val fragmentManager = supportFragmentManager
                    fragmentCantainer.visibility = View.VISIBLE
                    fragmentManager.beginTransaction().replace(R.id.fragmentCantainer,loginFragment()).commit()
                }

            }

            override fun unregister_workshop(position: Int, itemView: View) {
                val username = sharedPref?.getString("username","")
                registerWorkshops.deRegister(db,username!!,position)
                workshopsRegistered[position] = false
                adapter?.notifyDataSetChanged()
                Toast.makeText(applicationContext,"Workshop unregistered",Toast.LENGTH_SHORT).show()
            }
        }
    }
}
