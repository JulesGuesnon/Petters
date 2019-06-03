package com.example.petters

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val router = Router()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_dog_main_color_24dp)

        router.supportFragmentManager = supportFragmentManager
        router.supportActionBar = supportActionBar!!
        router.fragmentContainer = fragmentContainer

        supportFragmentManager
            .beginTransaction()
            .add(fragmentContainer.id, MainFragment.newInstance())
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        router.menu = menu!!
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item?.itemId ==  R.id.navChat) {
            router.goTo("/chat")
        }
        else {
           when(router.route) {
               "/" -> router.goTo("/profile")
               "/profile" -> router.goTo("/")
               "/chat" -> router.goTo("/")
               "/conversation" -> router.goTo("/chat")
           }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Toast.makeText(this, "TODO", Toast.LENGTH_SHORT).show()
    }
}