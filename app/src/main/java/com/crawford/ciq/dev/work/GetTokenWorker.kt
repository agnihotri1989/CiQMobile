package com.crawford.ciq.dev.work

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.crawford.ciq.dev.api.ApiHelper
import com.crawford.ciq.dev.api.RetrofitBuilder
import com.crawford.ciq.dev.repository.CiQRepository
import com.crawford.ciq.dev.utils.CiQSharedprefrences
import com.google.gson.JsonObject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody

class GetTokenWorker(
    ctx: Context,
    params: WorkerParameters
) : Worker(ctx, params) {

    val apiHelper: ApiHelper = ApiHelper(RetrofitBuilder.apiService)
    val ciQRepository: CiQRepository = CiQRepository(apiHelper)
    override fun doWork(): Result {
        return try {
            val jsonObject = JsonObject()
            jsonObject.addProperty("clientId", "VTP7GtduwA89f43pAq2IOZhnI9Z1HaBg")
            jsonObject.addProperty(
                "clientSecret",
                "G0Avcs6fhuET3ihmaWb9d6mvSw67hr40_71hDlIzGUMOKdpDrc4WYKuJ4rhw99tk"
            )
            jsonObject.addProperty("apiConsumerKey", "63777D0F-184A-458B-A135-D91BBD2DF9C9")
            GlobalScope.launch {
                if(CiQSharedprefrences.gettoken.equals("")){
                    val token:ResponseBody = ciQRepository.getbearertoken(jsonObject)
                    CiQSharedprefrences.gettoken = token.string()
                    Log.d("<<TOKEN>>",token.string())
                }
            }

            Result.success()
        } catch (throwable: Throwable) {
            Result.failure()
        }
    }
}