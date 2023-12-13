package com.cs388.humanbenchmark

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import java.util.Timer
import kotlin.concurrent.schedule
import kotlin.math.floor

class SequenceGameActivity : AppCompatActivity() {
    private var pattern: MutableList<Int> = mutableListOf()
    private var colors = listOf(R.color.dark_green, R.color.dark_white, R.color.light_black, R.color.red1, R.color.shrek)
    private var colorsPattern: MutableList<Int> = mutableListOf()
    private var gameLevel: Int = 0
    private var gamePlaying: Boolean = false
    private var cluePlaying: Boolean = false
    private var clueHoldTime: Int = 1000
    private var cluePauseTime: Int = 333
    private var nextClueWaitTime: Int = 1000
    private var guessCount: Int = 0
    private var cols: Int = 3
    private var numTiles: Int = 8
    private var length: Int = 5
    private var buttonSize: Int = 300
    private var minLevel: Int = 3


    private lateinit var startGameTextView: TextView
    private lateinit var llContent: LinearLayout
    private lateinit var levelText: TextView
    private lateinit var scoreText: TextView
    private lateinit var memoryIconView: ImageView

    private var newCardListener = View.OnClickListener {
        Log.d("id", "button id = ${it.id}")
        if (!cluePlaying)
            guess(it.id)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sequence_game_activity)

        startGameTextView = findViewById(R.id.sequence_game_start)
        llContent = findViewById(R.id.sequence_game_content)
        levelText = findViewById(R.id.level_text)
        scoreText = findViewById(R.id.score_text)
        memoryIconView = findViewById(R.id.memory_icon)

        startGameTextView.setOnClickListener {
            startGameTextView.visibility = View.GONE
            levelText.visibility = View.VISIBLE
            scoreText.visibility = View.GONE
            memoryIconView.visibility = View.GONE
            startGame()
        }
    }

    private fun startGame() {
        gamePlaying = true
        llContent.removeAllViews()
        createGrid(cols, buttonSize)
        createSequencePattern(length, numTiles)
        createColorPattern()
        playClueSequence()
        Log.d("sequence pattern", "pattern created = $pattern")
        Log.d("color pattern", "color pattern created = $colorsPattern")
    }

    private fun playClueSequence() {
        cluePlaying = true
        if (clueHoldTime >= 500)
            clueHoldTime -= 100

        Log.e("progress","game level = $gameLevel")
        var delay: Long = nextClueWaitTime.toLong()
        for (i in 0..gameLevel) {
            Log.e("clue state", "clue state $cluePlaying")
            val button: Button = findViewById(pattern[i])
            Timer().schedule(delay) {
                button.backgroundTintList =
                    ContextCompat.getColorStateList(button.context, colors[colorsPattern[i]])
                Timer().schedule(clueHoldTime.toLong()) {
                    button.backgroundTintList =
                        ContextCompat.getColorStateList(button.context, R.color.light_blue)
                }
            }
            delay += clueHoldTime
            delay += cluePauseTime
        }
        cluePlaying = false
    }

    private fun guess(buttonId: Int) {
        if (!gamePlaying || cluePlaying)
            return
        if (buttonId != pattern[guessCount]) {
            gameLoss()
            return
        }

        Log.v("guess and level", "guess = $guessCount, level = ${gameLevel+1}")
        if (guessCount == gameLevel) {
            // if player passes 5 levels the board changes
            if (gameLevel % 2 == 0 && gameLevel < 10) {
                llContent.removeAllViews()
                buttonSize -= 100
                cols += 1
                length += 5
                numTiles = cols*cols
                createGrid(cols, buttonSize)
                createSequencePattern(length, numTiles)
                Log.d("sequence pattern", "pattern created = $pattern")
                minLevel += 5
            }
            levelText.text = "Level: ${gameLevel+2}"
            guessCount = 0
            gameLevel++
            playClueSequence()
        } else {
            guessCount++
        }
    }

    // game lost function
    private fun gameLoss() {
        Log.d("score", "Your score! $gameLevel")
        scoreText.text = "Level: ${gameLevel+1}"
        gameLevel = 0
        guessCount = 0
        clueHoldTime = 1000
        cluePauseTime = 333
        nextClueWaitTime = 1000
        cols = 3
        numTiles = 8
        length = 5
        buttonSize = 300
        gamePlaying = false
        levelText.text = "Level: 1"
        pattern.clear()
        llContent.removeAllViews()
        levelText.visibility = View.GONE
        llContent.visibility = View.GONE
        scoreText.visibility = View.VISIBLE
        startGameTextView.visibility = View.VISIBLE
        memoryIconView.visibility = View.VISIBLE
    }

    // function to create grid for game
    private fun createGrid(columns: Int, size: Int) {
        llContent.visibility = View.VISIBLE
        var cardCount = 0
        for (i in 1..columns) {
            val llRow = LinearLayout(this)
            llRow.orientation = LinearLayout.HORIZONTAL
            llContent.addView(llRow)
            for (i in 1..columns) {
                val newCard = CardView(this)
                newCard.id = cardCount
                newCard.backgroundTintList = ContextCompat.getColorStateList(this, R.color.light_blue)
                llRow.addView(newCard)
                cardCount++
                newCard.setOnClickListener(newCardListener)
            }
        }
    }

    // function to create sequence pattern
    private fun createSequencePattern(length: Int, numTiles: Int) {
        for (i in 0..length) {
            pattern.add(floor(Math.random() * numTiles).toInt())
        }
    }

    private fun createColorPattern() {
        for (i in 0..200) {
            colorsPattern.add(floor(Math.random() * 5).toInt())
        }
    }
}