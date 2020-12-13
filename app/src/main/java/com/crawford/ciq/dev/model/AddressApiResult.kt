package com.crawford.ciq.dev.model

import com.google.gson.annotations.SerializedName
import retrofit2.SkipCallbackExecutor

data class AddressApiResult(
    @SerializedName("address1") val address1: String,
    @SerializedName("address2") val address2: String,
    @SerializedName("city") val city: String,
    @SerializedName("province") val province: String,
    @SerializedName("country") val country: String,
    @SerializedName("postalCode")val postalCode:String,
    @SerializedName("claimantId")val claimantId:Int,
    @SerializedName("claimId")val claimId:Int
) {
}