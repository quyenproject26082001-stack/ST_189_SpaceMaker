package com.oc.christmas.ocmaker.create.avatar.ui.random_character

import androidx.lifecycle.ViewModel
import com.oc.christmas.ocmaker.create.avatar.R
import com.oc.christmas.ocmaker.create.avatar.core.helper.InternetHelper
import com.oc.christmas.ocmaker.create.avatar.core.utils.state.HandleState
import com.oc.christmas.ocmaker.create.avatar.data.model.custom.SuggestionModel
import com.oc.christmas.ocmaker.create.avatar.ui.customize.CustomizeCharacterActivity
import kotlinx.coroutines.flow.MutableStateFlow

class RandomCharacterViewModel : ViewModel() {

    val randomList = ArrayList<SuggestionModel>()
    // Data từ API hay không
    private val _isDataAPI = MutableStateFlow(false)
    //-----------------------------------------------------------------------------------------------------------------

    suspend fun updateRandomList(suggestionModel: SuggestionModel){
        randomList.add(suggestionModel)
    }
    fun upsideDownList() = randomList.shuffle()

    fun setIsDataAPI(isAPI: Boolean) {
        _isDataAPI.value = isAPI
    }

    fun checkDataInternet(context: RandomCharacterActivity, action: (() -> Unit)) {
        if (!_isDataAPI.value) {
            action.invoke()
            return
        }
        InternetHelper.checkInternet(context) { result ->
            if (result == HandleState.SUCCESS) {
                action.invoke()
            } else {
                context.showToast(R.string.please_check_your_internet)
            }
        }
    }


}