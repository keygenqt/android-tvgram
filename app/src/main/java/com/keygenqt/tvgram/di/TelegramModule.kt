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
package com.keygenqt.tvgram.di

import android.content.Context
import android.os.Build
import com.keygenqt.tvgram.BuildConfig.API_HASH
import com.keygenqt.tvgram.BuildConfig.API_ID
import com.keygenqt.tvgram.base.TelegramApi
import com.keygenqt.tvgram.services.AuthRepository
import com.keygenqt.tvgram.services.ChatsRepository
import com.keygenqt.tvgram.services.CommonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import java.util.*
import javax.inject.Singleton

/**
 * Dagger Module with Retrofit
 */
@Module
@InstallIn(SingletonComponent::class)
object TelegramModule {

    @Singleton
    @Provides
    fun provideApiTelegram(
        parameters: TdApi.TdlibParameters,
    ): TelegramApi {
        return TelegramApi(
            parameters = parameters,
        )
    }

    @Singleton
    @Provides
    fun provideLibTdParameters(@ApplicationContext context: Context): TdApi.TdlibParameters {
        return TdApi.TdlibParameters().apply {
            apiId = API_ID
            apiHash = API_HASH
            useMessageDatabase = true
            useSecretChats = true
            systemLanguageCode = Locale.getDefault().language
            databaseDirectory = context.filesDir.absolutePath
            deviceModel = Build.MODEL
            systemVersion = Build.VERSION.RELEASE
            applicationVersion = "0.0.1"
            enableStorageOptimizer = true
        }
    }

    @Provides
    fun provideChatsRepository(api: TelegramApi) = ChatsRepository(api)

    @Provides
    fun provideAuthRepository(api: TelegramApi) = AuthRepository(api)

    @Provides
    fun provideCommonRepository(api: TelegramApi) = CommonRepository(api)
}
