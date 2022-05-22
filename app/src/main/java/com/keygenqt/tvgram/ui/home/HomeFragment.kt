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

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.leanback.app.BackgroundManager
import androidx.leanback.app.BrowseSupportFragment
import androidx.lifecycle.lifecycleScope
import com.keygenqt.tvgram.R
import com.keygenqt.tvgram.base.ArrayAdapterGroup
import com.keygenqt.tvgram.data.MessageModel
import com.keygenqt.tvgram.extensions.*
import com.keygenqt.tvgram.utils.IntentHelper.getIntentPhotoActivity
import com.keygenqt.tvgram.utils.IntentHelper.getIntentSettingsActivity
import com.keygenqt.tvgram.utils.IntentHelper.getIntentTextActivity
import com.keygenqt.tvgram.utils.IntentHelper.getIntentVideoActivity
import com.keygenqt.tvgram.utils.IntentHelper.getIntentVideoNoteActivity
import dagger.hilt.android.AndroidEntryPoint
import org.drinkless.td.libcore.telegram.TdApi

/**
 * Main page app Fragment
 */
@AndroidEntryPoint
class HomeFragment : BrowseSupportFragment() {

    private val viewModel by viewModels<HomeViewModel>()

    private val backgroundManager: BackgroundManager? by lazy {
        BackgroundManager.getInstance(activity)?.apply {
            attach(requireActivity().window)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUi()
        initListener()
        adapterListener()
        viewModel.updateChats()
    }

    override fun onResume() {
        super.onResume()
        if (!viewModel.checkPrefChats()) {
            viewModel.updateChats()
        }
        setBackground(if (viewModel.homeModels.value == null) R.drawable.bg_loading else R.drawable.bg)
    }

    private fun initUi() {
        badgeDrawable = AppCompatResources.getDrawable(
            requireContext(),
            R.drawable.brand_loading
        )
        setBackground(R.drawable.bg_loading)
        // over title
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true
        // set fastLane (or headers) background color
        brandColor = ContextCompat.getColor(requireContext(), R.color.colorHomeMenu)
        // set search icon color
        searchAffordanceColor = ContextCompat.getColor(requireContext(), R.color.colorHomeSearch)
        // set adapter
        adapter = CardAdapter(this, R.layout.home_adapter_item)
    }

    private fun setBackground(@DrawableRes id: Int) {
        val bg = BitmapFactory.decodeResource(resources, id)
        val h = bg.height - windowHeight * bg.width / windowWidth
        backgroundManager?.setBitmap(Bitmap.createBitmap(bg, 0, h, bg.width, bg.height - h))
    }

    private fun adapterListener() {
        adapter.toBaseAdapter()?.setOnSelectedListener {
            if (it is MessageModel) {
                // selected item
            }
        }
        adapter.toBaseAdapter()?.setOnClickListener { item ->
            when (item) {
                is String -> {
                    when (item) {
                        requireContext().getString(R.string.home_card_settings) -> {
                            startActivity(requireContext().getIntentSettingsActivity())
                        }
                    }
                }
                is MessageModel -> {
                    when (val content = item.message.content) {
                        is TdApi.MessageText -> if (content.isUrl) {
                            toastCardUndefined()
                        } else {
                            startActivity(
                                requireContext().getIntentTextActivity(
                                    text = content.text.text,
                                )
                            )
                        }
                        is TdApi.MessagePhoto -> item.file?.id.let { id ->
                            startActivity(
                                requireContext().getIntentPhotoActivity(
                                    photoId = id,
                                    text = content.caption.text,
                                )
                            )
                        }
                        is TdApi.MessageVideoNote -> startActivity(
                            requireContext().getIntentVideoNoteActivity(
                                videoId = content.videoNote.video.id,
                                title = "VideoNote",
                                description = item.message.date.toDate()
                            )
                        )
                        is TdApi.MessageVideo -> startActivity(
                            requireContext().getIntentVideoActivity(
                                videoId = content.video.video.id,
                                title = content.video.fileName,
                                description = content.caption.text
                            )
                        )
                        else -> toastCardUndefined()
                    }
                }
            }
        }
    }

    /**
     * ViewModel listeners
     */
    private fun initListener() {
        lifecycleScope.launchWhenStarted {
            viewModel.isError.collect {
                if (it != null) {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.homeModels.collect { chats ->

                adapter.toBaseAdapter()?.setItems(mutableListOf<ArrayAdapterGroup>().apply {
                    chats?.forEach { chat ->
                        add(
                            ArrayAdapterGroup(
                                title = chat.chat.title,
                                cards = chat.messages
                            )
                        )
                    }
                })
                if (chats != null) {
                    adapter.toBaseAdapter()?.addItemGroup(
                        ArrayAdapterGroup(
                            title = resources.getString(R.string.home_menu_item_preference),
                            cards = listOf(getString(R.string.home_card_settings))
                        ),
                    )
                }

                // set state badge
                badgeDrawable = AppCompatResources.getDrawable(
                    requireContext(),
                    if (chats == null) R.drawable.brand_loading else R.drawable.brand
                )

                setBackground(if (chats == null) R.drawable.bg_loading else R.drawable.bg)
            }
        }
    }

    /**
     * Show error open card
     */
    private fun toastCardUndefined() {
        Toast.makeText(
            requireContext(),
            getString(R.string.home_toast_type_undefined),
            Toast.LENGTH_LONG
        ).show()
    }
}