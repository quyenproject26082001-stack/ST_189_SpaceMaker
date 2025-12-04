package com.oc.space.ocmaker.create.ui.choose_character

import com.oc.space.ocmaker.create.core.base.BaseAdapter
import com.oc.space.ocmaker.create.core.extensions.gone
import com.oc.space.ocmaker.create.core.extensions.loadImage
import com.oc.space.ocmaker.create.core.extensions.tap
import com.oc.space.ocmaker.create.data.model.custom.CustomizeModel
import com.oc.space.ocmaker.create.databinding.ItemChooseAvatarBinding

class ChooseCharacterAdapter : BaseAdapter<CustomizeModel, ItemChooseAvatarBinding>(ItemChooseAvatarBinding::inflate) {
    var onItemClick: ((position: Int) -> Unit) = {}
    override fun onBind(binding: ItemChooseAvatarBinding, item: CustomizeModel, position: Int) {
        binding.apply {
            loadImage(item.avatar, imvImage, onDismissLoading = {
                sflShimmer.stopShimmer()
                sflShimmer.gone()
            })
            root.tap { onItemClick.invoke(position) }
        }
    }
}