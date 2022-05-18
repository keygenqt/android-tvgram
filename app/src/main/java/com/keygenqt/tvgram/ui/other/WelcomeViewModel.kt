package com.keygenqt.tvgram.ui.other

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keygenqt.tvgram.base.AuthState
import com.keygenqt.tvgram.base.TelegramApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val apiTelegram: TelegramApi,
) : ViewModel() {

    private val _isLogin = MutableStateFlow<Boolean?>(null)

    val isLogin: StateFlow<Boolean?> get() = _isLogin.asStateFlow()

    fun startAuthentication() {
        viewModelScope.launch {
            delay(1500)
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