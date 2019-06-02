package com.example.petters

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var menu: Menu

    var route = "/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         setSupportActionBar(findViewById(R.id.my_toolbar))

        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_dog_main_color_24dp)
        }
        supportFragmentManager
            .beginTransaction()
            .add(fragmentContainer.id, MainFragment.newInstance())
            .addToBackStack("init")
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        this.menu = menu!!
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item?.itemId ==  R.id.navChat) {
            route = "/chat"
            setBackMenu()
        }
        else {
           when(route) {
               "/" -> {
                   route = "/profile"
                   setBackMenu()
               }
               "/chat" -> {
                   route = "/"
                   setHomeMenu()
               }
               "/inChat" -> {
                   route = "/chat"
               }
               "/profile" -> {
                   route = "/"
                   setHomeMenu()
               }
           }
        }

        return super.onOptionsItemSelected(item)
    }

    fun setBackMenu() {
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_main_color_24dp)
        menu.findItem(R.id.navChat).isVisible = false
    }

    fun setHomeMenu() {
        menu.findItem(R.id.navChat).isVisible = true
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_dog_main_color_24dp)
    }
}
