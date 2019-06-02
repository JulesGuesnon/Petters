package com.example.petters

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import models.User

class ChatFragment(val router: Router): Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        val recycler = view.findViewById<RecyclerView>(R.id.chat_recycler)

        val layout = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        val itemAdapter = ItemAdapter<ChatUserItem>()
        val fastAdapter = FastAdapter.with<ChatUserItem, ItemAdapter<ChatUserItem>>(itemAdapter)

        fastAdapter.withOnClickListener { _, _, adapter, _ ->
            router.goTo("/conversation")
            true
        }

        recycler.layoutManager = layout
        recycler.adapter = fastAdapter

        itemAdapter.add(
            ChatUserItem(User("azezaeaze", "Jules"))
        )

        itemAdapter.add(
            ChatUserItem(User("azezaeaze", "Yoan"))
        )

        itemAdapter.add(
            ChatUserItem(User("azezaeaze", "Baton"))
        )
        return view
    }

    companion object {
        fun newInstance(router: Router): ChatFragment {
            return ChatFragment(router)
        }
    }
}