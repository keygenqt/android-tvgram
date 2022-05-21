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
package com.keygenqt.tvgram.services

import com.keygenqt.tvgram.base.TelegramApi
import com.keygenqt.tvgram.base.BaseResponse
import org.drinkless.td.libcore.telegram.TdApi

class ChatsRepository(
    private val api: TelegramApi
) {

    /**
     * Get main chats ids
     */
    suspend fun getChatsIds(
        limit: Int
    ): BaseResponse<TdApi.Chats> {
        return api.send(TdApi.GetChats(TdApi.ChatListMain(), limit))
    }

    /**
     * Get chat by id
     */
    suspend fun getChatById(
        chatId: Long
    ): BaseResponse<TdApi.Chat> {
        return api.send(TdApi.GetChat(chatId))
    }

    /**
     * Get list messages
     */
    suspend fun getHistory(
        chatId: Long,
        messageId: Long,
        limit: Int = 30
    ): BaseResponse<TdApi.Messages> {
        return api.send(TdApi.GetChatHistory(chatId, messageId, 0, limit, false))
    }
}