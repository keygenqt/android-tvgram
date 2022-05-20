package com.keygenqt.tvgram.extensions

import android.content.Context
import android.graphics.Bitmap
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation

/**
 * Loading image
 */
fun Context.loading(path: String, round: Boolean, loading: (Bitmap) -> Unit) {
    ImageLoader(this).enqueue(ImageRequest.Builder(this)
        .data(path)
        .apply { if (round) transformations(CircleCropTransformation()) }
        .allowHardware(false)
        .target { loading.invoke(it.toBitmap()) }
        .build())
}