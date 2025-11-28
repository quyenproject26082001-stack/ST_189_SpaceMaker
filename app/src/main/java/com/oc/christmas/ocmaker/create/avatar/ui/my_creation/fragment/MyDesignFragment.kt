package com.oc.christmas.ocmaker.create.avatar.ui.my_creation.fragment

import android.app.ActivityOptions
import android.content.Intent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.oc.christmas.ocmaker.create.avatar.R
import com.oc.christmas.ocmaker.create.avatar.core.base.BaseFragment
import com.oc.christmas.ocmaker.create.avatar.core.extensions.gone
import com.oc.christmas.ocmaker.create.avatar.core.extensions.hideNavigation
import com.oc.christmas.ocmaker.create.avatar.core.extensions.invisible
import com.oc.christmas.ocmaker.create.avatar.core.extensions.showInterAll
import com.oc.christmas.ocmaker.create.avatar.core.extensions.tap
import com.oc.christmas.ocmaker.create.avatar.core.extensions.visible
import com.oc.christmas.ocmaker.create.avatar.core.helper.LanguageHelper
import com.oc.christmas.ocmaker.create.avatar.core.utils.key.IntentKey
import com.oc.christmas.ocmaker.create.avatar.core.utils.key.ValueKey
import com.oc.christmas.ocmaker.create.avatar.core.utils.state.HandleState
import com.oc.christmas.ocmaker.create.avatar.databinding.FragmentMyDesignBinding
import com.oc.christmas.ocmaker.create.avatar.dialog.YesNoDialog
import com.oc.christmas.ocmaker.create.avatar.ui.customize.CustomizeCharacterActivity
import com.oc.christmas.ocmaker.create.avatar.ui.home.DataViewModel
import com.oc.christmas.ocmaker.create.avatar.ui.my_creation.MyCreationActivity
import com.oc.christmas.ocmaker.create.avatar.ui.my_creation.view_model.MyCreationViewModel
import com.oc.christmas.ocmaker.create.avatar.ui.my_creation.adapter.MyAvatarAdapter
import com.oc.christmas.ocmaker.create.avatar.ui.my_creation.adapter.MyDesignAdapter
import com.oc.christmas.ocmaker.create.avatar.ui.my_creation.view_model.MyAvatarViewModel
import com.oc.christmas.ocmaker.create.avatar.ui.my_creation.view_model.MyDesignViewModel
import com.oc.christmas.ocmaker.create.avatar.ui.view.ViewActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.getValue

class MyDesignFragment : BaseFragment<FragmentMyDesignBinding>() {
    private val viewModel: MyDesignViewModel by viewModels()
    private val myCreationViewModel: MyCreationViewModel by activityViewModels()
    private val myDesignAdapter by lazy { MyDesignAdapter() }

    private val myAlbumActivity: MyCreationActivity
        get() = requireActivity() as MyCreationActivity

    override fun setViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentMyDesignBinding {
        return FragmentMyDesignBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        initRcv()
        viewModel.loadMyDesign(myAlbumActivity)
    }

    override fun dataObservable() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.myDesignList.collect { list ->
                        myDesignAdapter.submitList(list)
                        binding.layoutNoItem.isVisible = list.isEmpty()
                    }
                }
                launch {
                    viewModel.isLastItem.collect { selectStatus ->
                        myAlbumActivity.changeImageActionBarRight(selectStatus)
                    }
                }
                launch {
                    myCreationViewModel.typeStatus.collect { status ->
                        resetData()
                    }
                }
            }
        }
    }

    override fun viewListener() {
        binding.apply {
            rcvMyDesign.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
                override fun onInterceptTouchEvent(
                    recyclerView: RecyclerView, motionEvent: MotionEvent
                ): Boolean {
                    return when {
                        motionEvent.action != MotionEvent.ACTION_UP || recyclerView.findChildViewUnder(
                            motionEvent.x, motionEvent.y
                        ) != null -> false

                        else -> {
                            resetData()
                            true
                        }
                    }
                }

                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
                override fun onTouchEvent(recyclerView: RecyclerView, motionEvent: MotionEvent) {}
            })
            myAlbumActivity.binding.actionBar.btnActionBarRight.tap { handleSelectAll() }
            myAlbumActivity.binding.actionBar.btnActionBarNextToRight.tap { handleDelete(viewModel.getPathSelected()) }
            myAlbumActivity.binding.btnDownload.tap { myAlbumActivity.handleDownload(viewModel.getPathSelected()) }
            myDesignAdapter.onItemClick = { pathInternal -> handleItemClick(pathInternal) }
            myDesignAdapter.onItemTick = { position -> viewModel.toggleSelect(position) }
            myDesignAdapter.onDeleteClick = { pathInternal -> handleDelete(arrayListOf(pathInternal)) }
            myDesignAdapter.onLongClick = { position -> handleLongClick(position) }
        }
    }

    private fun initRcv() {
        binding.apply {
            rcvMyDesign.apply {
                adapter = myDesignAdapter
                itemAnimator = null
            }
        }
    }

    private fun handleDelete(pathInternalList: ArrayList<String>) {
        if (pathInternalList.isEmpty()) {
            myAlbumActivity.showToast(R.string.please_select_an_image)
            return
        }
        val dialog = YesNoDialog(myAlbumActivity, R.string.delete, R.string.are_you_sure_want_to_delete_this_item)
        LanguageHelper.setLocale(myAlbumActivity)
        dialog.show()
        dialog.onDismissClick = {
            dialog.dismiss()
            myAlbumActivity.hideNavigation()
        }
        dialog.onNoClick = {
            dialog.dismiss()
            myAlbumActivity.hideNavigation()
        }
        dialog.onYesClick = {
            lifecycleScope.launch(Dispatchers.IO) {
                viewModel.deleteItem(pathInternalList)
                withContext(Dispatchers.Main) {
                    dialog.dismiss()
                    myAlbumActivity.hideNavigation()
                    resetData()
                }
            }
        }
    }

    private fun handleItemClick(pathInternal: String) {
        val intent = Intent(myAlbumActivity, ViewActivity::class.java)
        intent.putExtra(IntentKey.INTENT_KEY, pathInternal)
        intent.putExtra(IntentKey.TYPE_KEY, ValueKey.TYPE_VIEW)
        intent.putExtra(IntentKey.STATUS_KEY, ValueKey.MY_DESIGN_TYPE)
        val options = ActivityOptions.makeCustomAnimation(myAlbumActivity, R.anim.slide_in_right, R.anim.slide_out_left)
        myAlbumActivity.showInterAll { startActivity(intent, options.toBundle()) }
    }

    private fun handleLongClick(position: Int) {
        viewModel.showLongClick(position)
        handleSelectList(false)
    }

    private fun handleSelectList(isHide: Boolean) {
        if (isHide) {
            myAlbumActivity.binding.actionBar.btnActionBarRight.invisible()
            myAlbumActivity.binding.actionBar.btnActionBarNextToRight.gone()
            myAlbumActivity.binding.lnlBottom.gone()

        } else {
            myAlbumActivity.binding.actionBar.btnActionBarRight.visible()
            myAlbumActivity.binding.actionBar.btnActionBarNextToRight.gone()
            myAlbumActivity.binding.lnlBottomTop.gone()
            myAlbumActivity.binding.lnlBottom.visible()
        }
    }

    private fun resetData() {
        viewModel.loadMyDesign(myAlbumActivity)
        handleSelectList(true)
        myAlbumActivity.changeImageActionBarRight(true)
    }

    private fun handleSelectAll() {
        val shouldSelectAll = viewModel.myDesignList.value.any { !it.isSelected }
        myAlbumActivity.changeImageActionBarRight(!shouldSelectAll)
        viewModel.selectAll(shouldSelectAll)
    }
    
    override fun onStart() {
        super.onStart()
        resetData()
    }
}