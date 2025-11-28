package com.oc.christmas.ocmaker.create.avatar.ui.language

import android.annotation.SuppressLint
import android.content.Context
import com.oc.christmas.ocmaker.create.avatar.R
import com.oc.christmas.ocmaker.create.avatar.core.base.BaseAdapter
import com.oc.christmas.ocmaker.create.avatar.core.extensions.gone
import com.oc.christmas.ocmaker.create.avatar.core.extensions.loadImage
import com.oc.christmas.ocmaker.create.avatar.core.extensions.tap
import com.oc.christmas.ocmaker.create.avatar.core.extensions.visible
import com.oc.christmas.ocmaker.create.avatar.data.model.LanguageModel
import com.oc.christmas.ocmaker.create.avatar.databinding.ItemLanguageBinding

class LanguageAdapter(val context: Context) : BaseAdapter<LanguageModel, ItemLanguageBinding>(
    ItemLanguageBinding::inflate
) {
    var onItemClick: ((String) -> Unit) = {}
    override fun onBind(
        binding: ItemLanguageBinding, item: LanguageModel, position: Int
    ) {
        binding.apply {
            loadImage(root, item.flag, imvFlag, false)
            tvLang.text = item.name

            val (ratio, color) = if (item.activate) {
                R.drawable.ic_tick_lang to context.getColor(R.color.white)
            } else {
                R.drawable.ic_not_tick_lang to context.getColor(R.color.black)
            }
            loadImage(root, ratio, btnRadio, false)

            flMain.setBackgroundResource(if (item.activate) R.drawable.bg_16_stroke_yellow_f6_solid_red_ba else R.drawable.bg_16_solid_white)

            tvLang.setTextColor(color)

            root.tap {
                onItemClick.invoke(item.code)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitItem(position: Int) {
        items.forEach { it.activate = false }
        items[position].activate = true
        notifyDataSetChanged()
    }
}