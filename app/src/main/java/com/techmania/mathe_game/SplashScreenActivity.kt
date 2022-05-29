package com.techmania.mathe_game

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class SplashScreenActivity : AppCompatActivity() {
    private lateinit var topAnimation: Animation
    private lateinit var bottomAnimation: Animation
    private lateinit var image: ImageView
    private lateinit var title: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        ) //Hide Status bar
        setContentView(R.layout.activity_splash_screen)

        setupAnimations()
        startMainMenu()
    }

    private fun startMainMenu() {
        Handler().postDelayed(Runnable {
            kotlin.run {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, SPLASH_SCREEN)
    }

    private fun setupAnimations() {
        //init animations
        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)

        //Hooks
        image = findViewById(R.id.splashScreenImageView)
        title = findViewById(R.id.appnameTextView)

        //set animations
        title.animation = topAnimation
        image.animation = bottomAnimation
    }

    companion object {
        private const val SPLASH_SCREEN = 5000L
    }
}