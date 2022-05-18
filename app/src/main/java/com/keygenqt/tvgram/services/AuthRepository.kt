package com.keygenqt.tvgram.services

import com.keygenqt.tvgram.base.TelegramApi
import com.keygenqt.tvgram.base.TelegramResponse
import org.drinkless.td.libcore.telegram.TdApi

class AuthRepository(
    private val api: TelegramApi
) {

    /**
     * Send code
     */
    suspend fun setAuthenticationPhoneNumber(
        phone: String
    ): TelegramResponse<TdApi.Object> {
        return api.send(
            TdApi.SetAuthenticationPhoneNumber(
                phone,
                TdApi.PhoneNumberAuthenticationSettings(
                    false,
                    false,
                    false,
                    false,
                    emptyArray()
                )
            )
        )
    }

    /**
     * Check code
     */
    suspend fun checkAuthenticationCode(
        code: String
    ): TelegramResponse<TdApi.Object> {
        return api.send(TdApi.CheckAuthenticationCode(code))
    }
}