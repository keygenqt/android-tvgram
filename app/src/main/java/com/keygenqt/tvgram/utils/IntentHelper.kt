package com.keygenqt.tvgram.utils

import android.content.Context
import android.content.Intent
import com.keygenqt.tvgram.MainActivity
import com.keygenqt.tvgram.ui.auth.AuthActivity
import com.keygenqt.tvgram.ui.photo.PhotoActivity
import com.keygenqt.tvgram.ui.settings.SettingsActivity
import com.keygenqt.tvgram.ui.text.TextActivity
import com.keygenqt.tvgram.ui.video.VideoActivity

object IntentHelper {

    /**
     * Start [AuthActivity]
     */
    fun Context.getIntentAuthActivity() = Intent(
        this,
        AuthActivity::class.java
    ).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    /**
     * Start [MainActivity]
     */
    fun Context.getIntentMainActivity() = Intent(
        this,
        MainActivity::class.java
    ).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    /**
     * Start [VideoActivity]
     */
    fun Context.getIntentVideoActivity(
        videoId: Int,
        title: String,
        description: String,
    ) = Intent(
        this,
        VideoActivity::class.java
    ).apply {
        putExtra("isNote", false)
        putExtra("videoId", videoId)
        putExtra("title", title)
        putExtra("description", description)
    }

    /**
     * Start [VideoActivity] note
     */
    fun Context.getIntentVideoNoteActivity(
        videoId: Int,
        title: String,
        description: String,
    ) = Intent(
        this,
        VideoActivity::class.java
    ).apply {
        putExtra("isNote", true)
        putExtra("videoId", videoId)
        putExtra("title", title)
        putExtra("description", description)
    }

    /**
     * Start [PhotoActivity] note
     */
    fun Context.getIntentPhotoActivity(
        path: String,
        text: String,
    ) = Intent(
        this,
        PhotoActivity::class.java
    ).apply {
        putExtra("photo", path)
        putExtra("text", text)
    }

    /**
     * Start [TextActivity] note
     */
    fun Context.getIntentTextActivity(
        text: String,
    ) = Intent(
        this,
        TextActivity::class.java
    ).apply {
        putExtra("text", text)
    }

    /**
     * Start [SettingsActivity] note
     */
    fun Context.getIntentSettingsActivity() = Intent(
        this,
        SettingsActivity::class.java
    )
}