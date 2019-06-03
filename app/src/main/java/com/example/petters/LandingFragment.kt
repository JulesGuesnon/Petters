package com.example.petters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class LandingFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_landing, container, false)

        view.findViewById<Button>(R.id.landing_login).setOnClickListener {
            fragmentManager!!
                .beginTransaction()
                .replace(R.id.auth_fragmentContainer, LoginFragment.newInstance())
                .commit()
        }

        view.findViewById<Button>(R.id.landing_create_account).setOnClickListener {
            fragmentManager!!
                .beginTransaction()
                .replace(R.id.auth_fragmentContainer, SignupFragment.newInstance())
                .commit()
        }
        return view
    }

    companion object {

        fun newInstance(): LandingFragment {
            return LandingFragment()
        }
    }
}