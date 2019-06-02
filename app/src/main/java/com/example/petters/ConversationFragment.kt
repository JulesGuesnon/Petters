package com.example.petters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment

class ConversationFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_conversation, container, false)
        val msgInput = view.findViewById<EditText>(R.id.conversation_input)
        val btn = view.findViewById<ImageButton>(R.id.conversation_btn)

        btn.setOnClickListener {
            sendMessage(msgInput.text.toString())
        }
        return view
    }

    private fun sendMessage (msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
    }

    companion object {
        fun newInstance(): ConversationFragment {
            return ConversationFragment()
        }
    }
}