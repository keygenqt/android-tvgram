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
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.leanback.app.BackgroundManager
import androidx.leanback.app.BrowseSupportFragment
import androidx.lifecycle.lifecycleScope
import com.keygenqt.tvgram.R
import com.keygenqt.tvgram.base.ArrayAdapterGroup
import com.keygenqt.tvgram.extensions.toBaseAdapter
import com.keygenqt.tvgram.extensions.windowHeight
import com.keygenqt.tvgram.extensions.windowWidth
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main page app Fragment
 */
@AndroidEntryPoint
class HomeFragment : BrowseSupportFragment() {

    private var updateChat = false
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
        setBackground()
        viewModel.updateChats()
    }

    override fun onResume() {
        super.onResume()
        if (updateChat) {
            viewModel.updateChats()
        }
        setBackground()
    }

    private fun initUi() {
        badgeDrawable = AppCompatResources.getDrawable(
            requireContext(),
            R.drawable.brand_loading
        )

        // over title
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true
        // set fastLane (or headers) background color
        brandColor = ContextCompat.getColor(requireContext(), R.color.colorHomeMenu)
        // set search icon color
        searchAffordanceColor = ContextCompat.getColor(requireContext(), R.color.colorHomeSearch)
        // set adapter
//        adapter = ArrayObjectAdapter(ListRowPresenter())
        adapter = CardAdapter(this, R.layout.home_adapter_item)
//        adapter.toBaseAdapter()?.setItems(
//            listOf(
//                ArrayAdapterGroup(
//                    title = "TEST 1",
//                    cards = listOf(
//                        "CARD TEST 1 1",
//                        "CARD TEST 1 2",
//                        "CARD TEST 1 3",
//                        "CARD TEST 1 4",
//                    )
//                ),
//            )
//        )
//
//        adapter.toBaseAdapter()?.addItemGroup(
//            ArrayAdapterGroup(
//                title = "TEST 2",
//                cards = listOf(
//                    "CARD TEST 2 1",
//                    "CARD TEST 2 2",
//                    "CARD TEST 2 3",
//                    "CARD TEST 2 4",
//                )
//            ),
//        )
//
//        adapter.toBaseAdapter()?.addItemCard(1, "CARD TEST 2 5")

        // set click listener
//        onItemViewClickedListener = ItemGridViewClickedListener()
//        onItemViewSelectedListener = ItemMenuViewSelectedListener()
    }

    private fun setBackground() {
        val bg = BitmapFactory.decodeResource(resources, R.drawable.bg)
        val h = bg.height - windowHeight * bg.width / windowWidth
        backgroundManager?.setBitmap(Bitmap.createBitmap(bg, 0, h, bg.width, bg.height - h))
    }

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
                adapter.toBaseAdapter()?.addItemGroup(
                    ArrayAdapterGroup(
                        title = resources.getString(R.string.home_card_settings),
                        cards = listOf(getString(R.string.home_menu_item_preference))
                    ),
                )


//
//                val rowsAdapter = adapter as ArrayObjectAdapter
//                val cardPresenter = CardAdapter(this@HomeFragment)
//
//                // set state badge
//                badgeDrawable = AppCompatResources.getDrawable(
//                    requireContext(),
//                    if (chats == null) R.drawable.brand_loading else R.drawable.brand
//                )
//
//                // clear adapter data
//                rowsAdapter.clear()
//
//                // set data messages
//                chats?.forEachIndexed { index, chatModel ->
//                    val listRowAdapter = ArrayObjectAdapter(cardPresenter)
//                    chatModel.messages.forEachIndexed { index2, messageModel ->
//                        listRowAdapter.add(messageModel)
//                        if (chatModel.messages.size - 1 == index2) {
//                            listRowAdapter.add(messageModel.message.id)
//                        }
//                    }
//                    val header = HeaderItem(index.toLong(), chatModel.chat.title)
//                    rowsAdapter.add(ListRow(header, listRowAdapter))
//                }
//
//                // set preference
//                if (chats != null) {
//                    ArrayObjectAdapter(SettingsItemPresenter(requireContext())).apply {
//                        add(resources.getString(R.string.home_card_settings))
//                        rowsAdapter.add(
//                            ListRow(
//                                HeaderItem(
//                                    chats.size.toLong(),
//                                    getString(R.string.home_menu_item_preference)
//                                ), this
//                            )
//                        )
//                    }
//                }
            }
        }
    }

//    /**
//     * Listener for click grid item
//     */
//    inner class ItemGridViewClickedListener : OnItemViewClickedListener {
//        override fun onItemClicked(
//            itemViewHolder: Presenter.ViewHolder,
//            item: Any,
//            rowViewHolder: RowPresenter.ViewHolder,
//            row: Row
//        ) {
//            when (item) {
//                is Long -> {
//                    (itemViewHolder.view as ImageCardView).mainImageView.setImageDrawable(
//                        AppCompatResources.getDrawable(
//                            requireContext(),
//                            R.drawable.card_loading_background
//                        )
//                    )
//                    viewModel.updateRow(item) {
//                        synchronized(adapter) {
//                            (adapter as ArrayObjectAdapter).notifyArrayItemRangeChanged(1, 40)
//                        }
//                    }
//                }
//                is String -> {
//                    when (item) {
//                        requireContext().getString(R.string.home_card_settings) -> {
//                            updateChat = true
//                            startActivity(Intent(requireContext(), SettingsActivity::class.java))
//                        }
//                    }
//                }
//                is MessageModel -> {
//                    when (val content = item.message.content) {
//                        is TdApi.MessageText -> {
//                            if (content.text.text.isNullOrBlank()
//                                || Patterns.WEB_URL.matcher(content.text.text ?: "").matches()
//                            ) {
//                                Toast.makeText(
//                                    requireContext(),
//                                    getString(R.string.home_toast_type_undefined),
//                                    Toast.LENGTH_LONG
//                                ).show()
//                            } else {
//                                startActivity(
//                                    Intent(
//                                        requireContext(),
//                                        TextActivity::class.java
//                                    ).apply {
//                                        putExtra("text", content.text.text)
//                                    })
//                            }
//                        }
//                        is TdApi.MessagePhoto -> {
//                            startActivity(
//                                Intent(
//                                    requireContext(),
//                                    PhotoActivity::class.java
//                                ).apply {
//                                    putExtra("photo", item.file?.local?.path)
//                                    putExtra("text", content.caption.text)
//                                })
//                        }
//                        is TdApi.MessageVideoNote -> {
//                            startActivity(
//                                Intent(
//                                    requireContext(),
//                                    VideoActivity::class.java
//                                ).apply {
//                                    putExtra("isNote", true)
//                                    putExtra("videoId", content.videoNote.video.id)
//                                    putExtra("title", "VideoNote")
//                                    putExtra("description", content.videoNote.video.id)
//                                })
//                        }
//                        is TdApi.MessageVideo -> {
//                            startActivity(
//                                Intent(
//                                    requireContext(),
//                                    VideoActivity::class.java
//                                ).apply {
//                                    putExtra("isNote", false)
//                                    putExtra("videoId", content.video.video.id)
//                                    putExtra("title", content.video.fileName)
//                                    putExtra("description", content.caption.text)
//                                })
//                        }
//                        else -> {
//                            Toast.makeText(
//                                requireContext(),
//                                getString(R.string.home_toast_type_undefined),
//                                Toast.LENGTH_LONG
//                            ).show()
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    /**
//     * Listener for click menu item
//     */
//    inner class ItemMenuViewSelectedListener : OnItemViewSelectedListener {
//        override fun onItemSelected(
//            itemViewHolder: Presenter.ViewHolder?,
//            item: Any?,
//            rowViewHolder: RowPresenter.ViewHolder,
//            row: Row
//        ) {
//            if (item != null) {
//                if (item is ChatModel) {
//                    Timber.e(item.chat.title)
//                }
//            }
//        }
//    }
}