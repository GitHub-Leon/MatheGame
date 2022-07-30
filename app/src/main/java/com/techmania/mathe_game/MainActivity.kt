package com.techmania.mathe_game

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class MainActivity : AppCompatActivity() {
    private lateinit var addition: Button
    private lateinit var subtraction: Button
    private lateinit var multi: Button
    private lateinit var division: Button


    lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinner = findViewById(R.id.spinner)
        var arrayAdapter = ArrayAdapter.createFromResource(
            applicationContext,R.array.difficulties, android.R.layout.simple_spinner_item
        )
        //dropdown definieren
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        //spinner befüllen
        spinner.adapter = arrayAdapter

        initViews()
        initListeners()
        hideSystemBars()
    }

    private fun initViews() {
        addition = findViewById(R.id.buttonAdd)
        subtraction = findViewById(R.id.buttonSub)
        multi = findViewById(R.id.buttonMulti)
        division = findViewById(R.id.buttonDivision)

    }

    private fun initListeners() {
        addition.setOnClickListener {
            //intent to open another activity
            val intent = Intent(this@MainActivity, AdditionActivity::class.java)
            startActivity(intent) //second activity will open

            //define the main activity as the parent activity of this second activity
            // im Manifest : parentActivityName=".MainActivity
        }

        subtraction.setOnClickListener {
            //intent to open another activity
            val intent = Intent(this@MainActivity, SubtractionActivity::class.java)
            startActivity(intent) //second activity will open
        }

       /* start.setOnClickListener {
            //intent to open another activity
            val intent = Intent(this@MainActivity, ScoreboardActivity::class.java)
            startActivity(intent) //second activity will open
        }*/
        multi.setOnClickListener{
            val intent = Intent(this@MainActivity, MultiplicationActivity::class.java)
            startActivity(intent)
        }
        division.setOnClickListener{
            val intent = Intent(this@MainActivity, DivisionActivity::class.java)
            startActivity(intent)
        }
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
}