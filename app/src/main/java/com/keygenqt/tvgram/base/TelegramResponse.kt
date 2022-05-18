package com.keygenqt.tvgram.base

import com.keygenqt.tvgram.exceptions.ApiException

sealed class TelegramResponse<out R> {
    data class Success<out T>(val data: T) : TelegramResponse<T>()
    data class Error(val exception: ApiException) : TelegramResponse<Nothing>()
}

inline infix fun <T> TelegramResponse<T>.success(listener: (data: T) -> Unit): TelegramResponse<T> {
    if (this is TelegramResponse.Success && this.data != null) {
        listener.invoke(this.data)
    }
    return this
}

inline infix fun <T> TelegramResponse<T>.error(predicate: (data: ApiException) -> Unit): TelegramResponse<T> {
    if (this is TelegramResponse.Error) {
        predicate.invoke(this.exception)
    }
    return this
}