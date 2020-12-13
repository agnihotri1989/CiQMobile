package com.crawford.ciq.dev.model

import com.google.gson.annotations.SerializedName

data class NoteApiResult(@SerializedName("claimId") val claimId:Int,
                         @SerializedName("docketId") val docketId:Int,
                         @SerializedName("noteDetails") val noteDetails:String) {
}