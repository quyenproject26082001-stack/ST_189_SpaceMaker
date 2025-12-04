package com.oc.space.ocmaker.create.ui.language

import android.annotation.SuppressLint
import android.content.Context
import com.oc.space.ocmaker.create.R
import com.oc.space.ocmaker.create.core.base.BaseAdapter
import com.oc.space.ocmaker.create.core.extensions.gone
import com.oc.space.ocmaker.create.core.extensions.loadImage
import com.oc.space.ocmaker.create.core.extensions.tap
import com.oc.space.ocmaker.create.core.extensions.visible
import com.oc.space.ocmaker.create.data.model.LanguageModel
import com.oc.space.ocmaker.create.databinding.ItemLanguageBinding

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

            // Áp gradient trực tiếp vào text

            val ratio = if (item.activate) {
                R.drawable.ic_tick_lang
            } else {
                R.drawable.ic_not_tick_lang
            }
            tvLang.post {
                if (tvLang.paint.shader == null) {
                    val shader = android.graphics.LinearGradient(
                        0f,
                        tvLang.height.toFloat(),
                        0f,
                        0f,
                        intArrayOf(
                            android.graphics.Color.parseColor("#2641D7"),
                            android.graphics.Color.parseColor("#8FFFFD")
                        ),
                        null,
                        android.graphics.Shader.TileMode.CLAMP
                    )
                    tvLang.paint.shader = shader
                    tvLang.invalidate()
                }
            }
            loadImage(root, ratio, btnRadio, false)

            flMain.setBackgroundResource(if (item.activate) R.drawable.bg_16_stroke_yellow_f6_solid_red_ba else R.drawable.bg_16_stroke_yellow_f6_solid_red_ba)


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