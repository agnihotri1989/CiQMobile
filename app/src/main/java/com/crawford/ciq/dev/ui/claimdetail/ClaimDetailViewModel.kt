package com.crawford.ciq.dev.ui.claimdetail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.*
import com.crawford.ciq.dev.model.*
import com.crawford.ciq.dev.repository.CiQRepository
import com.crawford.ciq.dev.utils.CiQSharedprefrences
import com.crawford.ciq.dev.utils.Resource
import com.google.gson.JsonObject
import kotlinx.coroutines.*

class ClaimDetailViewModel(private val ciQRepository: CiQRepository) : ViewModel() {
    protected var getcliamlist: MutableLiveData<List<ClaimResult>>? = null
    protected var getinsuredaddresslist: MutableLiveData<List<InsuredAddressApiResult>>? = null
    protected var getclaimandclaimantaddresslist: MutableLiveData<List<AddressApiResult>>? = null
    protected var getcliamsubscriptionlist: MutableLiveData<List<ClaimSubscription>>? = null
    protected var getclaimantlist: MutableLiveData<List<ClaimantApiResult>>? = null
    lateinit var getbearertoken: String
    protected var getdocketlist: MutableLiveData<List<DocketApiResult>>? = null
    protected var getpostdocketlist: MutableLiveData<PostDocketApiResult>? = null
    protected var gettimecodelist: MutableLiveData<List<TimeCodeResult>>? = null
    protected var getexpensecodelist: MutableLiveData<List<ExpenseCodeResult>>? = null
    protected var gettimecodedetailist: MutableLiveData<TimeCodeDetailResult>? = null
    protected var getnotelist: MutableLiveData<List<NoteApiResult>>? = null


    public fun fetchTimeandexpensecode(claimID: Int) {
        viewModelScope.launch {


            try {
                if (gettimecodelist == null || getexpensecodelist == null) {
                    gettimecodelist = MutableLiveData<List<TimeCodeResult>>()
                    getexpensecodelist = MutableLiveData<List<ExpenseCodeResult>>()
                    getinsuredaddresslist = MutableLiveData<List<InsuredAddressApiResult>>()
                    getclaimandclaimantaddresslist = MutableLiveData<List<AddressApiResult>>()
                    coroutineScope {

                        val getaddressfromapideferred = async {
                            ciQRepository.getclaimandclaimantaddress(0, claimID, getbearertoken)
                        }
                        val getinsuredaddress = async {
                            ciQRepository.getinsuredaddress(claimID, getbearertoken)
                        }
                        val timecodefromApiDefered = async {
                            ciQRepository.gettimecode(getbearertoken)
                        }

                        val expensecodefromApiDefered = async {
                            ciQRepository.getexpensecode(getbearertoken)
                        }
                        val addressfromapi = getaddressfromapideferred.await()
                        val insuredaddressfromapi = getinsuredaddress.await()
                        val timecodeformapi = timecodefromApiDefered.await()
                        val expensecodefromapi = expensecodefromApiDefered.await()
                        getclaimandclaimantaddresslist!!.postValue(addressfromapi)
                        getinsuredaddresslist!!.postValue(insuredaddressfromapi)
                        gettimecodelist!!.postValue(timecodeformapi)
                        getexpensecodelist!!.postValue(expensecodefromapi)
                    }

                }
                // emit(Resource.success(data = ciQRepository.getClaims(adjusterId)))

            } catch (exception: Exception) {

            }

        }
    }

    fun initContext(context: Context, claimID: Int) {
        CiQSharedprefrences.init(context)
        getbearertoken = CiQSharedprefrences.gettoken!!
        fetchTimeandexpensecode(claimID)
    }


    fun getClaims(adjusterId: String, claimId: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            if (getcliamlist == null) {
                getcliamlist = MutableLiveData<List<ClaimResult>>()


                getcliamlist!!.postValue(
                    ciQRepository.getClaims(
                        adjusterId,
                        claimId,
                        getbearertoken
                    )
                )


            }
            // emit(Resource.success(data = ciQRepository.getClaims(adjusterId)))
            emit(Resource.success(data = getcliamlist))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getinsuredaddress(claimId: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            if (getinsuredaddresslist == null) {
                getinsuredaddresslist = MutableLiveData<List<InsuredAddressApiResult>>()


                getinsuredaddresslist!!.postValue(
                    ciQRepository.getinsuredaddress(

                        claimId,
                        getbearertoken
                    )
                )


            }
            // emit(Resource.success(data = ciQRepository.getClaims(adjusterId)))
            emit(Resource.success(data = getinsuredaddresslist))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getclaimandclaimantaddresslist(claimantId: Int, claimId: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {

            if (claimId == 0) {
                val getclaimantaddresslist = MutableLiveData<List<AddressApiResult>>()


                getclaimantaddresslist!!.postValue(
                    ciQRepository.getclaimandclaimantaddress(

                        claimantId, claimId,
                        getbearertoken
                    )
                )
                // emit(Resource.success(data = ciQRepository.getClaims(adjusterId)))
                emit(Resource.success(data = getclaimantaddresslist))
            } else {
                if (getclaimandclaimantaddresslist == null) {
                    getclaimandclaimantaddresslist = MutableLiveData<List<AddressApiResult>>()


                    getclaimandclaimantaddresslist!!.postValue(
                        ciQRepository.getclaimandclaimantaddress(

                            claimantId, claimId,
                            getbearertoken
                        )
                    )


                }
                emit(Resource.success(data = getclaimandclaimantaddresslist))
            }


            // emit(Resource.success(data = ciQRepository.getClaims(adjusterId)))

        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getClaimSubscription(adjusterId: String, claimId: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            if (getcliamsubscriptionlist == null) {
                getcliamsubscriptionlist = MutableLiveData<List<ClaimSubscription>>()
                getcliamsubscriptionlist!!.postValue(
                    ciQRepository.getClaimSubscription(
                        claimId,
                        getbearertoken
                    )
                )
            }
            // emit(Resource.success(data = ciQRepository.getClaims(adjusterId)))
            emit(Resource.success(data = getcliamsubscriptionlist))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getDockets(ClaimId: Int, statusoflist: Boolean) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {

            if (statusoflist) {
                getdocketlist = MutableLiveData<List<DocketApiResult>>()
                getdocketlist!!.postValue(ciQRepository.getdockets(ClaimId, getbearertoken))

            } else {
                if (getdocketlist == null) {
                    getdocketlist = MutableLiveData<List<DocketApiResult>>()
                    getdocketlist!!.postValue(ciQRepository.getdockets(ClaimId, getbearertoken))
                }
            }

            //emit(Resource.success(data = ciQRepository.getdockets(ClaimId)))
            emit(Resource.success(data = getdocketlist))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getpostdocketresult(jsonbody: JsonObject) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            if (getpostdocketlist == null) {

            }
            getpostdocketlist = MutableLiveData<PostDocketApiResult>()
            getpostdocketlist!!.postValue(
                ciQRepository.getpostdocketresult(
                    jsonbody,
                    getbearertoken
                )
            )
            //emit(Resource.success(data = ciQRepository.getdockets(ClaimId)))
            emit(Resource.success(data = getpostdocketlist))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getclaimant(ClaimId: Int) = liveData(Dispatchers.IO) {


        emit(Resource.loading(data = null))

        try {
            if (getclaimantlist == null) {
                getclaimantlist = MutableLiveData<List<ClaimantApiResult>>()
                getclaimantlist!!.postValue(ciQRepository.getclaimant(ClaimId, getbearertoken))
            }
            // emit(Resource.success(data = ciQRepository.getClaims(adjusterId)))
            emit(Resource.success(data = getclaimantlist))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }

    }

    fun gettimecodelist() = liveData(Dispatchers.IO) {


        emit(Resource.loading(data = null))

        try {
            if (gettimecodelist == null) {
                gettimecodelist = MutableLiveData<List<TimeCodeResult>>()


                gettimecodelist!!.postValue(ciQRepository.gettimecode(getbearertoken))
            }
            // emit(Resource.success(data = ciQRepository.getClaims(adjusterId)))
            emit(Resource.success(data = gettimecodelist))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }

    }

    fun returntimecode(): LiveData<List<TimeCodeResult>> {
        return gettimecodelist!!
    }

    fun returnexpensecode(): LiveData<List<ExpenseCodeResult>> {
        return getexpensecodelist!!
    }

    fun getexpensecodelist() = liveData(Dispatchers.IO) {


        emit(Resource.loading(data = null))

        try {
            if (getexpensecodelist == null) {
                getexpensecodelist = MutableLiveData<List<ExpenseCodeResult>>()
                getexpensecodelist!!.postValue(ciQRepository.getexpensecode(getbearertoken))
            }
            // emit(Resource.success(data = ciQRepository.getClaims(adjusterId)))
            emit(Resource.success(data = getexpensecodelist))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }

    }

    fun gettimecodedetaillist(timecode: String) = liveData(Dispatchers.IO) {

        emit(Resource.loading(data = null))

        try {
//            if (gettimecodedetailist == null) {
//
//            }

            gettimecodedetailist = MutableLiveData<TimeCodeDetailResult>()
            gettimecodedetailist!!.postValue(
                ciQRepository.gettimecodedetails(
                    timecode,
                    getbearertoken
                )
            )
            // emit(Resource.success(data = ciQRepository.getClaims(adjusterId)))
            emit(Resource.success(data = gettimecodedetailist))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }

    }

    fun getnote(claimId: Int, DocketId: Int) = liveData(Dispatchers.IO) {

        emit(Resource.loading(data = null))

        try {
            if (getnotelist == null) {

            }
            getnotelist = MutableLiveData<List<NoteApiResult>>()
            getnotelist!!.postValue(ciQRepository.getnote(claimId, DocketId, getbearertoken))
            // emit(Resource.success(data = ciQRepository.getClaims(adjusterId)))
            emit(Resource.success(data = getnotelist))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }

    }


}