package com.keygenqt.tvgram.ui.other

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.leanback.app.BackgroundFragment
import androidx.leanback.app.BaseSupportFragment
import androidx.leanback.app.BrandedSupportFragment
import com.keygenqt.tvgram.R
import com.keygenqt.tvgram.ui.auth.PhoneAuthFragment
import com.keygenqt.tvgram.ui.home.HomeFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Loads a grid of cards with movies to browse.
 */
@AndroidEntryPoint
class MainFragment(
    private val isLogin: Boolean
) : BrandedSupportFragment() {

    private lateinit var homeFragment: HomeFragment
    private lateinit var phoneAuthFragment: PhoneAuthFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            if (isLogin) {
                homeFragment = HomeFragment()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.main_browse_fragment, homeFragment)
                    .commitNow()
            } else {
                phoneAuthFragment = PhoneAuthFragment()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.main_browse_fragment, phoneAuthFragment)
                    .commitNow()
            }
        }
    }
}