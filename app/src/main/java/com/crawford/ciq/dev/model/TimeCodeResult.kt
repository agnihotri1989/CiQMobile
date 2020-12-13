package com.crawford.ciq.dev.model

import com.google.gson.annotations.SerializedName

data class TimeCodeResult(
    @SerializedName("code") val code: String,
    @SerializedName("description") val description: String,
    @SerializedName("serviceFee") val serviceFee: Boolean,
    @SerializedName("disallowBackdateOnDocket") val disallowBackdateOnDocket: Boolean,
    @SerializedName("unbillable") val unbillable: Boolean
) {


    override fun toString(): String {
        return "${code}         ${description}"
    }
}