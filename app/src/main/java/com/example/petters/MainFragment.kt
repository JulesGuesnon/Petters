package com.example.petters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.yuyakaido.android.cardstackview.*
import models.Card

class MainFragment: Fragment(), CardStackListener {

    lateinit var cardStackView: CardStackView
    lateinit var manager: CardStackLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_hello, container, false)

        cardStackView = view.findViewById<CardStackView>(R.id.main_card_stack_view)

        val itemAdapter = ItemAdapter<CardItem>()
        val fastAdapter = FastAdapter.with<CardItem, ItemAdapter<CardItem>>(itemAdapter)

        manager = CardStackLayoutManager(activity, this)

        view.findViewById<ImageButton>(R.id.main_done_button).setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Right)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()

            manager.setSwipeAnimationSetting(setting)
            cardStackView.swipe()
        }

        view.findViewById<ImageButton>(R.id.main_nope_button).setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Left)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()

            manager.setSwipeAnimationSetting(setting)
            cardStackView.swipe()
        }

        cardStackView.layoutManager = manager
        cardStackView.adapter = fastAdapter

        val users = listOf(
            CardItem(Card("0", "SUDO LE CHIEN")),
            CardItem(Card("12", "SUDO LE CHIEN")),
            CardItem(Card("23", "SUDO LE CHIEN")),
            CardItem(Card("34", "SUDO LE CHIEN")),
            CardItem(Card("40", "SUDO LE CHIEN"))
        )

        users.forEach {
            itemAdapter.add(it)
        }

        return view
    }

    companion object {

        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }

    override fun onCardDisappeared(view: View?, position: Int) {
        return
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {
        return
    }

    override fun onCardSwiped(direction: Direction?) {
        if (direction.toString() != "Right") return

        likeUser()
    }

    override fun onCardCanceled() {
        return
    }

    override fun onCardAppeared(view: View?, position: Int) {
        return
    }

    override fun onCardRewound() {
        return
    }

    fun likeUser() {
        println(manager.topPosition)
        println(manager.topView)
    }
}