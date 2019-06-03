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
import android.content.Intent
import android.text.TextUtils
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_auth.*


class LoginFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val email = view.findViewById<EditText>(R.id.login_email_adress_field)
        val password = view.findViewById<EditText>(R.id.login_password_field)
        val emailEmpty = view.findViewById<TextView>(R.id.login_no_email)
        val passwordEmpty = view.findViewById<TextView>(R.id.login_no_password)
        val createAccount = view.findViewById<TextView>(R.id.login_create_account_click)

        emailEmpty.visibility= View.INVISIBLE
        passwordEmpty.visibility= View.INVISIBLE

        createAccount.setOnClickListener {
            fragmentManager!!
                .beginTransaction()
                .replace(R.id.auth_fragmentContainer, SignupFragment.newInstance())
                .commit()
        }

        view.findViewById<Button>(R.id.login_validation_button).setOnClickListener{
            if(email.text.isEmpty()){
                emailEmpty.visibility= View.VISIBLE
            }
            if(password.text.isEmpty()){
                passwordEmpty.visibility= View.VISIBLE
            }
            else if(!email.text.toString().isEmailValid())
            {
                AlertDialog.Builder(context)
                    .setTitle("Invalid Email")
                    .setMessage("A prankster squirrel has messed up your email address. Could you check it again?")

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
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email.text.toString(),password.text.toString())
                    .addOnSuccessListener{
                        val intent = Intent(activity, MainActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                    .addOnFailureListener{
                        Toast.makeText(activity, "Connection failed...", Toast.LENGTH_LONG).show()
                    }
            }
        }
        return view
    }

    fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }


    companion object {

        fun newInstance(): LoginFragment {
            return LoginFragment()
        }
    }
}