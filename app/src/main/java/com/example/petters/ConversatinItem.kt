package com.example.petters

import android.view.View
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import models.Message
import models.User

class ConversatinItemViewHolder(itemView: View): FastAdapter.ViewHolder<ConversationItem>(itemView) {

    val msg: TextView

    init {
        var view = itemView.findViewById<TextView>(R.id.row_self_message)
        if (view == null) {
            view = itemView.findViewById(R.id.row_other_message)
        }

        msg = view

    }

    override fun bindView(item: ConversationItem, payloads: MutableList<Any>) {
        msg.text = item.message.content
    }

    override fun unbindView(item: ConversationItem) {
        msg.text = ""
    }
}

class ConversationItem(val message: Message): AbstractItem<ConversationItem, ConversatinItemViewHolder>() {
    override fun getType() = message.sendAt.toInt()

    override fun getViewHolder(v: View) = ConversatinItemViewHolder(v)

    override fun getLayoutRes(): Int {
        println(message.content)
        if (message.fromUid == FirebaseAuth.getInstance().currentUser?.uid) {
            println("SELF MESSAGE")
            return R.layout.row_self_message
        }
        else {
            println("OTHER MESSAGE")
            return R.layout.row_other_message
        }
    }
}