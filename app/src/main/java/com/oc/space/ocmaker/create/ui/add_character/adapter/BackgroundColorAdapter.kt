package com.oc.space.ocmaker.create.ui.add_character.adapter

import androidx.core.view.isVisible
import com.oc.space.ocmaker.create.R
import com.oc.space.ocmaker.create.core.base.BaseAdapter
import com.oc.space.ocmaker.create.core.extensions.gone
import com.oc.space.ocmaker.create.core.extensions.loadImage
import com.oc.space.ocmaker.create.core.extensions.tap
import com.oc.space.ocmaker.create.core.extensions.visible
import com.oc.space.ocmaker.create.data.model.SelectedModel
import com.oc.space.ocmaker.create.databinding.ItemBackgroundColorBinding
import com.oc.space.ocmaker.create.databinding.ItemBackgroundImageBinding

class BackgroundColorAdapter :
    BaseAdapter<SelectedModel, ItemBackgroundColorBinding>(ItemBackgroundColorBinding::inflate) {
    var onChooseColorClick: (() -> Unit) = {}
    var onBackgroundColorClick: ((Int, Int) -> Unit) = {_,_ ->}

    var currentSelected = -1
    override fun onBind(binding: ItemBackgroundColorBinding, item: SelectedModel, position: Int) {
        binding.apply {
            vFocus.isVisible = item.isSelected
            if (position == 0) {
                vFocus.gone()
                loadImage(root, R.drawable.img, imvColor)
                root.tap { onChooseColorClick.invoke() }
            } else {
                vFocus.isVisible = item.isSelected
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