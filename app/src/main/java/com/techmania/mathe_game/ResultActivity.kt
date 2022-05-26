package com.techmania.mathe_game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class ResultActivity : AppCompatActivity() {
    private lateinit var exitButton : Button
    private lateinit var playAgainButton : Button
    private lateinit var score : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        initViews()
        initListeners()
    }

    private fun initListeners() {
        /*
        Init views
         */
        exitButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        })
        playAgainButton.setOnClickListener(View.OnClickListener {
            lateinit var intentNew : Intent
            when (intent.getStringExtra("Gamemode")) {
                "Subtraction" -> intentNew = Intent(this, SubtractionActivity::class.java)
                "Addition" -> intentNew = Intent(this, AdditionActivity::class.java)
                //TODO: ADD OTHER GAMEMODES
            }

            startActivity(intentNew)
        })
    }

    private fun initViews() {
        exitButton = findViewById(R.id.buttonExit)
        playAgainButton = findViewById(R.id.buttonPlayAgain)
        score = findViewById(R.id.textScoreValue)
        score.text = intent.getStringExtra("Score")
    }
}