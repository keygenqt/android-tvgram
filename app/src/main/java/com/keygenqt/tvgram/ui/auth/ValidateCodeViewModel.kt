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
     * Auth by code
     */
    fun validateCode(
        code: String
    ) {
        viewModelScope.launch {
            repo.checkAuthenticationCode(code)
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