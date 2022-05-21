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
package com.keygenqt.tvgram.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*

/**
 * Base adapter holder
 */
class ArrayObjectAdapterHolder(
    @LayoutRes id: Int, group: ViewGroup,
    var view: View = LayoutInflater.from(group.context).inflate(id, group, false)
) : Presenter.ViewHolder(view)

data class ArrayAdapterGroup(
    val title: String,
    val cards: List<Any>
)

abstract class BaseArrayObjectAdapter(
    val fragment: BrowseSupportFragment,
    @LayoutRes val id: Int,
    val context: Context = fragment.requireContext(),
    presenter: ListRowPresenter = ListRowPresenter()
) : ArrayObjectAdapter(presenter) {

    // cards row
    private val cardsAdapters: MutableList<ArrayObjectAdapter> = mutableListOf()

    // listeners
    private var selectedListener: (item: Any) -> Unit = {}
    private var clickListener: (item: Any) -> Unit = {}

    init {
        // set listeners
        fragment.onItemViewClickedListener = ItemGridViewClickedListener()
        fragment.onItemViewSelectedListener = ItemMenuViewSelectedListener()
        // disable shadow
        presenter.shadowEnabled = false
        presenter.selectEffectEnabled = false
    }

    abstract fun onBindView(holder: View, model: Any)

    open fun onSelected(item: Any) {}
    open fun onClick(item: Any) {}

    fun addItems(list: List<ArrayAdapterGroup>) {
        list.forEachIndexed { index, group ->
            val cardsAdapter = ArrayObjectAdapter(InnerPresenter())
            group.cards.forEach { card ->
                cardsAdapter.add(card)
            }
            add(ListRow(HeaderItem(index.toLong(), group.title), cardsAdapter))
            cardsAdapters.add(cardsAdapter)
        }
    }

    fun setItems(list: List<ArrayAdapterGroup>) {
        clear()
        addItems(list)
    }

    fun addItemGroup(item: ArrayAdapterGroup) {
        addItems(listOf(item))
    }

    fun setItemGroup(item: ArrayAdapterGroup) {
        setItems(listOf(item))
    }

    fun addItemCard(index: Int, item: Any) {
        if (index >= 0 && cardsAdapters.size < index) {
            cardsAdapters[index].add(item)
        }
    }

    fun setOnSelectedListener(listener: (Any) -> Unit) {
        selectedListener = listener
    }

    fun setOnClickListener(listener: (Any) -> Unit) {
        clickListener = listener
    }

    inner class ItemGridViewClickedListener : OnItemViewClickedListener {
        override fun onItemClicked(
            itemViewHolder: Presenter.ViewHolder,
            item: Any?,
            rowViewHolder: RowPresenter.ViewHolder,
            row: Row
        ) {
            if (item != null) {
                clickListener.invoke(item)
                onClick(item)
            }
        }
    }

    inner class ItemMenuViewSelectedListener : OnItemViewSelectedListener {
        override fun onItemSelected(
            itemViewHolder: Presenter.ViewHolder?,
            item: Any?,
            rowViewHolder: RowPresenter.ViewHolder,
            row: Row
        ) {
            if (item != null) {
                selectedListener.invoke(item)
                onSelected(item)
            }
        }
    }

    inner class InnerPresenter : Presenter() {
        override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
            return ArrayObjectAdapterHolder(id, parent)
        }

        override fun onBindViewHolder(viewHolder: ViewHolder?, item: Any?) {
            if (viewHolder != null && item != null) {
                onBindView(viewHolder.view, item)
            }
        }

        override fun onUnbindViewHolder(viewHolder: ViewHolder?) {

        }
    }
}

///**
// * Base card presenter
// */
//abstract class BaseCardPresenter(
//    val fragment: BrowseSupportFragment,
//    val context: Context = fragment.requireContext()
//) : Presenter() {
//
//    private var adapter = fragment.adapter as? ArrayObjectAdapter
//    private var items = mutableListOf<Any>()
//
//    init {
//        fragment.onItemViewClickedListener = ItemGridViewClickedListener()
//        fragment.onItemViewSelectedListener = ItemMenuViewSelectedListener()
//    }
//
//    abstract fun onBindCardView(cardView: ImageCardView, model: Any)
//    abstract fun onSelected(model: Any)
//    abstract fun onClick(model: Any)
//
//    override fun onCreateViewHolder(parent: ViewGroup?): ViewHolder {
//        val cardView = object : ImageCardView(context) {}
//        cardView.isFocusable = true
//        cardView.isFocusableInTouchMode = true
//        return ViewHolder(cardView)
//    }
//
//    override fun onBindViewHolder(viewHolder: ViewHolder, model: Any) {
//        val cardView = viewHolder.view as ImageCardView
//        onBindCardView(cardView, model)
//    }
//
//    override fun onUnbindViewHolder(viewHolder: ViewHolder?) {
//    }
//
//    fun setItems(list: List<Any>) {
//        items.clear()
//        items.addAll(list)
//        adapter?.notifyItemRangeChanged()
//
//        (this as ArrayObjectAdapter).notifyArrayItemRangeChanged(1, 40)
//    }
//
//    /**
//     * Listener for click grid item
//     */
//    inner class ItemGridViewClickedListener : OnItemViewClickedListener {
//        override fun onItemClicked(
//            itemViewHolder: ViewHolder,
//            item: Any?,
//            rowViewHolder: RowPresenter.ViewHolder,
//            row: Row
//        ) {
//            if (item != null) {
//                onClick(item)
//            }
//        }
//    }
//
//    /**
//     * Listener for click menu item
//     */
//    inner class ItemMenuViewSelectedListener : OnItemViewSelectedListener {
//        override fun onItemSelected(
//            itemViewHolder: ViewHolder?,
//            item: Any?,
//            rowViewHolder: RowPresenter.ViewHolder,
//            row: Row
//        ) {
//            if (item != null) {
//                onSelected(item)
//            }
//        }
//    }
//}