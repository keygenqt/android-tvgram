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

class AuthRepository(
    private val api: TelegramApi
) {

    /**
     * Send code
     */
    suspend fun setAuthenticationPhoneNumber(
        phone: String
    ): BaseResponse<TdApi.Object> {
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
    ): BaseResponse<TdApi.Object> {
        return api.send(TdApi.CheckAuthenticationCode(code))
    }

    /**
     * Logout
     */
    suspend fun logout(): BaseResponse<TdApi.Object> {
        return api.send(TdApi.LogOut())
    }
}