package com.keygenqt.tvgram.data

/**
 * Model menu settings
 *
 * @property title Title item
 * @property switch if null hide, set state switch
 * @property focus listen focus
 * @property click listen click
 */
data class SettingModel(
    val title: String,
    var switch: Boolean?,
    val focus: () -> Unit,
    val click: (Boolean) -> Unit,
)