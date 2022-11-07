package com.umdproject.verticallyscrollingcomics.ui.fragments

import android.graphics.Bitmap

class LocalComicPreview(coverIn: Bitmap, filePathIn: String, authorIn: String, titleIn: String) {
    var cover: Bitmap
    var filePath: String
    var author: String
    var title: String

    init {
        cover = Bitmap.createScaledBitmap(coverIn, 300, 500, true)
        filePath = filePathIn
        author = authorIn
        title = titleIn
    }
}