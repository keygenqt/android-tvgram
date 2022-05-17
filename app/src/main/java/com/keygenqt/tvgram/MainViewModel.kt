package com.keygenqt.tvgram

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keygenqt.tvgram.base.AuthState
import com.keygenqt.tvgram.base.TelegramApi
import com.keygenqt.tvgram.services.AuthRepository
import com.keygenqt.tvgram.services.ChatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val apiTelegram: TelegramApi,
    private val repoAuth: AuthRepository,
    private val repoChats: ChatsRepository,
) : ViewModel() {

    private val _isLogin = MutableStateFlow<Boolean?>(null)

    val isLogin: StateFlow<Boolean?> get() = _isLogin.asStateFlow()

    init {
        apiTelegram.authState.onEach {
            when (it) {
                AuthState.UNAUTHENTICATED -> {
                    apiTelegram.startAuthentication()
                }
                AuthState.WAIT_FOR_NUMBER, AuthState.WAIT_FOR_CODE, AuthState.WAIT_FOR_PASSWORD, AuthState.WAIT_ENCRYPTION_KEY -> {
                    // login
                }
                AuthState.AUTHENTICATED -> {
                    // loading
                }
                AuthState.UNKNOWN -> {
                    // error
                }
            }
        }.launchIn(viewModelScope)

        viewModelScope.launch {
            delay(5000)
            _isLogin.value = false
        }

        viewModelScope.launch {
            delay(10000)
            _isLogin.value = true
        }
    }
}