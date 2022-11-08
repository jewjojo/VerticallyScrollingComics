package com.umdproject.verticallyscrollingcomics.viewModels

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

    private val _panelSpacing = MutableLiveData<Int>()
    internal val panelSpacing: LiveData<Int>
        get() = _panelSpacing

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
        _backgroundColor.value = Color.valueOf(0xffffffff)
        _scrollSpeed.value = 0.5.toFloat()
        _panelOrder.value = mutableListOf()
        _panelSpacing.value = 1
    }
}