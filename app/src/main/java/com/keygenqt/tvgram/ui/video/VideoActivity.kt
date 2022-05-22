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

import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.keygenqt.tvgram.R
import dagger.hilt.android.AndroidEntryPoint

/**
 * Auth Activity with material
 */
@AndroidEntryPoint
class VideoActivity : FragmentActivity() {

    private val viewModel by viewModels<VideoViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        viewModel.downloadVideo(intent.getIntExtra("videoId", 0))

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.main_browse_fragment, SpinnerFragment())
        }

        initListener()
    }

    private fun initListener() {
        lifecycleScope.launchWhenStarted {
            viewModel.isError.collect {
                if (it != null) {
                    Toast.makeText(this@VideoActivity, it, Toast.LENGTH_LONG).show()
                    this@VideoActivity.onBackPressed()
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.isSuccess.collect {
                if (it != null) {
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace(
                            R.id.main_browse_fragment, VideoFragment(
                                path = it,
                                title = intent.getStringExtra("title"),
                                description = intent.getStringExtra("description"),
                                isNote = intent.getBooleanExtra("isNote", false),
                            )
                        )
                    }
                }
            }
        }
    }

    class SpinnerFragment : Fragment() {
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            val progressBar = ProgressBar(container?.context)
            if (container is FrameLayout) {
                progressBar.layoutParams =
                    FrameLayout.LayoutParams(SPINNER_WIDTH, SPINNER_HEIGHT, Gravity.CENTER)
            }
            return progressBar
        }
    }

    companion object {
        private const val SPINNER_WIDTH = 100
        private const val SPINNER_HEIGHT = 100
    }
}