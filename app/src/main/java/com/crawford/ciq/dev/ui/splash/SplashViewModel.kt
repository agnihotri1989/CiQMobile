package com.crawford.ciq.dev.ui.splash

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.crawford.ciq.dev.model.MyClaimResult
import com.crawford.ciq.dev.model.UserResult
import com.crawford.ciq.dev.repository.CiQRepository
import com.crawford.ciq.dev.utils.CiQSharedprefrences
import com.crawford.ciq.dev.utils.Resource
import kotlinx.coroutines.Dispatchers

class SplashViewModel (private val ciQRepository: CiQRepository) : ViewModel() {
    lateinit var getbearertoken: String
    protected var getuserlist: MutableLiveData<List<UserResult>>? = null
    fun initContext(context: Context) {
        CiQSharedprefrences.init(context)
        getbearertoken = CiQSharedprefrences.gettoken!!
    }

    fun getAdjusterId(networkId: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {

            if (getuserlist == null) {
                getuserlist = MutableLiveData<List<UserResult>>()
                getuserlist!!.postValue(ciQRepository.getuser(networkId,getbearertoken))
            }
            // emit(Resource.success(data = ciQRepository.getClaims(adjusterId)))
            emit(Resource.success(data = getuserlist))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}