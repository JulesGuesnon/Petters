package com.example.petters

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import models.Message

class ConversationFragment(otherUid: String): Fragment() {

    val selfUid = FirebaseAuth.getInstance().currentUser?.uid
    val otherUid = otherUid

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_conversation, container, false)
        val msgInput = view.findViewById<EditText>(R.id.conversation_input)
        val btn = view.findViewById<ImageButton>(R.id.conversation_btn)
        val recycler = view.findViewById<RecyclerView>(R.id.conversation_recycler)

        val layout = LinearLayoutManager(activity, RecyclerView.VERTICAL, true)
        val itemAdapter = ItemAdapter<ConversationItem>()
        val fastAdapter = FastAdapter.with<ConversationItem, ItemAdapter<ConversationItem>>(itemAdapter)

        recycler.adapter = fastAdapter
        recycler.layoutManager = layout

        FirebaseDatabase
            .getInstance()
            .getReference("/messages")
            .addChildEventListener(object: ChildEventListener {
                override fun onCancelled(p0: DatabaseError) {
                   return
                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                    return
                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                    return
                }

                override fun onChildAdded(item: DataSnapshot, p1: String?) {
                    val msg = item.getValue(Message::class.java)

                    if (msg == null) return

                    if (
                        (msg.toUid == selfUid && msg.fromUid == otherUid) ||
                        (msg.toUid == otherUid && msg.fromUid == selfUid)
                    ) {
                        println(msg.content)

                        itemAdapter.add(
                            0,
                            ConversationItem(msg)
                        )
                    }

                    return
                }

                override fun onChildRemoved(p0: DataSnapshot) {
                    return
                }

            })


        btn.setOnClickListener {
            sendMessage(msgInput)
        }
        return view
    }

    private fun sendMessage (msgInput: EditText) {
        if (selfUid == null) return

        FirebaseDatabase
            .getInstance()
            .getReference("/messages")
            .push()
            .setValue(Message(selfUid, otherUid, msgInput.text.toString(), System.currentTimeMillis()))

        msgInput.text = SpannableStringBuilder("")
    }

    companion object {
        fun newInstance(otherUid: String): ConversationFragment {
            return ConversationFragment(otherUid)
        }
    }
}