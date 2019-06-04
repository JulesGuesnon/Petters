package com.example.petters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.yuyakaido.android.cardstackview.*
import models.Card
import models.User

class MainFragment: Fragment(), CardStackListener {

    lateinit var cardStackView: CardStackView
    lateinit var manager: CardStackLayoutManager
    lateinit var itemAdapter: ItemAdapter<CardItem>
    var users = mutableListOf<Card>()
    val selfId = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_hello, container, false)

        cardStackView = view.findViewById<CardStackView>(R.id.main_card_stack_view)

        itemAdapter = ItemAdapter<CardItem>()
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


        val usersRef = FirebaseDatabase
            .getInstance()
            .getReference("/users")


        usersRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                return
            }

            override fun onDataChange(p0: DataSnapshot) {
                val ref = FirebaseDatabase
                    .getInstance()
                    .getReference("/users/$selfId/liked/")

                ref.addChildEventListener(object: ChildEventListener {
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
                        val uid = data.getValue(String::class.java)
                        if (uid == null) return

                        val i = users.map { it.uid }.indexOf(uid)
                        if (i == -1) return
                        users.removeAt(i)

                        // It's gross
                        itemAdapter.clear()
                        users.forEach {
                            itemAdapter.add(CardItem(it))
                        }
                    }

                    override fun onChildRemoved(p0: DataSnapshot) {
                        return
                    }

                })
            }

        })
        usersRef.addChildEventListener(object: ChildEventListener {
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
                if (user.uid == FirebaseAuth.getInstance().currentUser?.uid) return

                val card = Card(
                    uid = user.uid,
                    name = user.petName,
                    image = user.profilePicture,
                    description = user.petDescription
                )
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
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid == null) return

        val user = getUser()
        FirebaseDatabase
            .getInstance()
            .getReference("users/$uid/liked/${user.uid}")
            .setValue(user.uid)

        FirebaseDatabase
            .getInstance()
            .getReference("/users/${user.uid}/liked")
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
                    val uid = data.getValue(String::class.java)
                    val selfUid = FirebaseAuth.getInstance().currentUser?.uid

                    println(uid)
                    println(selfUid)

                    if (uid == selfUid) {
                        FirebaseDatabase
                            .getInstance()
                            .getReference("users/$uid/matched/${user.uid}")
                            .setValue(user.uid)

                        FirebaseDatabase
                            .getInstance()
                            .getReference("users/$selfUid/matched/${user.uid}")
                            .setValue(user.uid)
                    }
                }

                override fun onChildRemoved(p0: DataSnapshot) {
                    return
                }

            })

    }

    fun getUser(): Card {
        return users[
                manager.topPosition - 1
        ]
    }
}