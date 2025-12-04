package com.oc.space.ocmaker.create.ui.add_character.adapter

import com.oc.space.ocmaker.create.core.base.BaseAdapter
import com.oc.space.ocmaker.create.core.extensions.loadImage
import com.oc.space.ocmaker.create.core.extensions.tap
import com.oc.space.ocmaker.create.data.model.SelectedModel
import com.oc.space.ocmaker.create.databinding.ItemStickerBinding

class StickerAdapter : BaseAdapter<SelectedModel, ItemStickerBinding>(ItemStickerBinding::inflate) {
    var onItemClick : ((String) -> Unit) = {}
    override fun onBind(binding: ItemStickerBinding, item: SelectedModel, position: Int) {
        binding.apply {
            loadImage(root, item.path, imvSticker)
            root.tap { onItemClick.invoke(item.path) }
        }
    }
}