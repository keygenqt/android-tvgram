package com.keygenqt.tvgram.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.request.SuccessResult
import com.keygenqt.tvgram.base.*
import com.keygenqt.tvgram.services.ChatsRepository
import com.keygenqt.tvgram.ui.auth.ValidateCodeFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.drinkless.td.libcore.telegram.TdApi
import javax.inject.Inject

/**
 * [ValidateCodeFragment] viewModel
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repoChats: ChatsRepository,
) : ViewModel() {

    /**
     * Response chat data
     */
    private val _chats = MutableStateFlow(emptyList<TdApi.Chat>())

    /**
     * [StateFlow] for variable [_chats]
     */
    val chats: StateFlow<List<TdApi.Chat>> get() = _chats.asStateFlow()

    /**
     * Error response after query
     */
    private val _isError = MutableStateFlow<String?>(null)

    /**
     * [StateFlow] for variable [_isError]
     */
    val isError: StateFlow<String?> get() = _isError.asStateFlow()

    /**
     * Get chats
     */
    fun updateChats() {
        viewModelScope.launch {
            repoChats.getChatsIds(Int.MAX_VALUE)
                .success {
                    _chats.value = it.chatIds
                        .map { id -> async { repoChats.getChatById(id) } }
                        .map { deferred -> deferred.await() }
                        .mapNotNull { response -> if (response.isSuccess) (response as TelegramResponse.Success).data else null }
                }
                .error {
                    _isError.value = it.message
                }
        }
    }
}