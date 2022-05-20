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

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import coil.load
import com.keygenqt.tvgram.R
import com.keygenqt.tvgram.data.HomeModel
import org.drinkless.td.libcore.telegram.TdApi

/**
 * Set image by type last message
 */
fun ImageView.setChatDrawable(model: HomeModel) {
    when (val content = model.chat.lastMessage?.content) {
        is TdApi.MessagePhoto -> {
            load(model.fileImage?.local?.path)
        }
        is TdApi.MessageVideo -> {
            setCardVideo(model.fileImage?.local?.path)
        }
        is TdApi.MessageVideoNote -> {
            setCardImage(model.fileImage?.local?.path, true)
        }
        is TdApi.MessageAnimation -> {
            load(model.fileImage?.local?.path)
        }
        is TdApi.MessageSticker -> {
            setCardImage(model.fileImage?.local?.path)
        }
        is TdApi.MessageVoiceNote -> {
            setImageDrawable(
                AppCompatResources.getDrawable(
                    context,
                    R.drawable.dr_message_type_voice
                )
            )
        }
        is TdApi.MessageText -> {
            if (android.util.Patterns.WEB_URL.matcher(content.text.text ?: "").matches()) {
                setImageDrawable(
                    AppCompatResources.getDrawable(
                        context,
                        R.drawable.dr_message_type_url
                    )
                )
            } else {
                setImageDrawable(
                    AppCompatResources.getDrawable(
                        context,
                        R.drawable.dr_message_type_text
                    )
                )
            }
        }
        is TdApi.MessageDocument -> {
            setImageDrawable(
                AppCompatResources.getDrawable(
                    context,
                    R.drawable.dr_message_type_document
                )
            )
        }
        is TdApi.MessageCall -> {
            setImageDrawable(
                AppCompatResources.getDrawable(
                    context,
                    R.drawable.dr_message_type_call
                )
            )
        }
        else -> {
            setImageDrawable(
                AppCompatResources.getDrawable(
                    context,
                    R.drawable.dr_message_type_undefined
                )
            )
        }
    }
}

/**
 * Merge image with background
 */
fun ImageView.setCardImage(path: String?, round: Boolean = false) {
    if (path != null) {

        val bg: Bitmap = AppCompatResources.getDrawable(
            context,
            R.drawable.card_default_background
        )?.toBitmap(500, 350)!!

        context.loading(path, round) {
            setImageBitmap(it.resizeAdaptive(250).bitmapMerge(bg))
        }
    }
}

/**
 * Merge image Video with background
 */
fun ImageView.setCardVideo(path: String?) {
    if (path != null) {
        context.loading(path, false) {

            val bg: Bitmap = AppCompatResources.getDrawable(
                context,
                R.drawable.card_video_background
            )?.toBitmap(it.width, it.height)!!

            val playBt: Bitmap = AppCompatResources.getDrawable(
                context,
                R.drawable.ic_baseline_play_circle_filled_24
            )?.toBitmap(90, 90)!!

            setImageBitmap(playBt.bitmapMerge(bg.bitmapMerge(it)))
        }
    }
}