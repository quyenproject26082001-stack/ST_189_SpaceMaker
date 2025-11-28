package com.oc.christmas.ocmaker.create.avatar.ui.add_character.adapter

import androidx.core.view.isVisible
import com.oc.christmas.ocmaker.create.avatar.R
import com.oc.christmas.ocmaker.create.avatar.core.base.BaseAdapter
import com.oc.christmas.ocmaker.create.avatar.core.extensions.gone
import com.oc.christmas.ocmaker.create.avatar.core.extensions.loadImage
import com.oc.christmas.ocmaker.create.avatar.core.extensions.tap
import com.oc.christmas.ocmaker.create.avatar.core.extensions.visible
import com.oc.christmas.ocmaker.create.avatar.data.model.SelectedModel
import com.oc.christmas.ocmaker.create.avatar.databinding.ItemBackgroundColorBinding
import com.oc.christmas.ocmaker.create.avatar.databinding.ItemBackgroundImageBinding

class BackgroundColorAdapter :
    BaseAdapter<SelectedModel, ItemBackgroundColorBinding>(ItemBackgroundColorBinding::inflate) {
    var onChooseColorClick: (() -> Unit) = {}
    var onBackgroundColorClick: ((Int, Int) -> Unit) = {_,_ ->}

    var currentSelected = -1
    override fun onBind(binding: ItemBackgroundColorBinding, item: SelectedModel, position: Int) {
        binding.apply {
            vFocus.isVisible = item.isSelected
            if (position == 0) {
                loadImage(root, R.drawable.ic_choose_color, imvColor)
                root.tap { onChooseColorClick.invoke() }
            } else {
                imvColor.setBackgroundColor(item.color)
                root.tap { onBackgroundColorClick.invoke(item.color, position) }
            }
        }
    }

    fun submitItem(position: Int, list: ArrayList<SelectedModel>){
        if (position != currentSelected){
            items.clear()
            items.addAll(list)

            notifyItemChanged(currentSelected)
            notifyItemChanged(position)

            currentSelected = position
        }
    }
}