package com.example.petters

import android.os.Bundle
import android.view.Menu
import android.widget.LinearLayout
import androidx.fragment.app.FragmentManager
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*


class Router {
    var route = "/"
    lateinit var menu: Menu
    lateinit var supportActionBar: ActionBar
    lateinit var supportFragmentManager: FragmentManager
    lateinit var fragmentContainer: LinearLayout

    fun goTo(newRoute: String, arg: Any = "")  {
        when(newRoute) {
            "/" -> {
                if (route == "/profile")
                    goHome(false)
                else
                    goHome()
            }
            "/chat" -> {
                if (route == "/conversation")
                    goChat()
                else
                    goChat(false)

            }
            "/profile" -> {
                setBackMenu()
                supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left)
                    .replace(fragmentContainer.id, ProfileFragment.newInstance())
                    .addToBackStack("test")
                    .commit()
            }
            "/conversation" -> {
                supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                    .replace(fragmentContainer.id, ConversationFragment.newInstance(arg.toString()))
                    .addToBackStack("test")
                    .commit()
            }

        }

        route = newRoute
    }

    fun setBackMenu() {
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_main_color_24dp)
        menu.findItem(R.id.navChat).isVisible = false
    }

    fun setHomeMenu() {
        menu.findItem(R.id.navChat).isVisible = true
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_dog_main_color_24dp)
    }

    private fun goHome(fromRight: Boolean = true) {
        setHomeMenu()
        if (!fromRight) {
            supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(fragmentContainer.id, MainFragment.newInstance())
                .addToBackStack("test")
                .commit()
        }
        else {
            supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left)
                .replace(fragmentContainer.id, MainFragment.newInstance())
                .addToBackStack("test")
                .commit()
        }
    }

    private fun goChat(fromRight: Boolean = true) {
        setBackMenu()
        if (!fromRight) {
            supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(fragmentContainer.id, ChatFragment.newInstance(this))
                .addToBackStack("test")
                .commit()
        }
        else {
            supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left)
                .replace(fragmentContainer.id, ChatFragment.newInstance(this))
                .addToBackStack("test")
                .commit()
        }
    }

}