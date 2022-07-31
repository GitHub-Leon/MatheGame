package com.techmania.mathe_game

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import java.util.*
import kotlin.math.roundToInt


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

        setAppLanguage()

        setupAnimations()
        startMainMenu()
    }

    private fun setAppLanguage() {
        setLocale(PreferenceManager.getDefaultSharedPreferences(this).getString("language", ""))
    }

    private fun setLocale(languageCode: String?) {
        val config = resources.configuration
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            config.setLocale(locale)
        else
            config.locale = locale

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            createConfigurationContext(config)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    private fun startMainMenu() {
        Handler().postDelayed({
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

        image.layoutParams.height = (display!!.height/1.5).roundToInt()
        image.layoutParams.width = (display!!.width/1.5).roundToInt()
    }

    companion object {
        private const val SPLASH_SCREEN = 5000L
    }
}