package com.umdproject.verticallyscrollingcomics.dataClasses

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
    val ratingAverage: Int = 0
)