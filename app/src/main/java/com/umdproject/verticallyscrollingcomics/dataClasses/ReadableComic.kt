package com.umdproject.verticallyscrollingcomics.dataClasses

import android.graphics.Color

data class ReadableComic (
    val id: String = "",
    val comicName: String = "",
    val comicAuthorName: String = "",
    val comicAuthorId: String = "",
    val comicId: String = "",
    val panelCount: Int = 0,
    val thumbnailResId: String = "",
    val views: Int = 0,
    val ratingCount: Int = 0,
    val ratingAverage: Int = 0,
    val backgroundColor: Color = Color.valueOf(0F,0F,0F),
    val scrollSpeed: Float = 0.5.toFloat(),
    val panelOrder: MutableList<Int> = mutableListOf(),
    val panelSpacing: Int = 1,
    val panels: MutableList<ComicPanel> = mutableListOf(),
    val commentList: MutableList<String>,
)