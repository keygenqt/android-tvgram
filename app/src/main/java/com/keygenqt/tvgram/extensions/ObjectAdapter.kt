package com.keygenqt.tvgram.extensions

import androidx.leanback.widget.ObjectAdapter
import com.keygenqt.tvgram.base.BaseArrayObjectAdapter

/**
 * Convert adapter
 */
fun ObjectAdapter.toBaseAdapter() = this as? BaseArrayObjectAdapter