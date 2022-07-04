package com.techmania.mathe_game

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.preference.PreferenceManager
import com.techmania.mathe_game.fragments.SettingsFragment
import java.util.*


class SettingsActivity : AppCompatActivity() {
    private lateinit var fragmentContainerView : FragmentContainerView
    private lateinit var prefs : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        initViews()
        registerSettingsChangeListener()

        supportActionBar?.title = "Settings" //set title of actionbar to Settings
    }

    private fun registerSettingsChangeListener() {
        val spChanged =
            OnSharedPreferenceChangeListener { sharedPreferences, key ->
                if (key == "language") {
                    setLocale(sharedPreferences.getString(key, ""))
                    setSettingsContainerView()
                }
            }

        prefs.registerOnSharedPreferenceChangeListener(spChanged)
    }

    private fun initViews() {
        fragmentContainerView = findViewById(R.id.fragmentContainerView)
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        setSettingsContainerView()
    }

    private fun setSettingsContainerView() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainerView, SettingsFragment())
            .commit()
    }

    private fun setLocale(languageCode: String?) {
        val config = resources.configuration
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        config.setLocale(locale)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            createConfigurationContext(config)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}