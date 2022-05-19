package com.keygenqt.tvgram.extensions

import androidx.recyclerview.widget.RecyclerView
import com.keygenqt.tvgram.base.BaseAdapter

/**
 * Get [BaseAdapter]
 */
inline val RecyclerView.baseAdapter: BaseAdapter get() = adapter as BaseAdapter