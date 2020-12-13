
package com.crawford.ciq.dev.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.crawford.ciq.dev.model.ClaimResult

class ClaimListFragmentViewModel:ViewModel() {
    protected var getcliamlist: MutableLiveData<List<ClaimResult>>? = null
}