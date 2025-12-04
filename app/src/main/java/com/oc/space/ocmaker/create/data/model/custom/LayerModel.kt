package com.oc.space.ocmaker.create.data.model.custom

import com.oc.space.ocmaker.create.data.model.custom.ColorModel

data class LayerModel(
    val image: String,
    val isMoreColors: Boolean = false,
    var listColor: ArrayList<ColorModel> = arrayListOf()
)