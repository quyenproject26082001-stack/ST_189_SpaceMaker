package com.oc.christmas.ocmaker.create.avatar.ui.random_character

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.Log
import com.bumptech.glide.Glide
import com.oc.christmas.ocmaker.create.avatar.core.base.BaseAdapter
import com.oc.christmas.ocmaker.create.avatar.core.utils.key.ValueKey
import com.oc.christmas.ocmaker.create.avatar.data.model.custom.SuggestionModel
import com.oc.christmas.ocmaker.create.avatar.databinding.ItemRandomCharacterBinding
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.core.graphics.createBitmap
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.oc.christmas.ocmaker.create.avatar.core.extensions.gone
import com.oc.christmas.ocmaker.create.avatar.core.extensions.invisible
import com.oc.christmas.ocmaker.create.avatar.core.extensions.tap
import com.oc.christmas.ocmaker.create.avatar.core.extensions.visible
import com.oc.christmas.ocmaker.create.avatar.core.helper.MediaHelper
import com.oc.christmas.ocmaker.create.avatar.core.utils.state.SaveState


class RandomCharacterAdapter(val context: Context) :
    BaseAdapter<SuggestionModel, ItemRandomCharacterBinding>(ItemRandomCharacterBinding::inflate) {
    var onItemClick: ((SuggestionModel) -> Unit) = {}
    override fun onBind(binding: ItemRandomCharacterBinding, item: SuggestionModel, position: Int) {
        binding.apply {
            sflShimmer.visible()
            sflShimmer.startShimmer()
            imvImage.invisible()

            var width = ValueKey.WIDTH_BITMAP
            var height = ValueKey.HEIGHT_BITMAP

            val listBitmap: ArrayList<Bitmap> = arrayListOf()
            val handleExceptionCoroutine = CoroutineExceptionHandler { _, throwable ->
                Log.e("nbhieu", "random_character: ${throwable.message}")
            }
            CoroutineScope(SupervisorJob() + Dispatchers.IO + handleExceptionCoroutine).launch {
                val job1 = async {
                    val bitmapDefault = Glide.with(context).asBitmap().load(item.pathSelectedList.first()).submit().get()
                    width = bitmapDefault.width/2 ?: ValueKey.WIDTH_BITMAP
                    height = bitmapDefault.height/2 ?: ValueKey.HEIGHT_BITMAP
                    if (items[position].pathInternalRandom == ""){
                        item.pathSelectedList.forEach { path ->
                            listBitmap.add(Glide.with(context).asBitmap().load(path).submit(width, height).get())
                        }
                    }
                    return@async true
                }

                withContext(Dispatchers.Main) {
                    if (job1.await()) {
                        if (items[position].pathInternalRandom == ""){
                            val combinedBitmap = createBitmap(width, height)
                            val canvas = Canvas(combinedBitmap)

                            for (i in 0 until listBitmap.size) {
                                val bitmap = listBitmap[i]
                                val left = (width - bitmap.width) / 2f
                                val top = (height - bitmap.height) / 2f
                                canvas.drawBitmap(bitmap, left, top, null)
                            }

                            MediaHelper.saveBitmapToInternalStorage(context, ValueKey.RANDOM_TEMP_ALBUM, combinedBitmap).collect { state ->
                                when(state){
                                    is SaveState.Loading -> {}
                                    is SaveState.Error -> {}
                                    is SaveState.Success -> {
                                        items[position].pathInternalRandom = state.path
                                    }
                                }
                            }
                        }


                        Glide.with(root).load(items[position].pathInternalRandom).listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable?>, isFirstResource: Boolean): Boolean {
                                sflShimmer.stopShimmer()
                                sflShimmer.gone()
                                return false
                            }

                            override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable?>?, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                                sflShimmer.stopShimmer()
                                sflShimmer.gone()
                                imvImage.visible()
                                return false
                            }
                        }).into(imvImage)
                    }
                }
            }

            root.tap { onItemClick.invoke(item) }
        }
    }
}