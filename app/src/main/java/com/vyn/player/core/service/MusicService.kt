package com.vyn.player.core.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

class MusicService : Service() {
    private val binder = LocalBinder()

    override fun onBind(intent: Intent?): IBinder = binder

    inner class LocalBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }
}