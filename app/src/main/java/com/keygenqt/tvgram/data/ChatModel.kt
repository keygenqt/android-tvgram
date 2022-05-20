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

import org.drinkless.td.libcore.telegram.TdApi

/**
 * Model menu settings
 *
 * @property chat [TdApi.Chat]
 * @property messages [MessageModel]
 */
data class ChatModel(
    val chat: TdApi.Chat,
    var totalCountMessages: Int = 0,
    var messages: List<MessageModel> = listOf(),
)