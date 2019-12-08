package com.example.internshalaapp


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_login.view.*

/**
 * A simple [Fragment] subclass.
 */

public interface FragmentInterface
{
    fun updateLogin(username : String)
    fun login_done()
}
class loginFragment : Fragment() {


    var Finterface : FragmentInterface? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Finterface = context as FragmentInterface
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        view.buttonLogin.setOnClickListener {
            if(view.usernameEditText.text.toString().equals("") || view.passwordEditText.text.toString().equals(""))
            {
                Toast.makeText(context,"Please fill username and password both", Toast.LENGTH_SHORT).show()
            }
            else
            {
                val databaseHelper = myDataBaseHelper(context!!)
                val db = databaseHelper.writableDatabase
                if(loginTable.search_for_login(db,view.usernameEditText.text.toString(),view.passwordEditText.text.toString()))
                {
                    //First update this information in the sharedPreferences.
                    Finterface?.updateLogin(view.usernameEditText.text.toString())
                    //Therefore now we are logged in. Make this fragment container close. And we will update all the registered workshops
                    Finterface?.login_done()

                }
                else
                {
                    Toast.makeText(context,"Incorrect username or password", Toast.LENGTH_SHORT).show()
                }
            }
        }

        view.registerButton.setOnClickListener {
            if(view.usernameRegisterEditText.text.toString().equals("") || view.passwordRegisterEditText.text.toString().equals("") || view.nameRegisterEditText.text.toString().equals(""))
            {
                Toast.makeText(context,"Please fill all the fields",Toast.LENGTH_SHORT).show()
            }
            else
            {
                val databaseHelper = myDataBaseHelper(context!!)
                val db = databaseHelper.writableDatabase //Therefore here db is the data which will store the data
                //First check if the username entered is unique or not
                if(loginTable.search_for_email(db,view.usernameRegisterEditText.text.toString())==false)
                {
                    loginTable.register(db,
                        Member(null,view.usernameRegisterEditText.text.toString(),view.passwordRegisterEditText.text.toString(),view.nameRegisterEditText.text.toString())
                    )
                    Finterface?.updateLogin(view.usernameRegisterEditText.text.toString())
                    Finterface?.login_done()
                }
                else
                {
                    Toast.makeText(context,"Please chose another username.\nThis username is already chosen ",Toast.LENGTH_SHORT).show()
                }
            }
        }

        view.buttonRegister.setOnClickListener {
            view.registerLinearLayout.visibility = View.VISIBLE
        }
    }

}
