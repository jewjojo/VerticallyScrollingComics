package com.umdproject.verticallyscrollingcomics.viewModels

import android.graphics.Bitmap
import android.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

// This ViewModel holds the data for the comic currently being read
class CurrentComicViewModel : ViewModel() {
    private val _backgroundColor = MutableLiveData<Color>()
    internal val backgroundColor: LiveData<Color>
        get() = _backgroundColor

    private val _scrollSpeed = MutableLiveData<Float>() // 0 == slowest, 1 == fastest
    internal val scrollSpeed: LiveData<Float>
        get() = _scrollSpeed

    private val _panelOrder = MutableLiveData<MutableList<Int>>() // List of page numbers-- maybe switch to list of images containing the panels?
    internal val panelOrder: LiveData<MutableList<Int>>
        get() = _panelOrder

    private val _panels = MutableLiveData<MutableList<Bitmap>>() // Hashmap of panels indexed by page number
    internal val panels: LiveData<MutableList<Bitmap>>
        get() = _panels

    private val _panelSpacing = MutableLiveData<Int>()
    internal val panelSpacing: LiveData<Int>
        get() = _panelSpacing


    private val _commentsList = MutableLiveData<Int>()
    internal val commentsList: LiveData<Int>
        get() = _commentsList

    private val _title = MutableLiveData<String>()
    internal val title: LiveData<String>
        get() = _title

    private val _author = MutableLiveData<String>()
    internal val author: LiveData<String>
        get() = _author

    fun setTitle(newTitle: String) {
        _title.value = newTitle
    }

    fun setAuthor(newAuthor: String) {
        _author.value = newAuthor
    }


    fun setPanels(newPanels: MutableList<Bitmap>) {
        _panels.value = newPanels
    }

    fun setBackgroundColor(newColor: Color) {
        _backgroundColor.value = newColor
    }

    fun setScrollSpeed(newSpeed: Float) {
        _scrollSpeed.value = newSpeed
    }

    fun setPanelOrder(newPanelOrder: MutableList<Int>) {
        _panelOrder.value = newPanelOrder
    }

    fun newPanelSpacing(newSpacing: Int) {
        _panelSpacing.value = newSpacing
    }

    init {
        _backgroundColor.value = Color.valueOf(0F,0F,0F)
        _scrollSpeed.value = 0.5.toFloat()
        _panelOrder.value = mutableListOf()
        _panelSpacing.value = 1
        _panels.value = mutableListOf()
    }
}