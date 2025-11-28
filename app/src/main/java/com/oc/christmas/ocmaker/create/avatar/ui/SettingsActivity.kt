package com.oc.christmas.ocmaker.create.avatar.ui

import android.view.LayoutInflater
import com.oc.christmas.ocmaker.create.avatar.R
import com.oc.christmas.ocmaker.create.avatar.core.base.BaseActivity
import com.oc.christmas.ocmaker.create.avatar.core.extensions.gone
import com.oc.christmas.ocmaker.create.avatar.core.extensions.handleBackLeftToRight
import com.oc.christmas.ocmaker.create.avatar.core.extensions.policy
import com.oc.christmas.ocmaker.create.avatar.core.extensions.select
import com.oc.christmas.ocmaker.create.avatar.core.extensions.setImageActionBar
import com.oc.christmas.ocmaker.create.avatar.core.extensions.setTextActionBar
import com.oc.christmas.ocmaker.create.avatar.core.extensions.shareApp
import com.oc.christmas.ocmaker.create.avatar.core.extensions.startIntentRightToLeft
import com.oc.christmas.ocmaker.create.avatar.core.extensions.visible
import com.oc.christmas.ocmaker.create.avatar.core.utils.key.IntentKey
import com.oc.christmas.ocmaker.create.avatar.core.utils.state.RateState
import com.oc.christmas.ocmaker.create.avatar.databinding.ActivitySettingsBinding
import com.oc.christmas.ocmaker.create.avatar.ui.language.LanguageActivity
import com.oc.christmas.ocmaker.create.avatar.core.extensions.tap
import com.oc.christmas.ocmaker.create.avatar.core.helper.RateHelper
import kotlin.jvm.java

class SettingsActivity : BaseActivity<ActivitySettingsBinding>() {
    override fun setViewBinding(): ActivitySettingsBinding {
        return ActivitySettingsBinding.inflate(LayoutInflater.from(this))
    }

    override fun initView() {
        initRate()
    }

    override fun viewListener() {
        binding.apply {
            actionBar.btnActionBarLeft.tap { handleBackLeftToRight() }
            btnLang.tap { startIntentRightToLeft(LanguageActivity::class.java, IntentKey.INTENT_KEY) }
            btnShare.tap(1500) { shareApp() }
            btnRate.tap {
                RateHelper.showRateDialog(this@SettingsActivity, sharePreference){ state ->
                    if (state != RateState.CANCEL){
                        btnRate.gone()
                        showToast(R.string.have_rated)
                    }
                }
            }
            btnPolicy.tap(1500) { policy() }
        }
    }

    override fun initText() {
        binding.actionBar.tvCenter.select()
    }

    override fun initActionBar() {
        binding.actionBar.apply {
            setImageActionBar(btnActionBarLeft, R.drawable.ic_back)
            setTextActionBar(tvCenter, getString(R.string.settings))
        }
    }

    private fun initRate() {
        if (sharePreference.getIsRate(this)) {
            binding.btnRate.gone()
        } else {
            binding.btnRate.visible()
        }
    }
}