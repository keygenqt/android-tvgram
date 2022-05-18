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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keygenqt.tvgram.base.done
import com.keygenqt.tvgram.base.error
import com.keygenqt.tvgram.base.success
import com.keygenqt.tvgram.services.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [ValidateCodeFragment] viewModel
 */
@HiltViewModel
class ValidateCodeViewModel @Inject constructor(
    private val repo: AuthRepository,
) : ViewModel() {

    /**
     * Success response after query
     */
    private val _isSuccess = MutableStateFlow<Boolean?>(null)

    /**
     * [StateFlow] for variable [_isSuccess]
     */
    val isSuccess: StateFlow<Boolean?> get() = _isSuccess.asStateFlow()

    /**
     * Error response after query
     */
    private val _isError = MutableStateFlow<String?>(null)

    /**
     * [StateFlow] for variable [_isError]
     */
    val isError: StateFlow<String?> get() = _isError.asStateFlow()

    /**
     * Start/end query
     */
    private val _isLoading = MutableStateFlow(false)

    /**
     * [StateFlow] for variable [_isLoading]
     */
    val isLoading: StateFlow<Boolean> get() = _isLoading.asStateFlow()

    /**
     * Auth by code
     */
    fun validateCode(
        code: String
    ) {
        _isLoading.value = true
        viewModelScope.launch {
            repo.checkAuthenticationCode(code)
                .success {
                    _isSuccess.value = true
                }
                .done {
                    _isLoading.value = false
                }
                .error {
                    _isError.value = it.message
                    launch {
                        delay(2000)
                        _isError.value = null
                    }
                }
        }
    }
}