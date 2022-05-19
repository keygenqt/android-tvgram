package com.keygenqt.tvgram.extensions

import org.drinkless.td.libcore.telegram.TdApi

/**
 * Get massage file ID
 */
inline val TdApi.MessageContent.messageFileId: Int?
    get() = when (this) {
        is TdApi.MessagePhoto -> this.photo.sizes.last().photo.id
        is TdApi.MessageInvoice -> this.photo?.sizes?.last()?.photo?.id
        is TdApi.MessageVideo -> this.video.thumbnail?.file?.id
        else -> null
    }