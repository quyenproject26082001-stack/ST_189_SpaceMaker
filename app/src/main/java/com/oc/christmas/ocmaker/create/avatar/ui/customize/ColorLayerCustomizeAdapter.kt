package com.oc.christmas.ocmaker.create.avatar.ui.customize

import android.content.Context
import androidx.core.graphics.toColorInt
import androidx.core.view.isVisible
import com.oc.christmas.ocmaker.create.avatar.core.base.BaseAdapter
import com.oc.christmas.ocmaker.create.avatar.core.extensions.tap
import com.oc.christmas.ocmaker.create.avatar.data.model.custom.ItemColorModel
import com.oc.christmas.ocmaker.create.avatar.databinding.ItemColorBinding

class ColorLayerCustomizeAdapter(val context: Context) :
    BaseAdapter<ItemColorModel, ItemColorBinding>(ItemColorBinding::inflate) {
    var onItemClick: ((Int) -> Unit) = {}
    override fun onBind(binding: ItemColorBinding, item: ItemColorModel, position: Int) {
        binding.apply {
            imvImage.setBackgroundColor(item.color.toColorInt())
            imvFocus.isVisible = item.isSelected
            root.tap { onItemClick.invoke(position) }
        }
    }
}