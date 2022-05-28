package com.techmania.mathe_game.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.techmania.mathe_game.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}