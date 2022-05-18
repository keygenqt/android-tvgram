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
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.keygenqt.tvgram.R
import com.keygenqt.tvgram.base.BaseFragment
import com.keygenqt.tvgram.databinding.AuthFragmentBinding
import com.keygenqt.tvgram.extensions.addTextChangedListener
import com.keygenqt.tvgram.extensions.hideKeyboard
import com.keygenqt.tvgram.extensions.showKeyboard
import dagger.hilt.android.AndroidEntryPoint

/**
 * First step auth Fragment
 */
@AndroidEntryPoint
class AuthFragment : BaseFragment<AuthFragmentBinding>() {

    private val viewModel by viewModels<AuthViewModel>()

    override fun onCreateBinding(i: LayoutInflater, v: ViewGroup?) =
        AuthFragmentBinding.inflate(i, v, false)

    override fun onCreateView(binding: AuthFragmentBinding): View {
        binding.initUi()
        binding.initListener()
        return binding.root
    }

    private fun AuthFragmentBinding.initListener() {
        lifecycleScope.launchWhenStarted {
            viewModel.isLoading.collect {
                loader.visibility = if (it) View.VISIBLE else View.GONE
                btnSubmit.visibility = if (!it) View.VISIBLE else View.GONE
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.isError.collect {
                tePhone.error = it
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.isSuccess.collect {
                if (it == true) {
                    fragmentTransaction.commit {
                        setReorderingAllowed(true)
                        replace(R.id.main_browse_fragment, ValidateCodeFragment())
                    }
                }
            }
        }
    }

    private fun AuthFragmentBinding.initUi() {
        // phone
        tePhone.requestFocus()
        tePhone.addTextChangedListener {
            tePhone.error = null
        }
        tePhone.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                tePhone.setHint(R.string.auth_phone_hint)
                tePhone.showKeyboard()
            } else {
                tePhone.hideKeyboard()
                tePhone.hint = ""
            }
        }
        // btn
        btnSubmit.setOnClickListener {
            tePhone.text.validate()?.let { error ->
                tePhone.error = error
                tePhone.requestFocus()
            } ?: run {
                viewModel.sendPhone(tePhone.text.toString())
            }
        }
    }

    private fun Editable?.validate(): String? {
        if (this == null || this.toString().isBlank()) {
            return getString(R.string.auth_error_empty)
        } else if (!"""^[+][0-9]{10,13}$""".toRegex().matches(this.toString())) {
            return getString(R.string.auth_error_not_match)
        }
        return null
    }
}