package com.oc.christmas.ocmaker.create.avatar.dialog

import android.app.Activity
import com.oc.christmas.ocmaker.create.avatar.R
import com.oc.christmas.ocmaker.create.avatar.core.base.BaseDialog
import com.oc.christmas.ocmaker.create.avatar.core.extensions.setBackgroundConnerSmooth
import com.oc.christmas.ocmaker.create.avatar.databinding.DialogLoadingBinding

class WaitingDialog(val context: Activity) :
    BaseDialog<DialogLoadingBinding>(context, maxWidth = true, maxHeight = true) {
    override val layoutId: Int = R.layout.dialog_loading
    override val isCancelOnTouchOutside: Boolean = false
    override val isCancelableByBack: Boolean = false

    override fun initView() {
    }

    override fun initAction() {}

    override fun onDismissListener() {}

}