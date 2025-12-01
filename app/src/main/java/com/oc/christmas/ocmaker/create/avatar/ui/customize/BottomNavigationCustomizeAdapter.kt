package com.oc.christmas.ocmaker.create.avatar.ui.customize

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.oc.christmas.ocmaker.create.avatar.R
import com.oc.christmas.ocmaker.create.avatar.core.extensions.loadImage
import com.oc.christmas.ocmaker.create.avatar.core.extensions.tap
import com.oc.christmas.ocmaker.create.avatar.core.helper.UnitHelper
import com.oc.christmas.ocmaker.create.avatar.data.model.custom.NavigationModel
import com.oc.christmas.ocmaker.create.avatar.databinding.ItemBottomNavigationBinding

class BottomNavigationCustomizeAdapter(private val context: Context) :
    ListAdapter<NavigationModel, BottomNavigationCustomizeAdapter.BottomNavViewHolder>(DiffCallback) {
    var onItemClick: (Int) -> Unit = {}

    inner class BottomNavViewHolder(
        private val binding: ItemBottomNavigationBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NavigationModel, position: Int) = with(binding) {

            if (item.isSelected) {
                vFocus.setBackgroundResource(R.drawable.bg_bottom_navi)
            } else {
                vFocus.setBackgroundColor(context.getColor(R.color.white))
            }

            loadImage(root, item.imageNavigation, imvImage)

            root.tap { onItemClick.invoke(position) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomNavViewHolder {
        val binding = ItemBottomNavigationBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BottomNavViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BottomNavViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<NavigationModel>() {
            override fun areItemsTheSame(oldItem: NavigationModel, newItem: NavigationModel): Boolean {
                // Nếu NavigationModel có id riêng thì nên so sánh id, ở đây tạm so sánh hình
                return oldItem.imageNavigation == newItem.imageNavigation
            }

            override fun areContentsTheSame(oldItem: NavigationModel, newItem: NavigationModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}
