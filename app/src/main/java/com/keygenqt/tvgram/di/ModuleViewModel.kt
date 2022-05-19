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
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.keygenqt.tvgram.preferences.BasePreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * Module Dagger responsible for api services
 */
@Module
@InstallIn(ViewModelComponent::class)
object ModuleViewModel {
    /**
     * Shared preferences
     */
    @Provides
    @ViewModelScoped
    fun provideSharedPreferences(@ApplicationContext context: Context): BasePreferences {
        return BasePreferences(
            EncryptedSharedPreferences.create(
                BasePreferences::class.java.simpleName,
                MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        )
    }
}
