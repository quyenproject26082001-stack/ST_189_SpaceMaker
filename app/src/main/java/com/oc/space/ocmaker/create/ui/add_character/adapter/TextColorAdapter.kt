package com.oc.space.ocmaker.create.ui.add_character.adapter

import android.annotation.SuppressLint
import androidx.core.view.isVisible
import com.oc.space.ocmaker.create.R
import com.oc.space.ocmaker.create.core.base.BaseAdapter
import com.oc.space.ocmaker.create.core.extensions.gone
import com.oc.space.ocmaker.create.core.extensions.tap
import com.oc.space.ocmaker.create.core.extensions.visible
import com.oc.space.ocmaker.create.data.model.SelectedModel
import com.oc.space.ocmaker.create.databinding.ItemTextColorBinding

class TextColorAdapter : BaseAdapter<SelectedModel, ItemTextColorBinding>(ItemTextColorBinding::inflate) {
    var onChooseColorClick: (() -> Unit) = {}
    var onTextColorClick: ((Int, Int) -> Unit) = { _, _ -> }

    private var currentSelected = 1


    override fun onBind(binding: ItemTextColorBinding, item: SelectedModel, position: Int) {
        binding.apply {
            vFocus.isVisible = item.isSelected

            if (position == 0) {
                imvColor.visible()
                imvColor.setImageResource(R.drawable.img0text_color)
                btnAddColor.visible()
                root.tap { onChooseColorClick.invoke() }
            } else {
                imvColor.visible()
                btnAddColor.gone()
                imvColor.setBackgroundColor(item.color)

                root.tap { onTextColorClick.invoke(item.color, position) }
            }
        }
    }

    fun submitItem(position: Int, list: ArrayList<SelectedModel>) {
        if (position != currentSelected) {
            items.clear()
            items.addAll(list)

            notifyItemChanged(currentSelected)
            notifyItemChanged(position)

            currentSelected = position
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitListReset(list: ArrayList<SelectedModel>){
        items.clear()
        items.addAll(list)
        currentSelected = 1
        notifyDataSetChanged()
    }
}