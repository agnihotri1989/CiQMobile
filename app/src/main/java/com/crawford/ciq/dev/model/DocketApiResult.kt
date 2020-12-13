package com.crawford.ciq.dev.model

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat

data class DocketApiResult(
    @SerializedName("id") var id: Int = 0,
    @SerializedName("claimID") var claimID: Int = 0,
    @SerializedName("branchID") var branchID: Int = 0,
    @SerializedName("timeCode") var timeCode: String = "",
    @SerializedName("expenseAmount") var expenseAmount: Double = 0.0,
    @SerializedName("createdDate") var createdDate: String = "",
    @SerializedName("docketRate") var docketRate: Double = 0.0,
    @SerializedName("adjusterID") var adjusterID: String = "",
    @SerializedName("timeCodeDescription") var timeCodeDescription: String = "",
    @SerializedName("note") var note: String = "",
    @SerializedName("timeAmount") var timeAmount: Double = 0.0,
    @SerializedName("timeUnits") var timeUnits: Double = 0.0,
    @SerializedName("expenseCode") var expenseCode: String = "",
    @SerializedName("expenseCodeDescription") var expenseCodeDescription: String = "",
    @SerializedName("expenseUnits") var expenseUnits: String = "",
    @SerializedName("expenseRatePerUnit") var expenseRatePerUnit: String = "0.0",
    var noteDetails:String="tobeupdate",
    @SerializedName("templatePayload") val templatePayload: TemplatePayloadResult
) {
    data class TemplatePayloadResult(
        @SerializedName("templateCode") val templateCode: String,
        @SerializedName("templateFields") val templateFields: List<TemplateFieldsResult>
    ) {

    }
    data class TemplateFieldsResult(
        @SerializedName("templateFieldId") var templateFieldId: Int,
        @SerializedName("templateFieldName") var templateFieldName: String,
        @SerializedName("templateFieldValue") var templateFieldValue: List<String>,
        @SerializedName("dataType") var dataType: String,
        @SerializedName("sequenceId") var sequenceId: Int,
        @SerializedName("sourceType") var sourceType: String,var viewtype: Int = 0
    ) {

        override fun toString(): String {
            return "${templateFieldName}\n${templateFieldValue[0]}\n"
        }
    }
    override fun toString(): String {
        return timeCode
    }
    fun retieveformatteddate(lossDate: String):String{
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formatter = SimpleDateFormat("MM/dd/yyyy HH:mm:ss a")
        val output: String = formatter.format(parser.parse(lossDate))
        return output
    }

}