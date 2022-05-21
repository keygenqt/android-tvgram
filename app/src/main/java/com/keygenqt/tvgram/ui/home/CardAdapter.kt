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

import android.view.View
import androidx.annotation.LayoutRes
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.Presenter
import com.keygenqt.tvgram.base.BaseArrayObjectAdapter
import com.keygenqt.tvgram.data.MessageModel
import com.keygenqt.tvgram.databinding.HomeAdapterItemBinding
import com.keygenqt.tvgram.databinding.SettingsAdapterItemBinding
import com.keygenqt.tvgram.extensions.toDate
import timber.log.Timber


/**
 * Presenter for post items
 */
class CardAdapter(
    fragment: BrowseSupportFragment,
    @LayoutRes id: Int
) : BaseArrayObjectAdapter(fragment, id) {

    override fun onBindView(holder: View, model: Any) {
        if (model is MessageModel) {
            HomeAdapterItemBinding.bind(holder).apply {
                tvSettingsItem.text = model.message.date.toDate()
            }
        }
        else if (model is String) {
            HomeAdapterItemBinding.bind(holder).apply {
                tvSettingsItem.text = model
            }
        }
    }

    override fun onSelected(model: Any) {
        Timber.e("onSelected")
    }

    override fun onClick(model: Any) {
        Timber.e("onClick")
    }

    //    companion object {
//        const val CARD_WIDTH = 500
//        const val CARD_HEIGHT = 350
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup?): ViewHolder {
//        val cardView = object : ImageCardView(context) {}
//        cardView.isFocusable = true
//        cardView.isFocusableInTouchMode = true
//        return ViewHolder(cardView)
//    }
//
//    override fun onBindViewHolder(viewHolder: ViewHolder, model: Any) {
//
//        val cardView = viewHolder.view as ImageCardView
//
//        if (model is MessageModel) {
//
//            cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT)
//            cardView.cardType = BaseCardView.CARD_TYPE_INFO_UNDER
//
//            val type = model.message.content?.let {
//                it::class.java.simpleName.replace("Message", "")
//            } ?: "Empty"
//
//            cardView.titleText = when (val content = model.message.content) {
//                is TdApi.MessageVideoNote -> type
//                is TdApi.MessageCall -> type
//                is TdApi.MessageSticker -> type
//                is TdApi.MessageChatDeleteMember -> type
//                is TdApi.MessageVoiceNote -> content.caption.text.ifBlank { type }
//                is TdApi.MessageText -> content.text.text.ifBlank { type }
//                is TdApi.MessagePhoto -> content.caption.text.ifBlank { type }
//                is TdApi.MessageAnimation -> content.caption.text.ifBlank { type }
//                is TdApi.MessageDocument -> content.caption.text.ifEmpty { content.document.fileName }
//                is TdApi.MessageVideo -> content.caption.text.ifBlank { content.video.fileName }
//                else -> context.getString(R.string.home_card_type_undefined, type)
//            }
//
//            model.drawable?.let {
//                cardView.mainImageView.setImageBitmap(it)
//            } ?: run {
//                cardView.mainImageView.setChatImage(model) {
//                    model.drawable = it
//                }
//            }
//
//            cardView.contentText = context.getString(
//                R.string.home_card_date,
//                model.message.date.toDate(),
//            )
//        } else if (model is Long) {
//            cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT + 100)
//            cardView.cardType = BaseCardView.CARD_TYPE_MAIN_ONLY
//            cardView.mainImageView.setImageDrawable(
//                AppCompatResources.getDrawable(
//                    context,
//                    R.drawable.card_next_background
//                )
//            )
//        }
//    }
//
//    override fun onUnbindViewHolder(viewHolder: ViewHolder?) {
//    }


    //    companion object {
//        const val CARD_WIDTH = 500
//        const val CARD_HEIGHT = 350
//    }
//
//    override fun onBindCardView(cardView: ImageCardView, model: Any) {
//        if (model is MessageModel) {
//
//            cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT)
//            cardView.cardType = BaseCardView.CARD_TYPE_INFO_UNDER
//
//            val type = model.message.content?.let {
//                it::class.java.simpleName.replace("Message", "")
//            } ?: "Empty"
//
//            cardView.titleText = when (val content = model.message.content) {
//                is TdApi.MessageVideoNote -> type
//                is TdApi.MessageCall -> type
//                is TdApi.MessageSticker -> type
//                is TdApi.MessageChatDeleteMember -> type
//                is TdApi.MessageVoiceNote -> content.caption.text.ifBlank { type }
//                is TdApi.MessageText -> content.text.text.ifBlank { type }
//                is TdApi.MessagePhoto -> content.caption.text.ifBlank { type }
//                is TdApi.MessageAnimation -> content.caption.text.ifBlank { type }
//                is TdApi.MessageDocument -> content.caption.text.ifEmpty { content.document.fileName }
//                is TdApi.MessageVideo -> content.caption.text.ifBlank { content.video.fileName }
//                else -> context.getString(R.string.home_card_type_undefined, type)
//            }
//
//            model.drawable?.let {
//                cardView.mainImageView.setImageBitmap(it)
//            } ?: run {
//                cardView.mainImageView.setChatImage(model) {
//                    model.drawable = it
//                }
//            }
//
//            cardView.contentText = context.getString(
//                R.string.home_card_date,
//                model.message.date.toDate(),
//            )
//        } else if (model is Long) {
//            cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT + 100)
//            cardView.cardType = BaseCardView.CARD_TYPE_MAIN_ONLY
//            cardView.mainImageView.setImageDrawable(
//                AppCompatResources.getDrawable(
//                    context,
//                    R.drawable.card_next_background
//                )
//            )
//        }
//    }
//
//    override fun onSelected(model: Any) {
//        Timber.e("onSelected: ${model::class.java.simpleName}")
//    }
//
//    override fun onClick(model: Any) {
//        Timber.e("onClick: ${model::class.java.simpleName}")
//    }
}