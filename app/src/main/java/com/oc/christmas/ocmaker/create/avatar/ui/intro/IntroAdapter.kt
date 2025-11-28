package com.oc.christmas.ocmaker.create.avatar.ui.intro

import android.content.Context
import com.oc.christmas.ocmaker.create.avatar.core.base.BaseAdapter
import com.oc.christmas.ocmaker.create.avatar.core.extensions.loadImage
import com.oc.christmas.ocmaker.create.avatar.core.extensions.select
import com.oc.christmas.ocmaker.create.avatar.core.extensions.strings
import com.oc.christmas.ocmaker.create.avatar.data.model.IntroModel
import com.oc.christmas.ocmaker.create.avatar.databinding.ItemIntroBinding

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