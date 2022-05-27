package com.techmania.mathe_game.listeners

import com.techmania.mathe_game.views.KonfettiView
import nl.dionsegijn.konfetti.core.Party

interface OnParticleSystemUpdateListener {
    fun onParticleSystemStarted(view: KonfettiView, party: Party, activeSystems: Int)
    fun onParticleSystemEnded(view: KonfettiView, party: Party, activeSystems: Int)
}