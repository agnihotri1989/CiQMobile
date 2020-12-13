package com.crawford.ciq.dev.model

import com.google.gson.annotations.SerializedName
import retrofit2.SkipCallbackExecutor

data class TimeCodeDetailResult(
    @SerializedName("timeCode") val timeCode: String,
    @SerializedName("description") val description: String,
    @SerializedName("serviceFee") val serviceFee: Boolean,
    @SerializedName("disallowBackdateOnDocket") val disallowBackdateOnDocket: Boolean,
    @SerializedName("unbillable") val unbillable: Boolean,
    @SerializedName("templatePayload") val templatePayload: templatePayloadResult
) {

    data class templatePayloadResult(
        @SerializedName("templateCode") val templateCode: String,
        @SerializedName("templateFields") val templateFields: List<templateFieldsResult>
    ) {

    }

    data class templateFieldsResult(
        @SerializedName("templateFieldId") var templateFieldId: Int,
        @SerializedName("templateFieldName") var templateFieldName: String,
        @SerializedName("templateFieldValue") var templateFieldValue: List<String>,
        @SerializedName("dataType") var dataType: String,
        @SerializedName("sequenceId") var sequenceId: Int,
        @SerializedName("sourceType") var sourceType: String,var viewtype: Int = 0
    ) {


    }
}

