package com.techmania.mathe_game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentContainerView
import com.techmania.mathe_game.fragments.SettingsFragment

class SettingsActivity : AppCompatActivity() {
    private lateinit var fragmentContainerView : FragmentContainerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        initViews()

        supportActionBar?.title = "Settings" //set title of actionbar to Settings
    }

    private fun initViews() {
        fragmentContainerView = findViewById(R.id.fragmentContainerView)

        //start transaction
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainerView, SettingsFragment())
            .commit()
    }
}