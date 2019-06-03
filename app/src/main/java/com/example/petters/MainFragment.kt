package com.example.petters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackView
import models.Card

class MainFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_hello, container, false)

        val cardStackView = view.findViewById<CardStackView>(R.id.main_card_stack_view)

        val itemAdapter = ItemAdapter<CardItem>()
        val fastAdapter = FastAdapter.with<CardItem, ItemAdapter<CardItem>>(itemAdapter)

        cardStackView.layoutManager = CardStackLayoutManager(activity)
        cardStackView.adapter = fastAdapter

        for (i in 0..200) {
            itemAdapter.add(
                CardItem(Card("SUDO LE CHIEN"))
            )
        }

        return view
    }

    companion object {

        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }
}