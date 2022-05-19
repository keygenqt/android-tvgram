package com.keygenqt.tvgram.base

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

/**
 * Base adapter holder
 */
class AdapterHolder(
    @LayoutRes id: Int, group: ViewGroup,
    var view: View = LayoutInflater.from(group.context).inflate(id, group, false)
) : RecyclerView.ViewHolder(view)

/**
 * Base adapter
 */
abstract class BaseAdapter(
    @LayoutRes val id: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items = mutableListOf<Any>()

    abstract fun onBindViewHolder(holder: View, model: Any, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AdapterHolder(id, parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AdapterHolder -> onBindViewHolder(holder.view, items[position], position)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("NotifyDataSetChanged")
    open fun updateItems(items: List<Any>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun updateItem(index: Int, item: Any) {
        if (index < items.size) {
            items[index] = item
            notifyItemChanged(index)
        }
    }

    fun removeItem(index: Int) {
        if (index < items.size) {
            items.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}