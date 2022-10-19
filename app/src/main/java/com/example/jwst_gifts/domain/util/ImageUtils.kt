package com.example.jwst_gifts.util

import android.widget.ImageView
import androidx.annotation.DrawableRes
import coil.load
import coil.transform.RoundedCornersTransformation

fun ImageView.loadImageUrl(
    imageUrl: String,
    @DrawableRes defaultImageResId: Int,
    crossFadeEnabled: Boolean = true
) {
    load(imageUrl) {
        crossfade(crossFadeEnabled)
        placeholder(defaultImageResId)
        transformations(RoundedCornersTransformation(radius = 16f))
    }
}