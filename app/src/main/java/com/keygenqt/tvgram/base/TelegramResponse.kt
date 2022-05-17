package com.keygenqt.tvgram.base

import org.drinkless.td.libcore.telegram.TdApi

sealed class TelegramResponse<out R> {
    data class Success<out T>(val data: T) : TelegramResponse<T>()
    data class Error(val exception: Exception) : TelegramResponse<Nothing>()
}

val TelegramResponse<*>?.isSuccess get() = this is TelegramResponse.Success

val TelegramResponse<*>?.isError get() = this is TelegramResponse.Error

inline infix fun <T> TelegramResponse<T>.success(listener: (data: T) -> Unit): TelegramResponse<T> {
    if (this is TelegramResponse.Success && this.data != null) {
        listener.invoke(this.data)
    }
    return this
}

inline infix fun <T> TelegramResponse<T>.error(predicate: (data: Exception) -> Unit): TelegramResponse<T> {
    if (this is TelegramResponse.Error) {
        predicate.invoke(this.exception)
    }
    return this
}