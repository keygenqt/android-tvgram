package com.keygenqt.tvgram.data

import org.drinkless.td.libcore.telegram.TdApi

/**
 * Model menu settings
 *
 * @property chat [TdApi.Chat]
 * @property fileImage [TdApi.File]
 */
data class HomeModel(
    val chat: TdApi.Chat,
    val message: TdApi.Message?,
    var fileImage: TdApi.File?
)