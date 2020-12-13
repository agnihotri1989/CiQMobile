package com.crawford.ciq.dev.ui.home

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.crawford.ciq.dev.model.ClaimResult
import com.crawford.ciq.dev.model.ClaimantResult
import com.crawford.ciq.dev.model.DocketResult
import com.crawford.ciq.dev.model.MyClaimResult
import com.crawford.ciq.dev.repository.CiQRepository
import com.crawford.ciq.dev.utils.CiQSharedprefrences
import com.crawford.ciq.dev.utils.Resource
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class HomeViewModel(private val ciQRepository: CiQRepository) : ViewModel() {


    protected var getmycliamlist: MutableLiveData<List<MyClaimResult>>? = null



    lateinit var getbearertoken: String

    fun initContext(context: Context) {
        CiQSharedprefrences.init(context)
        getbearertoken = CiQSharedprefrences.gettoken!!
    }

    fun getmyClaims(adjusterId: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {

            if (getmycliamlist == null) {
                getmycliamlist = MutableLiveData<List<MyClaimResult>>()
                getmycliamlist!!.postValue(ciQRepository.getmyClaims(adjusterId,getbearertoken))
            }
            // emit(Resource.success(data = ciQRepository.getClaims(adjusterId)))
            emit(Resource.success(data = getmycliamlist))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }








    fun getBearerToken(jsonObject: JsonObject) = liveData(Dispatchers.IO) {

        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = ciQRepository.getbearertoken(jsonObject)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }


}