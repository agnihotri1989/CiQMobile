package com.crawford.ciq.dev.api

import com.crawford.ciq.dev.model.*
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.http.*

interface ApiService {

    @POST("Account/authenticate")
    suspend fun getbearertoken(@Body jsonBody:JsonObject): ResponseBody

    @GET("Claim")
    suspend fun getClaims(@Query("AdjusterId") AdjusterId:String,@Query("ClaimId") claimId:Int,
                          @Header("Authorization") bearertoken:String): List<ClaimResult>
    @GET("InsuredAddress")
    suspend fun getinsuredaddress(@Query("ClaimId") claimId:Int,
                          @Header("Authorization") bearertoken:String): List<InsuredAddressApiResult>
    @GET("ClaimSubscription")
    suspend fun getClaimSubscription(@Query("ClaimId") ClaimId:Int,
                                     @Header("Authorization") bearertoken:String): List<ClaimSubscription>
    @GET("Claim/MyClaims")
    suspend fun getmyClaims(@Query("AdjusterId") AdjusterId:String,
                          @Header("Authorization") bearertoken:String): List<MyClaimResult>
    @GET("Docket")
    suspend fun getdockets(@Query("ClaimId") ClaimId:Int,@Header("Authorization") bearertoken:String):List<DocketApiResult>
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @POST("Docket")
    suspend fun getpostdocketresult(@Body jsonbody:JsonObject, @Header("Authorization") bearertoken:String):PostDocketApiResult
    @GET("Claimant")
    suspend fun getclaimant(@Query("ClaimId")ClaimId:Int,@Header("Authorization") bearertoken:String):List<ClaimantApiResult>
    @GET("Address")
    suspend fun getclaimantaddress(@Query("ClaimantId")ClaimantId:Int,@Query("ClaimId")ClaimId:Int,@Header("Authorization") bearertoken:String):List<AddressApiResult>

    @GET("User")
    suspend fun getUser(@Query("NetworkID") NetworkID:String,@Header("Authorization") bearertoken:String):List<UserResult>
    @GET("TimeCode")
    suspend fun gettimecode(@Header("Authorization") bearertoken:String):List<TimeCodeResult>
    @GET("ExpenseCode")
    suspend fun getexpensecode(@Header("Authorization") bearertoken:String):List<ExpenseCodeResult>
    @GET("TimeCode/Details")
    suspend fun gettimecodedetails(@Query("Code") Code:String,@Header("Authorization") bearertoken:String):TimeCodeDetailResult
    @GET("Note")
    suspend fun getnote(@Query("ClaimId") ClaimId:Int,@Query("DocketId") DocketId:Int,@Header("Authorization") bearertoken:String):List<NoteApiResult>

}