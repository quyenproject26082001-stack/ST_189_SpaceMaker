package com.oc.christmas.ocmaker.create.avatar.data.model.custom

import com.oc.christmas.ocmaker.create.avatar.data.model.custom.ColorModel

data class LayerModel(
    val image: String,
    val isMoreColors: Boolean = false,
    var listColor: ArrayList<ColorModel> = arrayListOf()
)