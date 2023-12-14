package com.cs388.humanbenchmark

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LeaderboardHzAdapter(private val verticalDataList: List<List<Player>>) :
    RecyclerView.Adapter<LeaderboardHzAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LeaderboardHzAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclers, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: LeaderboardHzAdapter.ViewHolder, position: Int) {
        val verticalData = verticalDataList[position]
        holder.bindVerticalData(verticalData)
    }

    override fun getItemCount(): Int {
        return verticalDataList.size

    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val verticalRecyclerView: RecyclerView = itemView.findViewById(R.id.leaderboard) // gets recycler view in recyclers xml

        fun bindVerticalData(verticalData: List<Player>) { // list of players
            val verticalAdapter = LeaderboardAdapter(verticalData) // uses the old recycler view adapter
            verticalRecyclerView.adapter = verticalAdapter
            verticalRecyclerView.layoutManager = LinearLayoutManager(itemView.context)

        }
    }


}