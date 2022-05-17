package com.keygenqt.tvgram.services

import com.keygenqt.tvgram.base.TelegramApi
import com.keygenqt.tvgram.base.TelegramResponse
import org.drinkless.td.libcore.telegram.TdApi

class AuthRepository(
    private val api: TelegramApi
) {

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

//    suspend fun sendPhoneNumberVerificationCode(
//        phone: String
//    ): TelegramResponse<List<TdApi.Chat>> {
//        return api.send(
//            TdApi.AuthenticationCodeTypeSms(phone, null)
//        )
//    }
}