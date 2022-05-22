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

import android.view.View
import androidx.annotation.LayoutRes
import com.keygenqt.tvgram.base.BaseAdapter
import com.keygenqt.tvgram.data.SettingModel
import com.keygenqt.tvgram.databinding.SettingsAdapterItemBinding

/**
 * Adapter settings
 */
class SettingsAdapter(@LayoutRes layout: Int) : BaseAdapter(layout) {
    override fun onBindViewHolder(holder: View, model: Any, position: Int) {
        if (model is SettingModel) {
            SettingsAdapterItemBinding.bind(holder).apply {

                model.switch?.let { state ->
                    switchItem.isChecked = state
                }

                switchItem.visibility =
                    if (model.switch != null) View.VISIBLE else View.GONE

                settingsTitle.visibility =
                    if (position == 0) View.VISIBLE else if (position == 4) View.INVISIBLE else View.GONE

                tvSettingsItem.text = model.title

                bodySettingsItem.setOnClickListener {
                    switchItem.isChecked = !switchItem.isChecked
                    model.switch = switchItem.isChecked
                    model.click.invoke(switchItem.isChecked)
                }

                bodySettingsItem.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        model.focus.invoke()
                    }
                }
            }
        }
    }
}