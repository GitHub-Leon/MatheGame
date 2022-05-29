package com.techmania.mathe_game

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.preference.PreferenceManager
import com.techmania.mathe_game.helpers.Presets
import com.techmania.mathe_game.views.KonfettiView


class ResultActivity : AppCompatActivity() {
    private lateinit var exitButton: Button
    private lateinit var playAgainButton: Button
    private lateinit var score: TextView
    private lateinit var viewKonfetti: KonfettiView

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        initViews()
        initListeners()

        viewKonfetti = findViewById(R.id.konfettiView)
        viewKonfetti.start(Presets.rain())
        playSound(R.raw.cheer)
        hideSystemBars()
    }

    override fun onStop() {
        super.onStop()
        mediaPlayer.stop()
        mediaPlayer.reset()
        mediaPlayer.release()
    }

    private fun hideSystemBars() {
        val windowInsetsController =
            ViewCompat.getWindowInsetsController(window.decorView) ?: return
        // Configure the behavior of the hidden system bars
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        // Hide both the status bar and the navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
    }

    private fun initListeners() {
        /*
        Init views
         */
        exitButton.setOnClickListener {
            playSound(R.raw.blob)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        playAgainButton.setOnClickListener {
            playSound(R.raw.blob)
            lateinit var intentNew: Intent
            when (intent.getStringExtra("Gamemode")) {
                "Subtraction" -> intentNew = Intent(this, SubtractionActivity::class.java)
                "Addition" -> intentNew = Intent(this, AdditionActivity::class.java)
                "Multiplication" -> intentNew = Intent(this, MultiplicationActivity::class.java)
                "Division" -> intentNew = Intent(this, DivisionActivity::class.java)
            }

            startActivity(intentNew)
        }
    }

    private fun initViews() {
        exitButton = findViewById(R.id.buttonExit)
        playAgainButton = findViewById(R.id.buttonPlayAgain)
        score = findViewById(R.id.textScoreValue)
        score.text = intent.getStringExtra("Score")
    }

    private fun playSound(resid:Int) {
        if (PreferenceManager.getDefaultSharedPreferences(applicationContext).getBoolean("sound", false)) {
            mediaPlayer = MediaPlayer.create(this, resid)
            mediaPlayer.start()
        }
    }
}