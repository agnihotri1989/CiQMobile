package com.crawford.ciq.dev

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.work.*
import com.crawford.ciq.dev.api.ApiHelper
import com.crawford.ciq.dev.api.RetrofitBuilder
import com.crawford.ciq.dev.repository.CiQRepository
import com.crawford.ciq.dev.utils.CiQSharedprefrences
import com.google.gson.JsonObject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.lang.Exception

class MyApplication : Application() {

    lateinit var periodicWorkRequest: PeriodicWorkRequest
    lateinit var periodicWorkRequestbuilder: PeriodicWorkRequest.Builder

    lateinit var aOneTimeSync: OneTimeWorkRequest
    val apiHelper: ApiHelper = ApiHelper(RetrofitBuilder.apiService)
    val ciQRepository: CiQRepository = CiQRepository(apiHelper)

    fun getAppContext(): Context {
        return applicationContext
    }

    override fun onCreate() {
        super.onCreate()

        Log.d("MyApplication", "application class called")
        CiQSharedprefrences.init(this)
        // if(CiQSharedprefrences.gettoken.equals("")){
        val jsonObject = JsonObject()
        jsonObject.addProperty("clientId", "VTP7GtduwA89f43pAq2IOZhnI9Z1HaBg")
        jsonObject.addProperty(
            "clientSecret",
            "G0Avcs6fhuET3ihmaWb9d6mvSw67hr40_71hDlIzGUMOKdpDrc4WYKuJ4rhw99tk"
        )
        jsonObject.addProperty("apiConsumerKey", "63777D0F-184A-458B-A135-D91BBD2DF9C9")

        GlobalScope.launch {
            try {
                val token: ResponseBody = ciQRepository.getbearertoken(jsonObject)
                CiQSharedprefrences.gettoken = "Bearer ${token.string()}"
                Log.d("<<TOKEN>>", CiQSharedprefrences.gettoken)
            } catch (e: Exception) {
                System.exit(0)
            }

        }
        // }

//        aOneTimeSync = OneTimeWorkRequest.Builder(GetTokenWorker::class.java)
//            .setConstraints(setConstraint()) //set Constraint
//            .addTag("After Click") //add tag
//            .setInitialDelay(0L, TimeUnit.MILLISECONDS) //10 seconds Initial delay
//
//            .build()
//        WorkManager.getInstance(this).enqueue(aOneTimeSync)


//        periodicWorkRequestbuilder = PeriodicWorkRequest.Builder(GetTokenWorker::class.java,0,TimeUnit.MINUTES)
//            .setConstraints(setConstraint()).addTag("Test")
//        periodicWorkRequest = periodicWorkRequestbuilder.build()
    }

    private fun setConstraint(): Constraints {

        return Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED) // check internet connectivity
            .setRequiresBatteryNotLow(true) // check battery level
            //.setRequiresCharging(true) // check charging mode
            // .setRequiresStorageNotLow(true) // check storage
            // .setRequiresDeviceIdle(false) // check device idle state
            .build()

    }
}