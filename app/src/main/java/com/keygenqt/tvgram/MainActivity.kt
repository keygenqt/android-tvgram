package com.keygenqt.tvgram

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import com.keygenqt.tvgram.ui.temp.HomeOldFragment
import com.keygenqt.tvgram.ui.other.WelcomeFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Loads [HomeOldFragment].
 */
@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.main_browse_fragment, WelcomeFragment())
        }
    }
}