package com.oc.christmas.ocmaker.create.avatar.ui.add_character.adapter

import com.oc.christmas.ocmaker.create.avatar.core.base.BaseAdapter
import com.oc.christmas.ocmaker.create.avatar.core.extensions.loadImage
import com.oc.christmas.ocmaker.create.avatar.core.extensions.tap
import com.oc.christmas.ocmaker.create.avatar.data.model.SelectedModel
import com.oc.christmas.ocmaker.create.avatar.databinding.ItemStickerBinding

class StickerAdapter : BaseAdapter<SelectedModel, ItemStickerBinding>(ItemStickerBinding::inflate) {
    var onItemClick : ((String) -> Unit) = {}
    override fun onBind(binding: ItemStickerBinding, item: SelectedModel, position: Int) {
        binding.apply {
            loadImage(root, item.path, imvSticker)
            root.tap { onItemClick.invoke(item.path) }
        }
    }
}