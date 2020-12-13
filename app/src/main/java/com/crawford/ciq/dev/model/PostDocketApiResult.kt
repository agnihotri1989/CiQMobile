package com.crawford.ciq.dev.model

import com.google.gson.annotations.SerializedName

data class PostDocketApiResult(
    @SerializedName("isSuccessful") val isSuccessful: Boolean,
    @SerializedName("id") val id: Int,
    @SerializedName("activityId") val activityId:String,
    @SerializedName("message") val message:String
) {
}