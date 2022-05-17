package com.keygenqt.tvgram.services

import com.keygenqt.tvgram.base.TelegramApi
import com.keygenqt.tvgram.base.TelegramResponse
import org.drinkless.td.libcore.telegram.TdApi

class ChatsRepository(
    private val api: TelegramApi
) {

    suspend fun getChats(
        limit: Int
    ): TelegramResponse<List<TdApi.Chat>> {
        return api.send(TdApi.GetChats(TdApi.ChatListMain(), limit))
    }
}