package com.oc.christmas.ocmaker.create.avatar.data.model.custom

data class LayerListModel(
    var positionCustom: Int = 0,
    var positionNavigation: Int = 0,
    var imageNavigation: String = "",
    var layer: ArrayList<LayerModel> = arrayListOf(),
)
