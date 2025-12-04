package com.oc.space.ocmaker.create.ui.intro

import android.content.Context
import com.oc.space.ocmaker.create.core.base.BaseAdapter
import com.oc.space.ocmaker.create.core.extensions.loadImage
import com.oc.space.ocmaker.create.core.extensions.select
import com.oc.space.ocmaker.create.core.extensions.strings
import com.oc.space.ocmaker.create.data.model.IntroModel
import com.oc.space.ocmaker.create.databinding.ItemIntroBinding

class IntroAdapter(val context: Context) : BaseAdapter<IntroModel, ItemIntroBinding>(
    ItemIntroBinding::inflate
) {
    override fun onBind(binding: ItemIntroBinding, item: IntroModel, position: Int) {
        binding.apply {
            loadImage(root, item.image, imvImage, false)
            tvContent.text = context.strings(item.content)
            tvContent.select()
        }
    }
}