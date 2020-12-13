package com.crawford.ciq.dev.model

import com.google.gson.annotations.SerializedName

data class DocketResult(
    @SerializedName("timeCode") var timeCode: String = "",
    @SerializedName("timeCodeDescription") var timeCodeDescription: String = "",
    var status: Boolean = false,var priority:Int = 0
) {
}