package com.keygenqt.tvgram.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.keygenqt.tvgram.R
import dagger.hilt.android.AndroidEntryPoint

/**
 * Auth Activity with material
 */
@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.main_browse_fragment, AuthFragment())
        }
    }
}