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

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.leanback.app.BackgroundManager
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import androidx.lifecycle.lifecycleScope
import com.keygenqt.tvgram.R
import com.keygenqt.tvgram.data.HomeModel
import com.keygenqt.tvgram.extensions.windowHeight
import com.keygenqt.tvgram.extensions.windowWidth
import com.keygenqt.tvgram.ui.photo.PhotoActivity
import com.keygenqt.tvgram.ui.settings.SettingsActivity
import com.keygenqt.tvgram.ui.text.TextActivity
import dagger.hilt.android.AndroidEntryPoint
import org.drinkless.td.libcore.telegram.TdApi
import timber.log.Timber

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

    private fun initListener() {
        lifecycleScope.launchWhenStarted {
            viewModel.isError.collect {
                if (it != null) {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.homeModels.collect { models ->
                (adapter as ArrayObjectAdapter).let { adapter ->
                    adapter.clear()
                    models.forEachIndexed { index, model ->
                        ArrayObjectAdapter(PostItemPresenter(requireContext())).apply {
                            if (model.chat.lastMessage != null) {
                                add(model)
                                adapter.add(
                                    ListRow(
                                        HeaderItem(
                                            index.toLong(),
                                            model.chat.title
                                        ), this
                                    )
                                )
                            }
                        }
                    }

                    ArrayObjectAdapter(SettingsItemPresenter(requireContext())).apply {
                        add(resources.getString(R.string.home_card_settings))
                        adapter.add(
                            ListRow(
                                HeaderItem(
                                    models.size.toLong(),
                                    getString(R.string.home_menu_item_preference)
                                ), this
                            )
                        )
                    }
                }

            }
        }
    }

    private fun initUi() {
        title = getString(R.string.app_title)
        // over title
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true
        // set fastLane (or headers) background color
        brandColor = ContextCompat.getColor(requireContext(), R.color.colorHomeMenu)
        // set search icon color
        searchAffordanceColor = ContextCompat.getColor(requireContext(), R.color.colorHomeSearch)
        // set adapter
        adapter = ArrayObjectAdapter(ListRowPresenter())
        // set click listener
        onItemViewClickedListener = ItemGridViewClickedListener()
        onItemViewSelectedListener = ItemMenuViewSelectedListener()
    }

    private fun setBackground() {
        val bg = BitmapFactory.decodeResource(resources, R.drawable.bg)
        val h = bg.height - windowHeight * bg.width / windowWidth
        backgroundManager?.setBitmap(Bitmap.createBitmap(bg, 0, h, bg.width, bg.height - h))
    }

    /**
     * Listener for click grid item
     */
    inner class ItemGridViewClickedListener : OnItemViewClickedListener {
        override fun onItemClicked(
            itemViewHolder: Presenter.ViewHolder,
            item: Any,
            rowViewHolder: RowPresenter.ViewHolder,
            row: Row
        ) {
            if (item is String) {
                when (item) {
                    requireContext().getString(R.string.home_card_settings) -> {
                        updateChat = true
                        startActivity(Intent(requireContext(), SettingsActivity::class.java))
                    }
                }
            } else if (item is HomeModel) {
                when (val content = item.message.content) {
                    is TdApi.MessageText -> {
                        if (content.text.text.isNullOrBlank()
                            || Patterns.WEB_URL.matcher(content.text.text ?: "").matches()
                        ) {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.home_toast_type_undefined),
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            startActivity(Intent(requireContext(), TextActivity::class.java).apply {
                                putExtra("text", content.text.text)
                            })
                        }
                    }
                    is TdApi.MessagePhoto -> {
                        startActivity(Intent(requireContext(), PhotoActivity::class.java).apply {
                            putExtra("photo", item.fileImage?.local?.path)
                            putExtra("text", content.caption.text)
                        })
                    }
                    is TdApi.MessageVideoNote -> {
                        Timber.e("open video")
                    }
                    is TdApi.MessageVideo -> {
                        Timber.e("open video")
                    }
                    else -> {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.home_toast_type_undefined),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    /**
     * Listener for click menu item
     */
    inner class ItemMenuViewSelectedListener : OnItemViewSelectedListener {
        override fun onItemSelected(
            itemViewHolder: Presenter.ViewHolder?,
            item: Any?,
            rowViewHolder: RowPresenter.ViewHolder,
            row: Row
        ) {

        }
    }
}