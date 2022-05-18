package com.keygenqt.tvgram.ui.auth

import android.text.Editable
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
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
                tePhone.setHint(R.string.auth_phone_label)
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