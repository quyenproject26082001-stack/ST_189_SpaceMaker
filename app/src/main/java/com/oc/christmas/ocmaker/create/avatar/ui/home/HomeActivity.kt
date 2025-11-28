package com.oc.christmas.ocmaker.create.avatar.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import com.lvt.ads.util.Admob
import com.oc.christmas.ocmaker.create.avatar.R
import com.oc.christmas.ocmaker.create.avatar.core.base.BaseActivity
import com.oc.christmas.ocmaker.create.avatar.core.extensions.loadNativeCollabAds
import com.oc.christmas.ocmaker.create.avatar.core.extensions.rateApp
import com.oc.christmas.ocmaker.create.avatar.core.extensions.select
import com.oc.christmas.ocmaker.create.avatar.core.extensions.setImageActionBar
import com.oc.christmas.ocmaker.create.avatar.core.extensions.showInterAll
import com.oc.christmas.ocmaker.create.avatar.core.extensions.startIntentRightToLeft
import com.oc.christmas.ocmaker.create.avatar.core.helper.LanguageHelper
import com.oc.christmas.ocmaker.create.avatar.core.helper.MediaHelper
import com.oc.christmas.ocmaker.create.avatar.core.utils.key.ValueKey
import com.oc.christmas.ocmaker.create.avatar.core.utils.state.RateState
import com.oc.christmas.ocmaker.create.avatar.databinding.ActivityHomeBinding
import com.oc.christmas.ocmaker.create.avatar.ui.SettingsActivity
import com.oc.christmas.ocmaker.create.avatar.ui.my_creation.MyCreationActivity
import com.oc.christmas.ocmaker.create.avatar.ui.choose_character.ChooseCharacterActivity
import com.oc.christmas.ocmaker.create.avatar.core.extensions.tap
import com.oc.christmas.ocmaker.create.avatar.core.extensions.strings
import com.oc.christmas.ocmaker.create.avatar.ui.random_character.RandomCharacterActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.system.exitProcess

class HomeActivity : BaseActivity<ActivityHomeBinding>() {

    override fun setViewBinding(): ActivityHomeBinding {
        return ActivityHomeBinding.inflate(LayoutInflater.from(this))
    }

    override fun initView() {
        sharePreference.setCountBack(sharePreference.getCountBack() + 1)
        deleteTempFolder()
    }

    override fun viewListener() {
        binding.apply {
            actionBar.btnActionBarRight.tap { startIntentRightToLeft(SettingsActivity::class.java) }
            btnCreate.tap { startIntentRightToLeft(ChooseCharacterActivity::class.java) }
            btnMyAlbum.tap { showInterAll { startIntentRightToLeft(MyCreationActivity::class.java) } }
            btnQuickMaker.tap { startIntentRightToLeft(RandomCharacterActivity::class.java) }
        }
    }

    override fun initText() {
        super.initText()
        binding.actionBar.tvCenter.select()
    }

    override fun initActionBar() {
        binding.actionBar.apply {
            setImageActionBar(btnActionBarRight, R.drawable.ic_settings)
        }
    }

    @SuppressLint("MissingSuperCall", "GestureBackNavigation")
    override fun onBackPressed() {
        if (!sharePreference.getIsRate(this) && sharePreference.getCountBack() % 2 == 0) {
            rateApp(sharePreference) { state ->
                if (state != RateState.CANCEL) {
                    showToast(R.string.have_rated)
                }
                lifecycleScope.launch {
                    withContext(Dispatchers.Main) {
                        delay(1000)
                        exitProcess(0)
                    }
                }
            }
        } else {
            exitProcess(0)
        }
    }

    private fun deleteTempFolder() {
        lifecycleScope.launch(Dispatchers.IO) {
            val dataTemp = MediaHelper.getImageInternal(this@HomeActivity, ValueKey.RANDOM_TEMP_ALBUM)
            if (dataTemp.isNotEmpty()) {
                dataTemp.forEach {
                    val file = File(it)
                    file.delete()
                }
            }
        }
    }

    private fun updateText() {
        binding.apply {
            tv1.text = strings(R.string.character_maker)
            tv2.text = strings(R.string.quick_maker)
            tv3.text = strings(R.string.my_character)
        }
    }

    override fun onRestart() {
        super.onRestart()
        deleteTempFolder()
        LanguageHelper.setLocale(this)
        updateText()
        initNativeCollab()
    }

    fun initNativeCollab() {
        loadNativeCollabAds(R.string.native_cl_home, binding.flNativeCollab, binding.scvMain)
    }

    override fun initAds() {
        initNativeCollab()
        Admob.getInstance().loadInterAll(this, getString(R.string.inter_all))
        Admob.getInstance().loadNativeAll(this, getString(R.string.native_all))
    }
}