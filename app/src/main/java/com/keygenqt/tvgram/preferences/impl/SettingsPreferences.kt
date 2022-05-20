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
package com.keygenqt.tvgram.preferences.impl

import android.content.SharedPreferences
import com.keygenqt.tvgram.interfaces.IAppPreferences

/**
 * [SharedPreferences] service for filter chat types
 */
interface SettingsPreferences : IAppPreferences {

    /**
     * Store private, primitive data in key-value pairs [SharedPreferences]
     */
    override val p: SharedPreferences

    /**
     * We put the keys in enum
     */
    enum class KEYS {
        CHAT_SECRET,
        CHAT_PRIVATE,
        CHAT_SUPERGROUP,
    }

    /**
     * Performed when the user logs out
     */
    override fun clearCacheAfterLogout() {
        isChatSecret = false
        isChatPrivate = false
        isChatSupergroup = false
    }

    /**
     * Filter chat type Secret
     */
    var isChatSecret: Boolean
        get() = p.getBoolean(KEYS.CHAT_SECRET.name, false)
        set(value) = p.edit().putBoolean(KEYS.CHAT_SECRET.name, value).apply()

    /**
     * Filter chat type Private
     */
    var isChatPrivate: Boolean
        get() = p.getBoolean(KEYS.CHAT_PRIVATE.name, false)
        set(value) = p.edit().putBoolean(KEYS.CHAT_PRIVATE.name, value).apply()

    /**
     * Filter chat type Supergroup. Default - true
     */
    var isChatSupergroup: Boolean
        get() = p.getBoolean(KEYS.CHAT_SUPERGROUP.name, true)
        set(value) = p.edit().putBoolean(KEYS.CHAT_SUPERGROUP.name, value).apply()
}
