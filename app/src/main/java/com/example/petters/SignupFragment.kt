package com.example.petters

import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import models.SignUpUser

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

        val login = view.findViewById<Button>(R.id.signup_create_account_click)

        login.setOnClickListener {
            fragmentManager!!
                .beginTransaction()
                .replace(R.id.auth_fragmentContainer, LoginFragment.newInstance())
                .commit()
        }

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
            else if(!email.text.toString().isEmailValid())
            {
                AlertDialog.Builder(context)
                    .setTitle("Invalid Email")
                    .setMessage("A prankster squirrel has messed up your email address. Could you check it again? ")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(R.string.alert_dialog_validation_fun) { dialog, which ->
                        // Continue with delete operation
                    }

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .show()
            }
            else if(passwordFirst.text.toString()!=passwordSecond.text.toString())
            {
                AlertDialog.Builder(context)
                    .setTitle("Passwords don't match")
                    .setMessage("You didn't only drink water, don't you? Could you type your passwords again?")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(R.string.alert_dialog_validation_fun) { dialog, which ->
                        // Continue with delete operation
                    }

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .show()
            }
            else
            {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.text.toString(),passwordFirst.text.toString())
                    .addOnFailureListener {
                        println("error")
                    }
                    .addOnSuccessListener {
                        val uid = it.user.uid
                        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
                        ref.setValue(SignUpUser(uid, name.text.toString()))

                        fragmentManager!!
                            .beginTransaction()
                            .replace(R.id.auth_fragmentContainer, CreateProfileFragment.newInstance())
                            .commit()
                    }

            }
        }
        return view    }

    fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    companion object {

        fun newInstance(): SignupFragment {
            return SignupFragment()
        }
    }
}