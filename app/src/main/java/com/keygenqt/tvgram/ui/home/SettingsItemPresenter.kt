package com.keygenqt.tvgram.ui.home

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.leanback.widget.Presenter
import com.keygenqt.tvgram.R

/**
 * Presenter for custom items
 */
class SettingsItemPresenter(val context: Context) : Presenter() {

    companion object {
        const val SIZE_WITH = 200
        const val SIZE_HEIGHT = 200
    }

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val view = TextView(parent.context)
        view.layoutParams = ViewGroup.LayoutParams(SIZE_WITH, SIZE_HEIGHT)
        view.isFocusable = true
        view.isFocusableInTouchMode = true
        view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorSettingsItemBg))
        view.setTextColor(Color.WHITE)
        view.gravity = Gravity.CENTER
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        (viewHolder.view as TextView).apply {
            text = item as String
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        }
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {}
}