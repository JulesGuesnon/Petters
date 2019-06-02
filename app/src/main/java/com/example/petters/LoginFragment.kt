package com.example.petters

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import android.content.DialogInterface
import android.widget.TextView


class LoginFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        val email = view.findViewById<EditText>(R.id.login_email_adress_field)
        val password = view.findViewById<EditText>(R.id.login_password_field)
        val emailEmpty = view.findViewById<TextView>(R.id.login_no_email)
        val passwordEmpty = view.findViewById<TextView>(R.id.login_no_password)
        emailEmpty.visibility= View.INVISIBLE
        passwordEmpty.visibility= View.INVISIBLE
        view.findViewById<Button>(R.id.login_validation_button).setOnClickListener{
            if(email.text.isEmpty()){
                emailEmpty.visibility= View.VISIBLE
            }
            if(password.text.isEmpty()){
                passwordEmpty.visibility= View.VISIBLE
            }
        }
        return view
    }
    companion object {

        fun newInstance(): LoginFragment {
            return LoginFragment()
        }
    }
}