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
            val username = view.usernameEditText.text.toString().trim()
            val password = view.passwordEditText.text.toString().trim()
            if(username.equals("") || password.equals(""))
            {
                Toast.makeText(context,"Please fill username and password both", Toast.LENGTH_SHORT).show()
            }
            else
            {
                val databaseHelper = myDataBaseHelper(context!!)
                val db = databaseHelper.writableDatabase
                if(loginTable.search_for_login(db,username,password))
                {
                    //First update this information in the sharedPreferences.
                    Finterface?.updateLogin(username)

                }
                else
                {
                    Toast.makeText(context,"Incorrect username or password", Toast.LENGTH_SHORT).show()
                }
            }
        }

        /**
         * This is for the registrer button of the register ID page
         */
        view.registerButton.setOnClickListener {

            if(view.usernameRegisterEditText.text.toString().contains(' ') || view.passwordRegisterEditText.text.toString().contains(' '))
            {
                Toast.makeText(context,"Username and password can't contain spaces ",Toast.LENGTH_SHORT).show()
            }
            else if(view.usernameRegisterEditText.text.toString().equals("") || view.passwordRegisterEditText.text.toString().equals("") || view.nameRegisterEditText.text.toString().equals(""))
            {
                Toast.makeText(context,"Please Enter all the above fields",Toast.LENGTH_SHORT).show()
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
