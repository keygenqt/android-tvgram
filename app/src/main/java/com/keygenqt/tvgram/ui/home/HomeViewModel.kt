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