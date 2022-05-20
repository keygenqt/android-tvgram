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
package com.keygenqt.tvgram.ui.text

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.keygenqt.tvgram.base.BaseFragment
import com.keygenqt.tvgram.databinding.TextFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Text step auth Fragment
 */
@AndroidEntryPoint
class TextFragment(
    private val text: String
) : BaseFragment<TextFragmentBinding>() {

    override fun onCreateBinding(i: LayoutInflater, v: ViewGroup?) =
        TextFragmentBinding.inflate(i, v, false)

    override fun onCreateView(binding: TextFragmentBinding): View {
        binding.initUi()
        return binding.root
    }

    private fun TextFragmentBinding.initUi() {
        tvText.text = text
    }
}