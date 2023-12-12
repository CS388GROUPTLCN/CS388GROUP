package com.cs388.humanbenchmark

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.marginBottom
import androidx.core.view.marginTop
import androidx.recyclerview.widget.RecyclerView

class LeaderboardAdapter(private val players: List<Player>): RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val playerNameView: TextView = itemView.findViewById(R.id.playerName)
        val gameNameView: TextView = itemView.findViewById(R.id.gameName)
        val gameScoreView: TextView = itemView.findViewById(R.id.gameScore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.player_scores, parent,false)
        return ViewHolder(contactView)
    }

    override fun getItemCount(): Int {
        return players.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val player = players[position]

        holder.playerNameView.text = player.username
        holder.gameNameView.text = player.game
        holder.gameScoreView.text = player.score.toString()

        if (position % 2 == 0)
            holder.itemView.setBackgroundResource(R.color.light_grey)
    }
}