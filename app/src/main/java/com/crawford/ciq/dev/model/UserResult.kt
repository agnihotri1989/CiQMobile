package com.crawford.ciq.dev.model

import com.google.gson.annotations.SerializedName

data class UserResult(@SerializedName("id") var id:String,
                      @SerializedName("networkID") var networkID:String) {
}