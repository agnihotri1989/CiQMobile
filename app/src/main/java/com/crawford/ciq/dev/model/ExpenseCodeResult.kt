package com.crawford.ciq.dev.model

import com.google.gson.annotations.SerializedName

data class ExpenseCodeResult(
    @SerializedName("code") val code: String,
    @SerializedName("description") val description: String,
    @SerializedName("defaultUnitRate") val defaultUnitRate: Double
) {

    override fun toString(): String {
        return "${code}         ${description}"
    }
}