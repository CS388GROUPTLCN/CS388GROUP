package com.cs388.humanbenchmark

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.cs388.humanbenchmark.R

class ReflexGameActivity : AppCompatActivity() {

    private lateinit var textView: TextView
    private lateinit var btn_quit: Button
    private lateinit var highScoreTextView: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private var startTime: Long = 0
    private var isGameRunning: Boolean = false
    private var isGameEnded: Boolean = false
    private val HIGH_SCORE_KEY = "high_score"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reflex_game)

        textView = findViewById(R.id.tapToStart)
        btn_quit = findViewById(R.id.btn_quit)
        highScoreTextView = findViewById(R.id.highScoreTextView)
        textView.setTextColor(Color.BLACK)
        highScoreTextView.setTextColor(Color.BLACK)
        sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        btn_quit.isEnabled = true
        btn_quit.setOnClickListener{

        }

        textView.setOnClickListener {
            if (!isGameRunning && !isGameEnded) {
                startGame()
            } else if (textView.text == getString(R.string.tap_when_red) && isGameRunning) {
                endGameTooEarly()
            } else if (textView.text == getString(R.string.tap_when_green) && isGameRunning) {
                endGame()
            }
        }
        btn_quit.setOnClickListener{
            this.finish()
        }
    }

    private fun startGame() {
        // Change text and background color
        textView.text = getString(R.string.tap_when_red)
        textView.setBackgroundColor(Color.RED)
        textView.setTextColor(Color.WHITE)
        isGameRunning = true
        btn_quit.visibility = View.INVISIBLE
        btn_quit.isEnabled = false
        // Set a timer to turn the screen green after a random time
        object : CountDownTimer((1000 + Math.random() * 3000).toLong(), 100) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                if (isGameRunning) {
                    turnScreenGreen()
                }
            }
        }.start()
    }

    private fun turnScreenGreen() {
        textView.text = getString(R.string.tap_when_green)
        textView.setBackgroundColor(Color.GREEN)
        textView.setTextColor(Color.WHITE)
        startTime = System.currentTimeMillis()

        textView.setOnClickListener {
            if (isGameRunning && textView.text == getString(R.string.tap_when_green)) {
                endGame()
            } else if (isGameRunning && textView.text == getString(R.string.tap_when_red)) {
                endGameTooEarly()
            }
        }
    }

    private fun endGame() {
        // Check if the screen is green before processing
        if (isGameRunning && textView.text == getString(R.string.tap_when_green)) {
            // Record the reaction time
            val reactionTime = System.currentTimeMillis() - startTime

            // Retrieve the current high score
            val currentHighScore = sharedPreferences.getLong(HIGH_SCORE_KEY, Long.MAX_VALUE)

            // Check if the current reaction time is lower than the high score
            if (reactionTime < currentHighScore) {
                // Update the high score
                sharedPreferences.edit().putLong(HIGH_SCORE_KEY, reactionTime).apply()
                // Update the high score text view
                highScoreTextView.text = getString(R.string.high_score, reactionTime)
            }

            // Display the reaction time
            textView.text = getString(R.string.reaction_time, reactionTime)
            isGameEnded = true

            resetGame()
        }
    }

    private fun endGameTooEarly() {
        // Player clicked too early
        textView.text = getString(R.string.clicked_too_early)
        textView.setBackgroundColor(Color.RED)
        isGameRunning = false

        textView.postDelayed({
            resetGame()
        }, 1000)
    }

    private fun resetGame() {
        btn_quit.visibility = View.VISIBLE
        btn_quit.isEnabled = true
        textView.setBackgroundColor(Color.WHITE)
        textView.setTextColor(Color.BLACK)
        isGameRunning = false
        isGameEnded = false // Reset the game-ended flag

        // Set the OnClickListener for "Tap to Start"
        textView.setOnClickListener {
            if (!isGameRunning && !isGameEnded) {
                startGame()
            } else if (textView.text == getString(R.string.tap_when_red) && isGameRunning) {
                endGameTooEarly()
            } else if (textView.text == getString(R.string.tap_when_green) && isGameRunning) {
                endGame()
            }
        }
    }
}
