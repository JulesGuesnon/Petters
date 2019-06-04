package com.example.petters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.squareup.picasso.Picasso
import models.Card

class CardItemViewHolder(itemView: View): FastAdapter.ViewHolder<CardItem>(itemView) {

    val image: ImageView
    val name: TextView

    init {
        image = itemView.findViewById(R.id.main_image)
        name = itemView.findViewById(R.id.main_name)
    }

    override fun bindView(item: CardItem, payloads: MutableList<Any>) {
        name.text = item.card.name + item.card.id
        Picasso
            .get()
            .load(item.card.image)
            .fit()
            .into(image)
    }

    override fun unbindView(item: CardItem) {
        name.text = ""
        image.setImageBitmap(null)
    }
}

class CardItem(val card: Card): AbstractItem<CardItem, CardItemViewHolder>() {
    override fun getType() = card.id.toInt()

    override fun getViewHolder(v: View) = CardItemViewHolder(v)

    override fun getLayoutRes() = R.layout.row_main_card
}