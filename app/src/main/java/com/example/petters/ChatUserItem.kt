package com.example.petters

import android.view.View
import android.widget.TextView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import models.User

class ChatUserViewHolder(itemView: View): FastAdapter.ViewHolder<ChatUserItem>(itemView) {

    val lastMsg: TextView
    val username: TextView
    val time: TextView

    init {
        lastMsg = itemView.findViewById(R.id.chat_message)
        username = itemView.findViewById(R.id.chat_username)
        time = itemView.findViewById(R.id.chat_time)
    }

    override fun bindView(item: ChatUserItem, payloads: MutableList<Any>) {
        username.text = item.user.name.toString()
        lastMsg.text = (Math.random() * 12).toString()
        time.text = (Math.random() * 100).toString()
    }

    override fun unbindView(item: ChatUserItem) {
        lastMsg.text = ""
        username.text = ""
        time.text = ""
    }
}

class ChatUserItem(val user: User): AbstractItem<ChatUserItem, ChatUserViewHolder>() {
    override fun getType() = 0

    override fun getViewHolder(v: View) = ChatUserViewHolder(v)

    override fun getLayoutRes() = R.layout.row_chatuser
}