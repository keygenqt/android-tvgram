package com.keygenqt.tvgram

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import com.keygenqt.tvgram.ui.home.HomeFragment
import com.keygenqt.tvgram.ui.other.MainFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Loads [HomeFragment].
 */
@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (findViewById<View>(android.R.id.content)).let { content ->
            content.viewTreeObserver.addOnPreDrawListener(
                object : ViewTreeObserver.OnPreDrawListener {
                    override fun onPreDraw(): Boolean {
                        val isLogin = viewModel.isLogin.value
                        return if (isLogin != null) {
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.main_browse_fragment, MainFragment(isLogin))
                                .commitNow()
                            content.viewTreeObserver.removeOnPreDrawListener(this)
                            true
                        } else {
                            false
                        }
                    }
                }
            )
        }
    }
}