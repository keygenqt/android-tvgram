package com.keygenqt.tvgram.ui.auth

import android.os.Bundle
import android.view.View

import androidx.core.content.ContextCompat
import androidx.leanback.app.ErrorSupportFragment
import com.keygenqt.tvgram.R

/**
 * This class demonstrates how to extend [ErrorSupportFragment].
 */
class PhoneAuthFragment : ErrorSupportFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = resources.getString(R.string.app_name)
    }
}