package com.umdproject.verticallyscrollingcomics.dataClasses

import android.graphics.Bitmap

class ComicPanel(imageIn: Bitmap) {
    var image: Bitmap
    var hasHaptics: Boolean
    init {
        image = imageIn
        hasHaptics = false
    }
}