package com.keygenqt.tvgram.services

import com.keygenqt.tvgram.base.TelegramApi
import com.keygenqt.tvgram.base.TelegramResponse
import org.drinkless.td.libcore.telegram.TdApi

class ChatsRepository(
    private val api: TelegramApi
) {

    /**
     * Get main chats ids
     */
    suspend fun getChatsIds(
        limit: Int
    ): TelegramResponse<TdApi.Chats> {
        return api.send(TdApi.GetChats(TdApi.ChatListMain(), limit))
    }

    /**
     * Get chat by id
     */
    suspend fun getChatById(
        id: Long
    ): TelegramResponse<TdApi.Chat> {
        return api.send(TdApi.GetChat(id))
    }
}