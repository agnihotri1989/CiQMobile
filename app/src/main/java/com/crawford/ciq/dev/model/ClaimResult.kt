package com.crawford.ciq.dev.model

import android.content.Intent
import android.net.Uri
import android.view.View
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class ClaimResult(
    @SerializedName("id") var filenumber: Int = 0,
    @SerializedName("receiveDate") var receiveDate: String = "",
    @SerializedName("branchID") var branchID: String = "",
    @SerializedName("catastrophicLossCode") var catastrophicLossCode: String = "",
    @SerializedName("claimTypeCode") var claimTypeCode: String = "",
    @SerializedName("lossLocation") var lossLocation: String = "",
    @SerializedName("insuredFirstName") var insuredFirstName: String = "",
    @SerializedName("insuredName") var insuredName: String = "",
    @SerializedName("organizationCode") var organizationCode: String = "",
    @SerializedName("cscRefNumber") var cscRefNumber: String = "",
    @SerializedName("lineOfBusinessCode") var lineOfBusinessCode: String = "",
    @SerializedName("lossDate") var lossDate: String = "",
    @SerializedName("adjusterID") var adjusterID: String = "",
    var distance: Int = (0..50).random(),
    var claimstatus:String = if((1..2).random()==1) "assist" else "alert",
    var address1: String = "",
    var address2: String = "",
    var city: String = "",
    var province: String = "",
    var country: String ="",
    var postalCode: String = ""
) {

fun retieveformatteddate(lossDate: String):String{
    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    val formatter = SimpleDateFormat("MM/dd/yyyy HH:mm:ss a")
    val output: String = formatter.format(parser.parse(lossDate))
    return output
}

    fun getAddressformat(
        address1: String,
        address2: String,
        city: String,
        province: String,
        country: String,
        postalCode: String

    ): String {
        var fullformattedaddress = ""
        if (address1!=null) {
            fullformattedaddress = "${fullformattedaddress}${address1}\n"
        }
        if (address2!=null ) {
            if(!address2.equals(""))
                fullformattedaddress = "${fullformattedaddress}${address2}\n"
        }


        return "${fullformattedaddress}${city}, ${province}, ${postalCode}\n${country}"
    }
    fun onNavigatetoGoogleMaps(view: View, address1: String,
                               address2: String,
                               city: String,
                               province: String,
                               country: String,
                               postalCode: String) {

        var fullformattedaddress = ""
        if (address1!=null) {
            if(!address1.isEmpty())fullformattedaddress="${fullformattedaddress}${address1},"
        }
        if (address2!=null ) {
            if(!address2.isEmpty())fullformattedaddress="${fullformattedaddress}${address2},"
        }

        val url =
            "http://maps.google.co.in/maps?q=${fullformattedaddress}" +
                    "${city}, ${province}, ${postalCode}," +
                    "${country}"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        view.context.startActivity(intent)
    }
}