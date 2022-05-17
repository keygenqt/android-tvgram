package com.keygenqt.tvgram.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keygenqt.tvgram.base.AuthState
import com.keygenqt.tvgram.base.TelegramApi
import com.keygenqt.tvgram.services.AuthRepository
import com.keygenqt.tvgram.services.ChatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val apiTelegram: TelegramApi,
    private val repoAuth: AuthRepository,
    private val repoChats: ChatsRepository,
) : ViewModel() {

    fun startAuthentication() {


        apiTelegram.authState.onEach {

            Timber.e(it.toString())

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


//            repoAuth.sendPhoneNumberVerificationCode("+79673015470")
//                .success { data ->
//                    Timber.e(data.toString())
//                }
//                .error {
//                    Timber.e(it.toString())
//                }

//            repoChats.getChats(10)
//                .success {
//                    Timber.e("success: $it")
//                }
//                .error {
//                    Timber.e("error: $it")
//                }
        }
    }
}