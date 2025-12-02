package com.oc.christmas.ocmaker.create.avatar.ui.my_creation

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.PopupWindow
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.lvt.ads.util.Admob
import com.oc.christmas.ocmaker.create.avatar.R
import com.oc.christmas.ocmaker.create.avatar.core.base.BaseActivity
import com.oc.christmas.ocmaker.create.avatar.core.extensions.checkPermissions
import com.oc.christmas.ocmaker.create.avatar.core.extensions.goToSettings
import com.oc.christmas.ocmaker.create.avatar.core.extensions.gone
import com.oc.christmas.ocmaker.create.avatar.core.extensions.hideNavigation
import com.oc.christmas.ocmaker.create.avatar.core.extensions.invisible
import com.oc.christmas.ocmaker.create.avatar.core.extensions.loadNativeCollabAds
import com.oc.christmas.ocmaker.create.avatar.core.extensions.requestPermission
import com.oc.christmas.ocmaker.create.avatar.core.extensions.select
import com.oc.christmas.ocmaker.create.avatar.core.extensions.setImageActionBar
import com.oc.christmas.ocmaker.create.avatar.core.extensions.setTextActionBar
import com.oc.christmas.ocmaker.create.avatar.core.extensions.tap

import com.oc.christmas.ocmaker.create.avatar.core.extensions.startIntentWithClearTop
import com.oc.christmas.ocmaker.create.avatar.core.extensions.visible
import com.oc.christmas.ocmaker.create.avatar.core.helper.LanguageHelper
import com.oc.christmas.ocmaker.create.avatar.core.helper.UnitHelper
import com.oc.christmas.ocmaker.create.avatar.core.utils.key.IntentKey
import com.oc.christmas.ocmaker.create.avatar.core.utils.key.RequestKey
import com.oc.christmas.ocmaker.create.avatar.core.utils.key.ValueKey
import com.oc.christmas.ocmaker.create.avatar.core.utils.share.whatsapp.WhatsappSharingActivity
import com.oc.christmas.ocmaker.create.avatar.core.utils.state.HandleState
import com.oc.christmas.ocmaker.create.avatar.databinding.ActivityAlbumBinding
import com.oc.christmas.ocmaker.create.avatar.dialog.YesNoDialog
import com.oc.christmas.ocmaker.create.avatar.ui.home.HomeActivity
import com.oc.christmas.ocmaker.create.avatar.ui.view.ViewActivity
import com.oc.christmas.ocmaker.create.avatar.databinding.PopupMyAlbumBinding
import com.oc.christmas.ocmaker.create.avatar.dialog.CreateNameDialog
import com.oc.christmas.ocmaker.create.avatar.ui.my_creation.adapter.MyAvatarAdapter
import com.oc.christmas.ocmaker.create.avatar.ui.my_creation.adapter.TypeAdapter
import com.oc.christmas.ocmaker.create.avatar.ui.my_creation.fragment.MyAvatarFragment
import com.oc.christmas.ocmaker.create.avatar.ui.my_creation.fragment.MyDesignFragment
import com.oc.christmas.ocmaker.create.avatar.ui.my_creation.view_model.MyAvatarViewModel
import com.oc.christmas.ocmaker.create.avatar.ui.my_creation.view_model.MyCreationViewModel
import com.oc.christmas.ocmaker.create.avatar.ui.permission.PermissionViewModel
import kotlinx.coroutines.launch
import kotlin.text.replace

class MyCreationActivity : WhatsappSharingActivity<ActivityAlbumBinding>() {
    private val viewModel: MyCreationViewModel by viewModels()
    private val permissionViewModel: PermissionViewModel by viewModels()

    override fun setViewBinding(): ActivityAlbumBinding {
        return ActivityAlbumBinding.inflate(LayoutInflater.from(this))
    }

    override fun initView() {
        viewModel.setTypeStatus(ValueKey.AVATAR_TYPE)
        viewModel.setStatusFrom(intent.getBooleanExtra(IntentKey.FROM_SAVE, false))
    }

    override fun dataObservable() {
        binding.apply {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.CREATED) {
                    launch {
                        viewModel.typeStatus.collect { type ->
                            if (type != -1) {
                                val fragment = if (type == ValueKey.AVATAR_TYPE) {
                                    // MyAvatar selected
                                    setupSelectedTab(btnMyAvatar, tvMyAvatar, imvFocusMyAvatar, subTabMyAvatar, isLeftTab = true)
                                    setupUnselectedTab(btnMyDesign, tvMyDesign, imvFocusMyDesign, subTabMyDesign, isLeftTab = false)
                                    MyAvatarFragment()
                                } else {
                                    // MyDesign selected
                                    setupSelectedTab(btnMyDesign, tvMyDesign, imvFocusMyDesign, subTabMyDesign, isLeftTab = false)
                                    setupUnselectedTab(btnMyAvatar, tvMyAvatar, imvFocusMyAvatar, subTabMyAvatar, isLeftTab = true)
                                    MyDesignFragment()
                                }
                                startFragment(fragment)
                            }

                        }
                    }
                    launch {
                        viewModel.downloadState.collect { state ->
                            when (state) {
                                HandleState.LOADING -> {
                                    showLoading()
                                }

                                HandleState.SUCCESS -> {
                                    dismissLoading()
                                    hideNavigation()
                                    showToast(R.string.download_success)
                                }

                                else -> {
                                    dismissLoading()
                                    hideNavigation()
                                    showToast(R.string.download_failed_please_try_again_later)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun viewListener() {
        binding.apply {
            actionBar.apply {
                btnActionBarLeft.tap { startIntentWithClearTop(HomeActivity::class.java) }
            }

            btnMyAvatar.tap { viewModel.setTypeStatus(ValueKey.AVATAR_TYPE) }
            btnMyDesign.tap { viewModel.setTypeStatus(ValueKey.MY_DESIGN_TYPE) }
        }
    }

    override fun initActionBar() {
        binding.actionBar.apply {
            setImageActionBar(btnActionBarLeft, R.drawable.ic_back)
            setTextActionBar(tvCenter, getString(R.string.my_character))

            btnActionBarRight.setImageResource(R.drawable.ic_not_select_all)
            btnActionBarNextToRight.setImageResource(R.drawable.ic_delete_white)
        }
    }

    override fun initText() {
        binding.apply {
            tvWhatsapp.select()
            tvTelegram.select()
            tvMyAvatar.select()
            tvMyDesign.select()
        }
    }

    fun changeImageActionBarRight(isReset: Boolean) {
        val res = if (isReset) R.drawable.ic_not_select_all else R.drawable.ic_select_all
        binding.actionBar.btnActionBarRight.setImageResource(res)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == RequestKey.STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                permissionViewModel.updateStorageGranted(sharePreference, true)
                showToast(R.string.granted_storage)
            } else {
                permissionViewModel.updateStorageGranted(sharePreference, false)
            }
        }
    }

    fun handleAddToTelegram(list: ArrayList<String>) {
        if (list.isEmpty()) {
            showToast(R.string.please_select_an_image)
            return
        }
        viewModel.addToTelegram(this, list)
    }

    fun handleAddToWhatsApp(list: ArrayList<String>) {
        if (list.size < 3) {
            showToast(R.string.limit_3_items)
            return
        }
        if (list.size > 30) {
            showToast(R.string.limit_30_items)
            return
        }

        val dialog = CreateNameDialog(this)
        LanguageHelper.setLocale(this)
        dialog.show()

        fun dismissDialog() {
            dialog.dismiss()
            hideNavigation()
        }
        dialog.onNoClick = {
            dismissDialog()
        }
        dialog.onDismissClick = {
            dismissDialog()
        }

        dialog.onYesClick = { packageName ->
            dismissDialog()
            viewModel.addToWhatsapp(this, packageName, list) { stickerPack ->
                if (stickerPack != null) {
                    addToWhatsapp(stickerPack)
                }
            }
        }
    }

    fun handleDownload(list: ArrayList<String>) {
        if (list.isEmpty()) {
            showToast(R.string.please_select_an_image)
            return
        }
        viewModel.downloadFiles(this, list)
    }

    private fun startFragment(fragment: Fragment) {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.frmList)
        if (currentFragment?.javaClass != fragment.javaClass) {
            supportFragmentManager.beginTransaction().replace(R.id.frmList, fragment).commit()
        }
    }

    @SuppressLint("MissingSuperCall", "GestureBackNavigation")
    override fun onBackPressed() {
        startIntentWithClearTop(HomeActivity::class.java)
    }

    fun initNativeCollab() {
        loadNativeCollabAds(R.string.native_cl_myCharactor, binding.flNativeCollab, binding.lnlBottom)
    }
    override fun initAds() {
        initNativeCollab()
        Admob.getInstance().loadNativeAd(
            this,
            getString(R.string.native_myCharactor),
            binding.nativeAds,
            R.layout.ads_native_banner
        )
    }

    override fun onRestart() {
        super.onRestart()
        initNativeCollab()
    }

    private fun setupSelectedTab(
        tabView: View,
        textView: android.widget.TextView,
        focusImage: android.widget.ImageView,
        subTab: View,
        isLeftTab: Boolean
    ) {
        // Set weight = 1.6
        val params = tabView.layoutParams as android.widget.LinearLayout.LayoutParams
        params.weight = 1.6f
        params.topMargin = 0
        tabView.layoutParams = params

        // Set text size = 20sp, color = white
        textView.textSize = 20f
        textView.setTextColor(getColor(R.color.white))

        // Show selected_tab drawable (no flip for selected)
        focusImage.setImageResource(R.drawable.selected_tab)
        focusImage.scaleX = 1f
        focusImage.visible()

        // Hide subTab
        subTab.gone()
    }

    private fun setupUnselectedTab(
        tabView: View,
        textView: android.widget.TextView,
        focusImage: android.widget.ImageView,
        subTab: View,
        isLeftTab: Boolean
    ) {
        // Set weight = 1
        val params = tabView.layoutParams as android.widget.LinearLayout.LayoutParams
        params.weight = 1f
        params.topMargin = UnitHelper.dpToPx(this, 16f).toInt()
        tabView.layoutParams = params

        // Set text size = 16sp, color = colorPrimary
        textView.textSize = 16f
        textView.setTextColor(getColor(R.color.colorPrimary))

        // Show un_selected_tab drawable
        focusImage.setImageResource(R.drawable.un_selected_tab)
        // Flip horizontally if on left side
        focusImage.scaleX = if (isLeftTab) -1f else 1f
        focusImage.visible()

        // Show subTab
        subTab.visible()
    }
}