package com.example.petters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.yuyakaido.android.cardstackview.*
import models.Card
import models.User

class MainFragment: Fragment(), CardStackListener {

    lateinit var cardStackView: CardStackView
    lateinit var manager: CardStackLayoutManager
    var users = mutableListOf<Card>()

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


        FirebaseDatabase
            .getInstance()
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

                    val card = Card(
                        uid = user.uid,
                        name = user.petName,
                        image = user.profilePicture,
                        description = user.petDescription
                    )

                    itemAdapter.add(CardItem(card))
                    users.add(card)
                }

                override fun onChildRemoved(p0: DataSnapshot) {
                    return
                }

            })

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
        println(
            users[
                manager.topPosition - 1
            ]
        )
    }
}