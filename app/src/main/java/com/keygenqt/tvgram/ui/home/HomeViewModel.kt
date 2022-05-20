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
import com.keygenqt.tvgram.base.isSuccess
import com.keygenqt.tvgram.base.success
import com.keygenqt.tvgram.data.HomeModel
import com.keygenqt.tvgram.extensions.messageFileId
import com.keygenqt.tvgram.preferences.BasePreferences
import com.keygenqt.tvgram.services.ChatsRepository
import com.keygenqt.tvgram.services.CommonRepository
import com.keygenqt.tvgram.ui.auth.ValidateCodeFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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

    /**
     * Response chat data
     */
    private val _homeModels = MutableStateFlow(emptyList<HomeModel>())

    /**
     * [StateFlow] for variable [_homeModels]
     */
    val homeModels: StateFlow<List<HomeModel>> get() = _homeModels.asStateFlow()

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

                    val chats = it.chatIds
                        .map { id -> async { repoChats.getChatById(id) } }
                        .map { deferred -> deferred.await() }
                        .mapNotNull { response -> if (response.isSuccess) (response as BaseResponse.Success).data else null }
                        .filter { chat ->

                            if (chat.lastMessage == null) {
                                return@filter false
                            }

                            when (chat.type.constructor) {
                                TdApi.ChatTypeSecret.CONSTRUCTOR -> preferences.isChatSecret
                                TdApi.ChatTypePrivate.CONSTRUCTOR -> preferences.isChatPrivate
                                else -> true
                            }
                        }

                    val files =
                        chats.map { chat ->
                            async {
                                repoCommon.getImage(chat.lastMessage?.content?.messageFileId)
                            }
                        }
                            .map { deferred -> deferred.await() }
                            .map { response -> if (response.isSuccess) (response as BaseResponse.Success).data else null }

                    _homeModels.value = chats.map { chat ->
                        HomeModel(
                            chat = chat,
                            message = chat.lastMessage!!,
                            fileImage = files.firstOrNull { file ->
                                file?.id == chat.lastMessage?.content?.messageFileId
                            }
                        )
                    }
                }
                .error {
                    _isError.value = it.message
                }
        }
    }
}