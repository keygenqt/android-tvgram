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
package com.keygenqt.tvgram.ui.photo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.keygenqt.tvgram.R
import dagger.hilt.android.AndroidEntryPoint

/**
 * Photo Activity with material
 */
@AndroidEntryPoint
class PhotoActivity : AppCompatActivity() {

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)

        setContentView(R.layout.main_activity)

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.main_browse_fragment, PhotoFragment(
                photo = intent.getStringExtra("photo"),
                text = intent.getStringExtra("text")
            ))
        }
    }
}