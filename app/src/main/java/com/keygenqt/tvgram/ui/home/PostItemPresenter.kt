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
package com.keygenqt.tvgram.ui.home

import android.content.Context
import android.view.ViewGroup
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter
import com.keygenqt.tvgram.R
import com.keygenqt.tvgram.data.HomeModel
import com.keygenqt.tvgram.extensions.setChatDrawable
import com.keygenqt.tvgram.extensions.toDate
import org.drinkless.td.libcore.telegram.TdApi


/**
 * Presenter for post items
 */
class PostItemPresenter(val context: Context) : Presenter() {

    companion object {
        const val CARD_WIDTH = 500
        const val CARD_HEIGHT = 350
    }

    override fun onCreateViewHolder(parent: ViewGroup?): ViewHolder {
        val cardView = object : ImageCardView(context) {}
        cardView.isFocusable = true
        cardView.isFocusableInTouchMode = true
        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {

        val model = item as HomeModel
        val cardView = viewHolder.view as ImageCardView

        val type = model.message.content?.let {
            it::class.java.simpleName.replace("Message", "")
        } ?: "Empty"

        cardView.titleText = when (val content = model.message.content) {
            is TdApi.MessageVideoNote -> type
            is TdApi.MessageCall -> type
            is TdApi.MessageSticker -> type
            is TdApi.MessageChatDeleteMember -> type
            is TdApi.MessageVoiceNote -> content.caption.text.ifBlank { type }
            is TdApi.MessageText -> content.text.text.ifBlank { type }
            is TdApi.MessagePhoto -> content.caption.text.ifBlank { type }
            is TdApi.MessageAnimation -> content.caption.text.ifBlank { type }
            is TdApi.MessageDocument -> content.caption.text.ifEmpty { content.document.fileName }
            is TdApi.MessageVideo -> content.caption.text.ifBlank { content.video.fileName }
            else -> context.getString(R.string.home_card_type_undefined, type)
        }

        cardView.mainImageView.setChatDrawable(model)

        cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT)
        cardView.contentText = context.getString(
            R.string.home_card_date,
            model.message.date.toDate(),
        )
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder?) {
    }

}