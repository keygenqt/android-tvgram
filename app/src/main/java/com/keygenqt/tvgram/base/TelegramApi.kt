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
) : Client.ResultHandler {

    private val client = Client.create(this, {
        Timber.e(it)
    }, {
        Timber.e(it)
    }).apply {
        send(TdApi.SetLogVerbosityLevel(1), this@TelegramApi)
        send(TdApi.GetAuthorizationState(), this@TelegramApi)
    }

    private val _authState = MutableStateFlow(AuthState.UNKNOWN)
    val authState: StateFlow<AuthState> get() = _authState.asStateFlow()

    suspend fun checkAuthUser() = send<TdApi.Object>(TdApi.SetTdlibParameters(parameters))

    override fun onResult(data: TdApi.Object?) {
        when (data?.constructor) {
            TdApi.UpdateAuthorizationState.CONSTRUCTOR -> {
                Timber.e("!!!! ${(data as TdApi.UpdateAuthorizationState).authorizationState}")
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

    private fun onAuthorizationStateUpdated(authorizationState: TdApi.AuthorizationState) {

        Timber.e("authorizationState: ${authorizationState}")

        _authState.value = when (authorizationState.constructor) {
            TdApi.AuthorizationStateWaitEncryptionKey.CONSTRUCTOR -> {
                client.send(TdApi.CheckDatabaseEncryptionKey()) {
                    when (it.constructor) {
                        TdApi.Ok.CONSTRUCTOR -> {
                            Timber.e("CheckDatabaseEncryptionKey: OK")
                        }
                        TdApi.Error.CONSTRUCTOR -> {
                            Timber.e("CheckDatabaseEncryptionKey: Error")
                        }
                    }
                }
                AuthState.WAIT_ENCRYPTION_KEY
            }
            TdApi.AuthorizationStateWaitTdlibParameters.CONSTRUCTOR -> AuthState.UNAUTHENTICATED
            TdApi.AuthorizationStateWaitPhoneNumber.CONSTRUCTOR -> AuthState.WAIT_FOR_NUMBER
            TdApi.AuthorizationStateWaitCode.CONSTRUCTOR -> AuthState.WAIT_FOR_CODE
            TdApi.AuthorizationStateWaitPassword.CONSTRUCTOR -> AuthState.WAIT_FOR_PASSWORD
            TdApi.AuthorizationStateReady.CONSTRUCTOR -> AuthState.AUTHENTICATED
            TdApi.AuthorizationStateLoggingOut.CONSTRUCTOR -> AuthState.UNAUTHENTICATED
            else -> AuthState.UNKNOWN
        }
    }

    suspend fun <T> send(query: TdApi.Function): TelegramResponse<T> {
        return callbackFlow<TelegramResponse<T>> {
            client.send(query, {
                if (it is TdApi.Error) {
                    trySend(
                        TelegramResponse.Error(
                            ApiException(
                                code = it.code,
                                message = it.message
                            )
                        )
                    )
                } else {
                    (it as? T)?.let { result ->
                        trySend(TelegramResponse.Success(result))
                    } ?: run {
                        trySend(
                            TelegramResponse.Error(
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
                    TelegramResponse.Error(
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