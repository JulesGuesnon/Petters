package com.example.petters

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        FirebaseAuth.getInstance().signOut()
        val isAuth = !(FirebaseAuth.getInstance().currentUser?.isAnonymous ?: true)
        if (isAuth) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        supportFragmentManager
            .beginTransaction()
            .add(auth_fragmentContainer.id, LoginFragment.newInstance())
            .commit()
    }
}