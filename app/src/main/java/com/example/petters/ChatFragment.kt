package com.example.petters

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import models.User

class ChatFragment(val router: Router): Fragment() {

    val selfUid = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        val recycler = view.findViewById<RecyclerView>(R.id.chat_recycler)

        val layout = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        val itemAdapter = ItemAdapter<ChatUserItem>()
        val fastAdapter = FastAdapter.with<ChatUserItem, ItemAdapter<ChatUserItem>>(itemAdapter)

        fastAdapter.withOnClickListener { _, _, adapter, _ ->
            router.goTo(Routes.CONVERSATION, adapter.user.uid)
            true
        }

        recycler.layoutManager = layout
        recycler.adapter = fastAdapter

        FirebaseDatabase.getInstance()
            .getReference("/users/$selfUid/matched")
            .addChildEventListener(object: ChildEventListener {
                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                    return
                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                    return
                }

                override fun onChildAdded(item: DataSnapshot, p1: String?) {
                    val uid = item.getValue(String::class.java)

                    if (uid == null) return

                    FirebaseDatabase.getInstance()
                        .getReference("/users")
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

                            override fun onChildAdded(data: DataSnapshot, p1: String?) {
                                val user = data.getValue(User::class.java)
                                if (user == null) return
                                if (user.uid != uid) return

                                itemAdapter.add(
                                    ChatUserItem(user)
                                )
                            }

                            override fun onChildRemoved(p0: DataSnapshot) {
                                return
                            }

                        })
                }

                override fun onChildRemoved(p0: DataSnapshot) {
                    return
                }

                override fun onCancelled(p0: DatabaseError) {
                    return
                }

            })
        return view
    }

    companion object {
        fun newInstance(router: Router): ChatFragment {
            return ChatFragment(router)
        }
    }
}