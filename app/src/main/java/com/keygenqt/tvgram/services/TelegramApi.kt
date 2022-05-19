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
package com.keygenqt.tvgram.base

import com.keygenqt.tvgram.exceptions.ApiException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import timber.log.Timber

enum class AuthState {
    UNAUTHENTICATED,
    WAIT_ENCRYPTION_KEY,
    WAIT_FOR_NUMBER,
    WAIT_FOR_CODE,
    WAIT_FOR_PASSWORD,
    AUTHENTICATED,
    UNKNOWN
}

class TelegramApi(
    private val parameters: TdApi.TdlibParameters,
    private val _authState: MutableStateFlow<AuthState> = MutableStateFlow(AuthState.UNKNOWN),
) : Client.ResultHandler {

    private val client = Client.create(this, {
        Timber.e(it)
    }, {
        Timber.e(it)
    }).apply {
        send(TdApi.SetLogVerbosityLevel(1), this@TelegramApi)
        send(TdApi.GetAuthorizationState(), this@TelegramApi)
    }

    val authState: StateFlow<AuthState> get() = _authState.asStateFlow()

    override fun onResult(data: TdApi.Object?) {
        when (data?.constructor) {
            TdApi.UpdateAuthorizationState.CONSTRUCTOR -> {
                onAuthorizationStateUpdated((data as TdApi.UpdateAuthorizationState).authorizationState)
            }
        }
    }

    fun startAuthentication() {
        if (_authState.value != AuthState.UNAUTHENTICATED) {
            throw IllegalStateException("Start authentication called but client already authenticated. State: ${_authState.value}.")
        }
        client.send(TdApi.SetTdlibParameters(parameters)) {

        }
    }

    fun checkDatabaseEncryptionKey() {
        if (_authState.value == AuthState.WAIT_ENCRYPTION_KEY) {
            client.send(TdApi.CheckDatabaseEncryptionKey()) {

            }
        }
    }

    private fun onAuthorizationStateUpdated(authorizationState: TdApi.AuthorizationState) {
        _authState.value = when (authorizationState.constructor) {
            TdApi.AuthorizationStateWaitEncryptionKey.CONSTRUCTOR -> AuthState.WAIT_ENCRYPTION_KEY
            TdApi.AuthorizationStateWaitTdlibParameters.CONSTRUCTOR -> AuthState.UNAUTHENTICATED
            TdApi.AuthorizationStateWaitPhoneNumber.CONSTRUCTOR -> AuthState.WAIT_FOR_NUMBER
            TdApi.AuthorizationStateWaitCode.CONSTRUCTOR -> AuthState.WAIT_FOR_CODE
            TdApi.AuthorizationStateWaitPassword.CONSTRUCTOR -> AuthState.WAIT_FOR_PASSWORD
            TdApi.AuthorizationStateReady.CONSTRUCTOR -> AuthState.AUTHENTICATED
            TdApi.AuthorizationStateLoggingOut.CONSTRUCTOR -> AuthState.UNAUTHENTICATED
            else -> AuthState.UNKNOWN
        }
    }

    suspend fun <T> send(query: TdApi.Function): BaseResponse<T> {
        return callbackFlow<BaseResponse<T>> {
            client.send(query, {
                if (it is TdApi.Error) {
                    trySend(
                        BaseResponse.Error(
                            ApiException(
                                code = it.code,
                                message = it.message
                            )
                        )
                    )
                } else {
                    (it as? T)?.let { result ->
                        trySend(BaseResponse.Success(result))
                    } ?: run {
                        trySend(
                            BaseResponse.Error(
                                ApiException(
                                    code = -2,
                                    message = "Error loading data"
                                )
                            )
                        )
                    }
                }
            }, {
                trySend(
                    BaseResponse.Error(
                        ApiException(
                            code = -1,
                            message = it.message ?: "App error"
                        )
                    )
                )
            })
            awaitClose {}
        }.first()
    }


}