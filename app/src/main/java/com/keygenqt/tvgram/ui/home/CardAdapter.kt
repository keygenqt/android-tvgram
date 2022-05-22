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

import android.text.format.DateUtils
import android.view.View
import androidx.annotation.LayoutRes
import androidx.leanback.app.BrowseSupportFragment
import com.keygenqt.tvgram.R
import com.keygenqt.tvgram.base.BaseArrayObjectAdapter
import com.keygenqt.tvgram.data.MessageModel
import com.keygenqt.tvgram.databinding.HomeAdapterItemBinding
import com.keygenqt.tvgram.extensions.isUrl
import com.keygenqt.tvgram.extensions.loadingImage
import com.keygenqt.tvgram.extensions.toDate
import org.drinkless.td.libcore.telegram.TdApi

/**
 * Adapter for post items
 */
class CardAdapter(
    fragment: BrowseSupportFragment,
    @LayoutRes id: Int
) : BaseArrayObjectAdapter(fragment, id) {

    override fun onBindView(holder: View, model: Any) {
        HomeAdapterItemBinding.bind(holder).apply {

            blockMessage.visibility = View.GONE
            blockSettings.visibility = View.GONE

            if (model is MessageModel) {
                blockMessage.visibility = View.VISIBLE
                includeBlockMessage.apply {

                    val content = model.message.content

                    ivImageType.setImageResource(
                        when (content) {
                            is TdApi.MessagePhoto -> R.drawable.ic_baseline_image_24
                            is TdApi.MessageDocument -> R.drawable.ic_baseline_insert_drive_file_24
                            is TdApi.MessageVideo,
                            is TdApi.MessageVideoNote -> R.drawable.ic_baseline_play_circle_filled_24
                            is TdApi.MessageText -> if (content.isUrl) {
                                R.drawable.ic_baseline_link_24
                            } else {
                                R.drawable.ic_baseline_textsms_24
                            }
                            is TdApi.MessageVoiceNote -> R.drawable.ic_baseline_record_voice_over_24
                            is TdApi.MessageCall -> R.drawable.ic_baseline_call_24
                            else -> R.drawable.ic_baseline_contact_support_24
                        }
                    )

                    tvDesc.maxLines = when (content) {
                        is TdApi.MessagePhoto -> 5
                        is TdApi.MessageDocument -> 3
                        is TdApi.MessageVideo -> 3
                        is TdApi.MessageVideoNote -> 5
                        is TdApi.MessageText -> 5
                        is TdApi.MessageVoiceNote -> 5
                        is TdApi.MessageCall -> 5
                        else -> 2
                    }

                    val title = when (content) {
                        is TdApi.MessagePhoto -> null
                        is TdApi.MessageDocument -> content.document.fileName
                        is TdApi.MessageVideo -> content.video.fileName
                        is TdApi.MessageVideoNote -> null
                        is TdApi.MessageText -> null
                        is TdApi.MessageVoiceNote -> null
                        is TdApi.MessageCall -> null
                        else -> null
                    }

                    val desc = when (content) {
                        is TdApi.MessagePhoto -> content.caption.text
                        is TdApi.MessageDocument -> content.caption.text
                        is TdApi.MessageVideo -> content.caption.text
                        is TdApi.MessageVideoNote -> null
                        is TdApi.MessageText -> content.text.text
                        is TdApi.MessageVoiceNote -> content.caption.text
                        is TdApi.MessageCall -> null
                        else -> null
                    }

                    tvTitle.visibility =
                        if (title.isNullOrBlank()) View.GONE else View.VISIBLE

                    tvDesc.visibility =
                        if (desc.isNullOrBlank()) View.GONE else View.VISIBLE

                    tvTitle.text = title
                    tvDesc.text = desc
                    tvDate.text =
                        context.getString(R.string.home_card_date, model.message.date.toDate())

                    if (content is TdApi.MessageVideo) {
                        tvDuration.text = context.getString(
                            R.string.home_card_duration,
                            DateUtils.formatElapsedTime(content.video.duration.toLong())
                        )
                    } else {
                        tvDuration.text = ""
                    }

                    model.drawable?.let {
                        ivImage.setImageBitmap(it)
                    } ?: run {
                        model.file?.local?.path?.let { path ->
                            context.loadingImage(path) {
                                ivImage.setImageBitmap(it)
                                model.drawable = it
                            }
                        } ?: run {
                            ivImage.setImageBitmap(null)
                        }
                    }
                }
            } else if (model is String) {
                blockSettings.visibility = View.VISIBLE
                includeBlockSettings.apply {
                    when (model) {
                        context.getString(R.string.home_card_settings) -> {
                            tvTitle.text = model
                            ivImageSettings.setImageResource(R.drawable.ic_baseline_settings_24)
                        }
                    }
                }
            }
        }
    }
}