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
package com.keygenqt.tvgram.ui.other

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keygenqt.tvgram.base.AuthState
import com.keygenqt.tvgram.base.TelegramApi
import com.keygenqt.tvgram.data.ChatModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val apiTelegram: TelegramApi,
) : ViewModel() {

    /**
     * Check is login user
     */
    private val _isLogin = MutableStateFlow<Boolean?>(null)

    /**
     * [StateFlow] for variable [_isLogin]
     */
    val isLogin: StateFlow<Boolean?> get() = _isLogin.asStateFlow()

    /**
     * Query check is login
     */
    fun startAuthentication() {
        viewModelScope.launch {
            // delay for animation
            delay(1800)
            // check state user
            apiTelegram.authState.collect {
                when (it) {
                    AuthState.UNAUTHENTICATED -> apiTelegram.startAuthentication()
                    AuthState.WAIT_ENCRYPTION_KEY -> apiTelegram.checkDatabaseEncryptionKey()
                    AuthState.WAIT_FOR_NUMBER,
                    AuthState.WAIT_FOR_CODE,
                    AuthState.WAIT_FOR_PASSWORD,
                    AuthState.UNKNOWN -> _isLogin.value = false
                    AuthState.AUTHENTICATED -> _isLogin.value = true
                }
            }
        }
    }
}