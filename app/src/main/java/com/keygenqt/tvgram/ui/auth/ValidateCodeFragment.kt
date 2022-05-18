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
import com.keygenqt.tvgram.databinding.ValidateCodeFragmentBinding
import com.keygenqt.tvgram.extensions.addTextChangedListener
import com.keygenqt.tvgram.extensions.hideKeyboard
import com.keygenqt.tvgram.extensions.showKeyboard
import com.keygenqt.tvgram.ui.home.HomeFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


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
            viewModel.isError.collect {
                teCode.error = it
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.isSuccess.collect {
                if (it == true) {
                    fragmentTransaction.commit {
                        setReorderingAllowed(true)
                        replace(R.id.main_browse_fragment, HomeFragment())
                    }
                    requireActivity().onBackPressed()
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
                teCode.setHint(R.string.validate_code_label)
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