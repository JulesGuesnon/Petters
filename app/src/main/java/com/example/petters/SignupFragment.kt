package com.example.petters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment

class SignupFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_signup, container, false)

        val name = view.findViewById<EditText>(R.id.signup_fullname_field)
        val nameEmpty = view.findViewById<TextView>(R.id.signup_no_name)

        val email = view.findViewById<EditText>(R.id.signup_email_adress_field)
        val emailEmpty = view.findViewById<TextView>(R.id.signup_no_email)

        val passwordFirst = view.findViewById<EditText>(R.id.signup_password_field)
        val passwordFirstEmpty = view.findViewById<TextView>(R.id.signup_no_password_first)

        val passwordSecond = view.findViewById<EditText>(R.id.signup_password_confirmation_field)
        val passwordSecondEmpty = view.findViewById<TextView>(R.id.signup_no_password_second)

        nameEmpty.visibility = View.INVISIBLE
        emailEmpty.visibility = View.INVISIBLE
        passwordFirstEmpty.visibility = View.INVISIBLE
        passwordSecondEmpty.visibility = View.INVISIBLE
        view.findViewById<Button>(R.id.signup_validation_button).setOnClickListener{
            if(name.text.isEmpty()){
                nameEmpty.visibility = View.VISIBLE
            }
            if(email.text.isEmpty()){
                emailEmpty.visibility = View.VISIBLE
            }
            if(passwordFirst.text.isEmpty()){
                passwordFirstEmpty.visibility = View.VISIBLE
            }
            if(passwordSecond.text.isEmpty()){
                passwordSecondEmpty.visibility = View.VISIBLE
            }
        }
        return view    }

    companion object {

        fun newInstance(): SignupFragment {
            return SignupFragment()
        }
    }
}