package com.keygenqt.tvgram.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keygenqt.tvgram.base.error
import com.keygenqt.tvgram.base.success
import com.keygenqt.tvgram.services.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: AuthRepository,
) : ViewModel() {

    private val _isSuccess = MutableStateFlow<Boolean?>(null)

    val isSuccess: StateFlow<Boolean?> get() = _isSuccess.asStateFlow()

    private val _isError = MutableStateFlow<String?>(null)

    val isError: StateFlow<String?> get() = _isError.asStateFlow()

    fun sendPhone(
        phone: String
    ) {
        viewModelScope.launch {
            repo.setAuthenticationPhoneNumber(phone)
                .success {
                    _isSuccess.value = true
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