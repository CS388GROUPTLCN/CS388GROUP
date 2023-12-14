package com.cs388.humanbenchmark

class PlayerScoreFetcher {
    companion object {
        val username = listOf(
            "USER1", "USER2", "USER3", "USER4", "USER5",
                    "USER6", "USER7", "USER8", "USER9", "USER10", "USER11",
            "USER12", "USER13", "USER14", "USER15", "USER16"
        )
        val game = "Game1 Example"

        val score = 123456

        fun getScores(): MutableList<Player> {
            var players: MutableList<Player> = ArrayList()
            for (i in 0..9) {
                val player = Player(username[i], game, score)
                players.add(player)

            }
            return players
        }

        fun getNext5Scores(): MutableList<Player> {
            var newPlayers : MutableList<Player> = ArrayList()
            for (i in 10..14){
                val player = Player(username[i], game, score)
                newPlayers.add(player)
            }
            return newPlayers

        }


    }







}