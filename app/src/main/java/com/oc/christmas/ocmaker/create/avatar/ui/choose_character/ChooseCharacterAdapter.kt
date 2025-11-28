package com.oc.christmas.ocmaker.create.avatar.ui.choose_character

import com.oc.christmas.ocmaker.create.avatar.core.base.BaseAdapter
import com.oc.christmas.ocmaker.create.avatar.core.extensions.gone
import com.oc.christmas.ocmaker.create.avatar.core.extensions.loadImage
import com.oc.christmas.ocmaker.create.avatar.core.extensions.tap
import com.oc.christmas.ocmaker.create.avatar.data.model.custom.CustomizeModel
import com.oc.christmas.ocmaker.create.avatar.databinding.ItemChooseAvatarBinding

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