/*
 * Copyright 2022 Vitaliy Zarubin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.keygenqt.tvgram.ui.video

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import androidx.leanback.app.BackgroundManager
import androidx.leanback.app.VideoSupportFragment
import androidx.leanback.app.VideoSupportFragmentGlueHost
import androidx.leanback.media.MediaPlayerAdapter
import androidx.leanback.media.PlaybackTransportControlGlue
import androidx.leanback.widget.PlaybackControlsRow
import dagger.hilt.android.AndroidEntryPoint

/**
 * Video play Fragment
 */
@AndroidEntryPoint
class VideoFragment(
    private val path: String,
    private val title: String?,
    private val description: String?,
    private val isNote: Boolean,
) : VideoSupportFragment() {

    private val backgroundManager: BackgroundManager? by lazy {
        BackgroundManager.getInstance(activity)?.apply {
            attach(requireActivity().window)
        }
    }

    private lateinit var transportControlGlue: PlaybackTransportControlGlue<MediaPlayerAdapter>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (isNote) {
            backgroundManager?.color = Color.WHITE
        }

        val glueHost = VideoSupportFragmentGlueHost(this@VideoFragment)
        val playerAdapter = MediaPlayerAdapter(context)

        playerAdapter.setRepeatAction(PlaybackControlsRow.RepeatAction.INDEX_NONE)

        transportControlGlue = PlaybackTransportControlGlue(activity, playerAdapter)
        transportControlGlue.host = glueHost
        transportControlGlue.title = title
        transportControlGlue.subtitle = description
        transportControlGlue.playWhenPrepared()

        // Video rewind
        setOnKeyInterceptListener { _, _, ev ->
            if (ev?.keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                transportControlGlue.seekTo(transportControlGlue.currentPosition + 5000)
                return@setOnKeyInterceptListener true
            }
            if (ev?.keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                transportControlGlue.seekTo(transportControlGlue.currentPosition - 5000)
                return@setOnKeyInterceptListener true
            }
            false
        }

        playerAdapter.setDataSource(Uri.parse(path))
    }

    override fun onPause() {
        super.onPause()
        transportControlGlue.pause()
    }
}