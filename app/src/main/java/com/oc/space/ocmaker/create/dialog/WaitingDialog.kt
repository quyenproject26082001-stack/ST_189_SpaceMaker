package com.oc.space.ocmaker.create.dialog

import android.app.Activity
import com.oc.space.ocmaker.create.R
import com.oc.space.ocmaker.create.core.base.BaseDialog
import com.oc.space.ocmaker.create.core.extensions.setBackgroundConnerSmooth
import com.oc.space.ocmaker.create.databinding.DialogLoadingBinding

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