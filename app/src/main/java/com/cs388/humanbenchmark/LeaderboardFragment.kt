package com.cs388.humanbenchmark

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class LeaderboardFragment : Fragment() {
    private lateinit var dbref : DatabaseReference
    lateinit var leaderboardHz: RecyclerView
    lateinit var game1Scores: MutableList<Player>
//    lateinit var game2Scores: List<Player>
//    lateinit var game3Scores: List<Player>
    lateinit var verticalDataList: Array<MutableList<Player>?> // List of data for each vertical RecyclerView


    lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().reference
        verticalDataList = arrayOfNulls<MutableList<Player>>(3)
        game1Scores = ArrayList()


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_leaderboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        leaderboardHz = view.findViewById(R.id.leaderboardHz)
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(leaderboardHz)

        val hzAdapter = LeaderboardHzAdapter(verticalDataList)
        leaderboardHz.adapter = hzAdapter
        leaderboardHz.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)








        getUserData(1)
        getUserData(2)
        getUserData(3)


        //verticalDataList.removeFirst()
        // Create and set layout manager for the horizontal RecyclerView


    }


    fun getUserData(gameCategory: Int) {



        val dbref =
            FirebaseDatabase.getInstance().getReference("leaderboard").child("game$gameCategory")
                .orderByValue()


        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var players: MutableList<Player> = ArrayList()


                    for (userSnapshot in snapshot.children) {

                        Log.d("snapshot", "${userSnapshot.toString()}")
                        val username = userSnapshot.key
                        val score = userSnapshot.value.toString().toLong()
                        Log.d("snapshot", "${username.toString()}")
                        Log.d("snapshot", "${score.toString()}")


                        if (username != null && score != null) {
                            val player = Player(username, "game$gameCategory", score)
                            players.add(player)
                        }
                    }

                    if(gameCategory == 2){
                       players = players.reversed().toMutableList()
                    }

                    players.sortWith(Comparator {
                            lhs, rhs ->
                        lhs.score.toInt().compareTo(rhs.score.toInt())
                    })
                    verticalDataList[gameCategory-1] = players

                    leaderboardHz.adapter = LeaderboardHzAdapter(verticalDataList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle cancellation
            }
        })
    }

}
