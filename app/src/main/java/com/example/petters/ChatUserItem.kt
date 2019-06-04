package com.example.petters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.squareup.picasso.Picasso
import models.User

class ChatUserViewHolder(itemView: View): FastAdapter.ViewHolder<ChatUserItem>(itemView) {

    val username: TextView
    val image: ImageView

    init {
        username = itemView.findViewById(R.id.chat_username)
        image = itemView.findViewById(R.id.chat_profile_picture)
    }

    override fun bindView(item: ChatUserItem, payloads: MutableList<Any>) {
        username.text = item.user.name
        Picasso.get()
            .load(item.user.profilePicture)
            .fit()
            .transform(CircleTransform())
            .into(image)
    }

    override fun unbindView(item: ChatUserItem) {
        username.text = ""
    }
}

class ChatUserItem(val user: User): AbstractItem<ChatUserItem, ChatUserViewHolder>() {
    override fun getType() = 0

    override fun getViewHolder(v: View) = ChatUserViewHolder(v)

    override fun getLayoutRes() = R.layout.row_chatuser
}