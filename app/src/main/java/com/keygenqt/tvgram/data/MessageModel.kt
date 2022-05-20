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
package com.keygenqt.tvgram.data

import android.graphics.Bitmap
import org.drinkless.td.libcore.telegram.TdApi

/**
 * Model menu settings
 *
 * @property message [TdApi.Message]
 * @property file [TdApi.File]
 * @property drawable [Bitmap]
 */
data class MessageModel(
    val message: TdApi.Message,
    var file: TdApi.File? = null,
    var drawable: Bitmap? = null,
)