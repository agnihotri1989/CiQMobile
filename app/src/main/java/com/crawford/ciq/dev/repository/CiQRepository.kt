package com.crawford.ciq.dev.repository

import com.crawford.ciq.dev.api.ApiHelper
import com.google.gson.JsonObject

class CiQRepository (private val apiHelper: ApiHelper){



    suspend fun getClaims(adjusterId:String,claimId:Int,bearertoken:String) = apiHelper.getClaims(adjusterId,claimId,bearertoken)
    suspend fun getinsuredaddress(claimId:Int,bearertoken:String) = apiHelper.getinsuredaddress(claimId,bearertoken)
    suspend fun getclaimandclaimantaddress(ClaimantId:Int,ClaimId:Int,bearertoken:String) = apiHelper.getclaimantaddress(ClaimantId,ClaimId,bearertoken)
    suspend fun getmyClaims(adjusterId:String,bearertoken:String) = apiHelper.getmyClaims(adjusterId,bearertoken)
    suspend fun getdockets(ClaimId:Int,bearertoken:String) = apiHelper.getdockets(ClaimId,bearertoken)
    suspend fun getpostdocketresult(jsonbody:JsonObject,bearertoken:String) = apiHelper.getpostdocketresult(jsonbody,bearertoken)
    suspend fun getclaimant(ClaimId:Int,bearertoken:String) = apiHelper.getclaimant(ClaimId,bearertoken)
    suspend fun getClaimSubscription(ClaimId:Int,bearertoken:String) = apiHelper.getClaimSubscription(ClaimId,bearertoken)
    suspend fun gettimecode(bearertoken:String) = apiHelper.gettimecode(bearertoken)
    suspend fun getexpensecode(bearertoken:String) = apiHelper.getexpensecode(bearertoken)
    suspend fun gettimecodedetails(code:String,bearertoken:String) = apiHelper.gettimecodedetails(code,bearertoken)
    suspend fun getuser(NetworkID:String,bearertoken: String) = apiHelper.getuser(NetworkID,bearertoken)
    suspend fun getbearertoken(jsonObject: JsonObject) = apiHelper.getbearertoken(jsonObject)
    suspend fun getnote(claimId: Int,DocketId:Int,bearertoken:String) = apiHelper.getnote(claimId,DocketId,bearertoken)
}