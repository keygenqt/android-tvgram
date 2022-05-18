package com.keygenqt.tvgram.ui.other

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.keygenqt.tvgram.BuildConfig
import com.keygenqt.tvgram.R
import com.keygenqt.tvgram.base.BaseFragment
import com.keygenqt.tvgram.databinding.WelcomeFragmentBinding
import com.keygenqt.tvgram.ui.auth.AuthActivity
import com.keygenqt.tvgram.ui.home.HomeFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Welcome Fragment
 */
@AndroidEntryPoint
class WelcomeFragment : BaseFragment<WelcomeFragmentBinding>() {

    private val viewModel by viewModels<WelcomeViewModel>()

    override fun onCreateBinding(i: LayoutInflater, v: ViewGroup?) =
        WelcomeFragmentBinding.inflate(i, v, false)

    override fun onCreateView(binding: WelcomeFragmentBinding): View {

        with(binding) {
            tvVersion.text = getString(R.string.welcome_version, BuildConfig.VERSION_NAME)
        }

        viewModel.startAuthentication()

        lifecycleScope.launchWhenStarted {
            viewModel.isLogin.collect {
                when (it) {
                    false -> startActivity(Intent(requireContext(), AuthActivity::class.java))
                    true -> fragmentTransaction.commit {
                        setReorderingAllowed(true)
                        replace(R.id.main_browse_fragment, HomeFragment())
                    }
                    else -> {}
                }
            }
        }

        return binding.root
    }
}