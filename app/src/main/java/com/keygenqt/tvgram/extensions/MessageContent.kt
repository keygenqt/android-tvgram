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
package com.keygenqt.tvgram.extensions

import android.util.Patterns
import org.drinkless.td.libcore.telegram.TdApi

/**
 * Get massage file ID
 */
inline val TdApi.MessageContent.messageFileId: Int?
    get() = when (this) {
        is TdApi.MessagePhoto -> this.photo.sizes.last().photo.id
        is TdApi.MessageVideo -> this.video.thumbnail?.file?.id
        is TdApi.MessageVideoNote -> this.videoNote.thumbnail?.file?.id
        is TdApi.MessageAnimation -> this.animation.thumbnail?.file?.id
        is TdApi.MessageSticker -> this.sticker.thumbnail?.file?.id
        else -> null
    }

/**
 * Check is url [TdApi.MessageText]
 */
inline val TdApi.MessageContent.isUrl: Boolean
    get() = when (this) {
        is TdApi.MessageText -> !text.text.isNullOrBlank() && Patterns.WEB_URL.matcher(text.text).matches()
        else -> false
    }