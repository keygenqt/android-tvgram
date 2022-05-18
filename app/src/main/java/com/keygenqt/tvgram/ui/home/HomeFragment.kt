package com.keygenqt.tvgram.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import androidx.lifecycle.lifecycleScope
import com.keygenqt.tvgram.R
import com.keygenqt.tvgram.ui.temp.HomeOldFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

/**
 * Main page app Fragment
 */
@AndroidEntryPoint
class HomeFragment : BrowseSupportFragment() {

    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initListener()
        initUi()
        viewModel.updateChats()
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
            viewModel.chats.collect { chats ->
                val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
                chats.forEachIndexed { index, chat ->
                    val gridHeader = HeaderItem(index.toLong(), chat.title)
                    val mGridPresenter = GridItemPresenter()
                    val gridRowAdapter = ArrayObjectAdapter(mGridPresenter)
                    gridRowAdapter.add(resources.getString(R.string.grid_view))
                    gridRowAdapter.add(getString(R.string.error_fragment))
                    gridRowAdapter.add(resources.getString(R.string.personal_settings))
                    rowsAdapter.add(ListRow(gridHeader, gridRowAdapter))
                }
                adapter = rowsAdapter
            }
        }
    }

    private inner class GridItemPresenter : Presenter() {
        override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
            val view = TextView(parent.context)
            view.layoutParams = ViewGroup.LayoutParams(200, 200)
            view.isFocusable = true
            view.isFocusableInTouchMode = true
            view.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorBackground))
            view.setTextColor(Color.WHITE)
            view.gravity = Gravity.CENTER
            return ViewHolder(view)
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
            (viewHolder.view as TextView).text = item as String
        }

        override fun onUnbindViewHolder(viewHolder: ViewHolder) {}
    }

    private fun initUi() {
        title = getString(R.string.browse_title)
        // over title
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true
        // set fastLane (or headers) background color
        brandColor = ContextCompat.getColor(requireContext(), R.color.colorHomeMenu)
        // set search icon color
        searchAffordanceColor = ContextCompat.getColor(requireContext(), R.color.colorHomeSearch)
    }
}