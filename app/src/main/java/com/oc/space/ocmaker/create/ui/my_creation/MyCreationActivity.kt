package com.oc.space.ocmaker.create.ui.my_creation

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
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
import com.oc.space.ocmaker.create.R
import com.oc.space.ocmaker.create.core.base.BaseActivity
import com.oc.space.ocmaker.create.core.extensions.checkPermissions
import com.oc.space.ocmaker.create.core.extensions.goToSettings
import com.oc.space.ocmaker.create.core.extensions.gone
import com.oc.space.ocmaker.create.core.extensions.hideNavigation
import com.oc.space.ocmaker.create.core.extensions.invisible
import com.oc.space.ocmaker.create.core.extensions.loadNativeCollabAds
import com.oc.space.ocmaker.create.core.extensions.requestPermission
import com.oc.space.ocmaker.create.core.extensions.select
import com.oc.space.ocmaker.create.core.extensions.setImageActionBar
import com.oc.space.ocmaker.create.core.extensions.setTextActionBar
import com.oc.space.ocmaker.create.core.extensions.tap

import com.oc.space.ocmaker.create.core.extensions.startIntentWithClearTop
import com.oc.space.ocmaker.create.core.extensions.visible
import com.oc.space.ocmaker.create.core.helper.LanguageHelper
import com.oc.space.ocmaker.create.core.helper.UnitHelper
import com.oc.space.ocmaker.create.core.utils.key.IntentKey
import com.oc.space.ocmaker.create.core.utils.key.RequestKey
import com.oc.space.ocmaker.create.core.utils.key.ValueKey
import com.oc.space.ocmaker.create.core.utils.share.whatsapp.WhatsappSharingActivity
import com.oc.space.ocmaker.create.core.utils.state.HandleState
import com.oc.space.ocmaker.create.databinding.ActivityAlbumBinding
import com.oc.space.ocmaker.create.dialog.YesNoDialog
import com.oc.space.ocmaker.create.ui.home.HomeActivity
import com.oc.space.ocmaker.create.ui.view.ViewActivity
import com.oc.space.ocmaker.create.databinding.PopupMyAlbumBinding
import com.oc.space.ocmaker.create.dialog.CreateNameDialog
import com.oc.space.ocmaker.create.ui.my_creation.adapter.MyAvatarAdapter
import com.oc.space.ocmaker.create.ui.my_creation.adapter.TypeAdapter
import com.oc.space.ocmaker.create.ui.my_creation.fragment.MyAvatarFragment
import com.oc.space.ocmaker.create.ui.my_creation.fragment.MyDesignFragment
import com.oc.space.ocmaker.create.ui.my_creation.view_model.MyAvatarViewModel
import com.oc.space.ocmaker.create.ui.my_creation.view_model.MyCreationViewModel
import com.oc.space.ocmaker.create.ui.permission.PermissionViewModel
import kotlinx.coroutines.launch
import kotlin.text.replace

class MyCreationActivity : WhatsappSharingActivity<ActivityAlbumBinding>() {
    private val viewModel: MyCreationViewModel by viewModels()
    private val permissionViewModel: PermissionViewModel by viewModels()

    private var myAvatarFragment: MyAvatarFragment? = null
    private var myDesignFragment: MyDesignFragment? = null

    override fun setViewBinding(): ActivityAlbumBinding {
        return ActivityAlbumBinding.inflate(LayoutInflater.from(this))
    }

    override fun initView() {
        viewModel.setTypeStatus(ValueKey.AVATAR_TYPE)
        viewModel.setStatusFrom(intent.getBooleanExtra(IntentKey.FROM_SAVE, false))

        // Hide deleteSection by default
        binding.deleteSection.gone()
    }

    override fun dataObservable() {
        binding.apply {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.CREATED) {
                    launch {
                        viewModel.typeStatus.collect { type ->
                            if (type != -1) {
                                if (type == ValueKey.AVATAR_TYPE) {
                                    // MyAvatar selected
                                    setupSelectedTab(btnMySpace, tvSpace, imvFocusMyAvatar, subTabMyAvatar, isLeftTab = true)
                                    setupUnselectedTab(btnMyDesign, tvMyDesign, imvFocusMyDesign, subTabMyDesign, isLeftTab = false)
                                    showFragment(ValueKey.AVATAR_TYPE)
                                } else {
                                    // MyDesign selected
                                    setupSelectedTab(btnMyDesign, tvMyDesign, imvFocusMyDesign, subTabMyDesign, isLeftTab = false)
                                    setupUnselectedTab(btnMySpace, tvSpace, imvFocusMyAvatar, subTabMyAvatar, isLeftTab = true)
                                    showFragment(ValueKey.MY_DESIGN_TYPE)
                                }
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

            btnMySpace.tap { viewModel.setTypeStatus(ValueKey.AVATAR_TYPE) }
            btnMyDesign.tap { viewModel.setTypeStatus(ValueKey.MY_DESIGN_TYPE) }

            // Delete button in deleteSection
            btnDeleteSelect.setOnClickListener {
                android.util.Log.d("MyCreationActivity", "btnDeleteSelect clicked!")
                // Use fragment by tag instead of by ID
                val currentFragment = supportFragmentManager.findFragmentByTag("MyAvatarFragment")
                android.util.Log.d("MyCreationActivity", "Current fragment: $currentFragment")
                if (currentFragment is MyAvatarFragment && currentFragment.isVisible) {
                    android.util.Log.d("MyCreationActivity", "Calling deleteSelectedItems")
                    // Call delete method - dialog will handle exit selection mode
                    currentFragment.deleteSelectedItems()
                } else {
                    android.util.Log.d("MyCreationActivity", "MyAvatarFragment not visible or not found")
                }
            }
        }
    }

    override fun initActionBar() {
        binding.actionBar.apply {
            setImageActionBar(btnActionBarLeft, R.drawable.ic_back)
            setTextActionBar(tvCenter, getString(R.string.my_character))

            // Hide action bar buttons - using btnDeleteSelect instead
            btnActionBarRight.gone()
            btnActionBarNextToRight.gone()
        }
    }

    override fun initText() {
        binding.apply {
            tvSpace.select()
            tvMyDesign.select()
        }
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

    fun handleShare(list: ArrayList<String>) {
        if (list.isEmpty()) {
            showToast(R.string.please_select_an_image)
            return
        }
        viewModel.shareImages(this, list)
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

    private fun showFragment(type: Int) {
        val transaction = supportFragmentManager.beginTransaction()

        // Initialize fragments if null
        if (myAvatarFragment == null) {
            myAvatarFragment = MyAvatarFragment()
            transaction.add(R.id.frmList, myAvatarFragment!!, "MyAvatarFragment")
        }
        if (myDesignFragment == null) {
            myDesignFragment = MyDesignFragment()
            transaction.add(R.id.frmList, myDesignFragment!!, "MyDesignFragment")
        }

        // Show/Hide based on type
        if (type == ValueKey.AVATAR_TYPE) {
            myAvatarFragment?.let { transaction.show(it) }
            myDesignFragment?.let { transaction.hide(it) }
        } else {
            myAvatarFragment?.let { transaction.hide(it) }
            myDesignFragment?.let { transaction.show(it) }
        }

        transaction.commit()
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

    fun enterSelectionMode() {
        binding.apply {
            // Show delete section
            android.util.Log.d("MyCreationActivity", "enterSelectionMode called - showing deleteSection")
            deleteSection.visible()
            android.util.Log.d("MyCreationActivity", "deleteSection visibility: ${deleteSection.visibility}")
        }
    }

    fun exitSelectionMode() {
        binding.apply {
            // Hide delete section
            android.util.Log.d("MyCreationActivity", "exitSelectionMode called - hiding deleteSection")
            deleteSection.gone()
        }
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

        // Set text size = 20sp
        textView.textSize = 20f

        // Apply gradient color from top to bottom (using fixed height based on text size)
        val textHeight = textView.lineHeight.toFloat()
        val shader = LinearGradient(
            0f, 0f, 0f, textHeight,
            Color.parseColor("#8FFFFD"),
            Color.parseColor("#2641D7"),
            Shader.TileMode.CLAMP
        )
        textView.paint.shader = shader

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
        // Remove gradient shader and set solid color
        textView.paint.shader = null
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