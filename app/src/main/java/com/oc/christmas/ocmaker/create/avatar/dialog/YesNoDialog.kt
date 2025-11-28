package com.oc.christmas.ocmaker.create.avatar.dialog

import android.app.Activity
import com.oc.christmas.ocmaker.create.avatar.core.extensions.gone
import com.oc.christmas.ocmaker.create.avatar.core.extensions.hideNavigation
import com.oc.christmas.ocmaker.create.avatar.core.extensions.tap
import com.oc.christmas.ocmaker.create.avatar.R
import com.oc.christmas.ocmaker.create.avatar.core.base.BaseDialog
import com.oc.christmas.ocmaker.create.avatar.core.extensions.strings
import com.oc.christmas.ocmaker.create.avatar.databinding.DialogConfirmBinding


class YesNoDialog(
    val context: Activity, val title: Int, val description: Int, val isError: Boolean = false
) : BaseDialog<DialogConfirmBinding>(context, maxWidth = true, maxHeight = true) {
    override val layoutId: Int = R.layout.dialog_confirm
    override val isCancelOnTouchOutside: Boolean = false
    override val isCancelableByBack: Boolean = false

    var onNoClick: (() -> Unit) = {}
    var onYesClick: (() -> Unit) = {}
    var onDismissClick: (() -> Unit) = {}

    override fun initView() {
        initText()
        if (isError) {
            binding.btnNo.gone()
        }
        context.hideNavigation()
    }

    override fun initAction() {
        binding.apply {
            btnNo.tap { onNoClick.invoke() }
            btnYes.tap { onYesClick.invoke() }
            flOutSide.tap { onDismissClick.invoke() }
        }
    }

    override fun onDismissListener() {

    }

    private fun initText() {
        binding.apply {
            tvTitle.text = context.strings(title)
            tvDescription.text = context.strings(description)
        }
    }
}