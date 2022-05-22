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
package com.keygenqt.tvgram.ui.photo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import com.keygenqt.tvgram.R
import com.keygenqt.tvgram.base.BaseFragment
import com.keygenqt.tvgram.databinding.PhotoFragmentBinding
import com.keygenqt.tvgram.ui.video.VideoFragment
import com.keygenqt.tvgram.ui.video.VideoViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Photo show Fragment
 */
@AndroidEntryPoint
class PhotoFragment(
    private val photo: Int,
    private val text: String?
) : BaseFragment<PhotoFragmentBinding>() {

    private val viewModel by viewModels<PhotoViewModel>()

    override fun onCreateBinding(i: LayoutInflater, v: ViewGroup?) =
        PhotoFragmentBinding.inflate(i, v, false)

    override fun onCreateView(binding: PhotoFragmentBinding): View {
        binding.initUi()
        binding.initListener()
        viewModel.downloadPhoto(photo)
        return binding.root
    }

    private fun PhotoFragmentBinding.initListener() {
        lifecycleScope.launchWhenStarted {
            viewModel.isError.collect {
                if (it != null) {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                    requireActivity().onBackPressed()
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.isSuccess.collect {
                if (it != null) {
                    ivImage.load(it)
                }
            }
        }
    }

    private fun PhotoFragmentBinding.initUi() {
        // if text has show right block
        if (text.isNullOrBlank()) {
            list2.visibility = View.GONE
        } else {
            list2.visibility = View.VISIBLE
            photoText.text = text
        }
    }
}