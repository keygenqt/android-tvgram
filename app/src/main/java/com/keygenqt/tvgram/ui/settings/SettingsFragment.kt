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
package com.keygenqt.tvgram.ui.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.keygenqt.tvgram.R
import com.keygenqt.tvgram.base.BaseFragment
import com.keygenqt.tvgram.data.SettingModel
import com.keygenqt.tvgram.databinding.SettingsFragmentBinding
import com.keygenqt.tvgram.extensions.baseAdapter
import com.keygenqt.tvgram.utils.IntentHelper.getIntentAuthActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * First step auth Fragment
 */
@AndroidEntryPoint
class SettingsFragment : BaseFragment<SettingsFragmentBinding>() {

    private val viewModel by viewModels<SettingsViewModel>()

    override fun onCreateBinding(i: LayoutInflater, v: ViewGroup?) =
        SettingsFragmentBinding.inflate(i, v, false)

    override fun onCreateView(binding: SettingsFragmentBinding): View {
        binding.initUi()
        binding.initListener()
        return binding.root
    }

    private fun SettingsFragmentBinding.initListener() {
        lifecycleScope.launchWhenStarted {
            viewModel.isLoading.collect {
                loader.visibility = if (it) View.VISIBLE else View.GONE
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.isError.collect {
                if (it != null) {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.isSuccess.collect {
                if (it == true) {
                    startActivity(requireContext().getIntentAuthActivity())
                }
            }
        }
    }

    private fun SettingsFragmentBinding.initUi() {
        // init recycler view
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = SettingsAdapter(R.layout.settings_adapter_item)
        // set params
        recyclerView.baseAdapter.updateItems(
            listOf(
                SettingModel(
                    switch = viewModel.isChatSecret,
                    title = getString(R.string.settings_item_secret),
                    focus = {
                        settingsSubtitle.text = getString(R.string.settings_subtitle_secret)
                    },
                    click = {
                        viewModel.isChatSecret = it
                    }
                ),
                SettingModel(
                    switch = viewModel.isChatPrivate,
                    title = getString(R.string.settings_item_private),
                    focus = {
                        settingsSubtitle.text = getString(R.string.settings_subtitle_private)
                    },
                    click = {
                        viewModel.isChatPrivate = it
                    }
                ),
                SettingModel(
                    switch = viewModel.isChatSupergroup,
                    title = getString(R.string.settings_item_supergroup),
                    focus = {
                        settingsSubtitle.text = getString(R.string.settings_subtitle_supergroup)
                    },
                    click = {
                        viewModel.isChatSupergroup = it
                    }
                ),
                SettingModel(
                    switch = null,
                    title = getString(R.string.settings_item_cache),
                    focus = {
                        settingsSubtitle.text = getString(R.string.settings_subtitle_cache)
                    },
                    click = {
                        viewModel.clearCache()
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.settings_clear_success),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                ),
                SettingModel(
                    switch = null,
                    title = getString(R.string.settings_item_logout),
                    focus = {
                        settingsSubtitle.text = ""
                    },
                    click = {
                        viewModel.logout()
                    }
                ),
            )
        )
    }
}