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
package com.keygenqt.tvgram.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keygenqt.tvgram.base.BaseResponse
import com.keygenqt.tvgram.base.error
import com.keygenqt.tvgram.base.success
import com.keygenqt.tvgram.data.ChatModel
import com.keygenqt.tvgram.data.MessageModel
import com.keygenqt.tvgram.extensions.messageFileId
import com.keygenqt.tvgram.preferences.BasePreferences
import com.keygenqt.tvgram.services.ChatsRepository
import com.keygenqt.tvgram.services.CommonRepository
import com.keygenqt.tvgram.ui.auth.ValidateCodeFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.drinkless.td.libcore.telegram.TdApi
import javax.inject.Inject

/**
 * [ValidateCodeFragment] viewModel
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val preferences: BasePreferences,
    private val repoChats: ChatsRepository,
    private val repoCommon: CommonRepository,
) : ViewModel() {

    private var isChatSecret: Boolean = preferences.isChatSecret
    private var isChatPrivate: Boolean = preferences.isChatPrivate
    private var isChatSupergroup: Boolean = preferences.isChatSupergroup

    /**
     * Response chat data
     */
    private val _homeModels = MutableStateFlow<List<ChatModel>?>(null)

    /**
     * [StateFlow] for variable [_homeModels]
     */
    val homeModels: StateFlow<List<ChatModel>?> get() = _homeModels.asStateFlow()

    /**
     * Error response after query
     */
    private val _isError = MutableStateFlow<String?>(null)

    /**
     * [StateFlow] for variable [_isError]
     */
    val isError: StateFlow<String?> get() = _isError.asStateFlow()

    /**
     * Check change pref
     */
    fun checkPrefChats(): Boolean {
        return if (isChatSecret == preferences.isChatSecret
            && isChatPrivate == preferences.isChatPrivate
            && isChatSupergroup == preferences.isChatSupergroup
        ) {
            true
        } else {
            isChatSecret = preferences.isChatSecret
            isChatPrivate = preferences.isChatPrivate
            isChatSupergroup = preferences.isChatSupergroup
            false
        }
    }

    /**
     * Get chats
     */
    fun updateChats() {
        viewModelScope.launch {
            _homeModels.value = null
            repoChats.getChatsIds(Int.MAX_VALUE)
                .success {
                    val list = it.chatIds
                        .map { id ->
                            withContext(Dispatchers.Default) {
                                repoChats.getChatById(id)
                            }
                        }
                        .mapNotNull { response ->
                            if (response is BaseResponse.Success && response.data.lastMessage != null) {
                                ChatModel(chat = response.data)
                            } else {
                                null
                            }
                        }
                        .filter { chat ->
                            when (chat.chat.type.constructor) {
                                TdApi.ChatTypeSecret.CONSTRUCTOR -> preferences.isChatSecret
                                TdApi.ChatTypePrivate.CONSTRUCTOR -> preferences.isChatPrivate
                                TdApi.ChatTypeSupergroup.CONSTRUCTOR -> preferences.isChatSupergroup
                                else -> true
                            }
                        }

                    list.forEach { chat ->
                        repoChats.getHistory(chat.chat.id, chat.chat.lastMessage?.id!!)
                            .success { messages ->
                                chat.totalCountMessages = messages.totalCount
                                chat.messages =
                                    (listOf(chat.chat.lastMessage!!) + messages.messages)
                                        .map { m -> MessageModel(message = m) }
                                chat.messages.forEach { message ->
                                    repoCommon
                                        .getFile(message.message.content.messageFileId)
                                        .success { file -> message.file = file }
                                }
                            }
                    }

                    _homeModels.value = list
                }
                .error {
                    _isError.value = it.message
                }
        }
    }

    /**
     * Update row posts
     */
    fun updateRow(messageId: Long, success: () -> Unit) {
        viewModelScope.launch {
            _homeModels.value?.let { chats ->
                for (chat in chats) {
                    if (chat.messages.last().message.id == messageId) {
                        repoChats.getHistory(chat.chat.id, messageId)
                            .success { messages ->
                                val result =
                                    messages.messages.map { m -> MessageModel(message = m) }
                                result.forEach { message ->
                                    repoCommon
                                        .getFile(message.message.content.messageFileId)
                                        .success { file -> message.file = file }
                                }
                                chat.messages = result
                            }
                    }
                    break
                }
            }
            success.invoke()
        }
    }
}