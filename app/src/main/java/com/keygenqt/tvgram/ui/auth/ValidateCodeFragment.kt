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
package com.keygenqt.tvgram.ui.auth

import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.keygenqt.tvgram.R
import com.keygenqt.tvgram.base.BaseFragment
import com.keygenqt.tvgram.databinding.ValidateCodeFragmentBinding
import com.keygenqt.tvgram.extensions.addTextChangedListener
import com.keygenqt.tvgram.extensions.hideKeyboard
import com.keygenqt.tvgram.extensions.showKeyboard
import com.keygenqt.tvgram.utils.IntentHelper.getIntentMainActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * Second step auth Fragment
 */
@AndroidEntryPoint
class ValidateCodeFragment : BaseFragment<ValidateCodeFragmentBinding>() {

    private val viewModel by viewModels<ValidateCodeViewModel>()

    override fun onCreateBinding(i: LayoutInflater, v: ViewGroup?) =
        ValidateCodeFragmentBinding.inflate(i, v, false)

    override fun onCreateView(binding: ValidateCodeFragmentBinding): View {
        binding.initUi()
        binding.initListener()
        return binding.root
    }

    private fun ValidateCodeFragmentBinding.initListener() {
        lifecycleScope.launchWhenStarted {
            viewModel.isLoading.collect {
                loader.visibility = if (it) View.VISIBLE else View.GONE
                btnSubmit.visibility = if (!it) View.VISIBLE else View.GONE
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.isError.collect {
                teCode.error = it
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.isSuccess.collect {
                if (it == true) {
                    startActivity(requireContext().getIntentMainActivity())
                }
            }
        }
    }

    private fun ValidateCodeFragmentBinding.initUi() {
        // phone
        teCode.requestFocus()
        teCode.addTextChangedListener {
            teCode.error = null
        }
        teCode.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                teCode.showKeyboard()
                teCode.setHint(R.string.validate_code_hint)
            } else {
                teCode.hideKeyboard()
                teCode.hint = ""
            }
        }
        // btn
        btnSubmit.setOnClickListener {
            teCode.text.validate()?.let { error ->
                teCode.error = error
                teCode.requestFocus()
            } ?: run {
                viewModel.validateCode(teCode.text.toString())
            }
        }
    }

    private fun Editable?.validate(): String? {
        if (this == null || this.toString().isBlank()) {
            return getString(R.string.auth_error_empty)
        } else if (!"""^[0-9]{5}$""".toRegex().matches(this.toString())) {
            return getString(R.string.auth_error_not_match)
        }
        return null
    }
}