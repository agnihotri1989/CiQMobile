package com.crawford.ciq.dev.model

import android.content.Intent
import android.net.Uri
import android.telephony.PhoneNumberUtils
import android.view.View
import com.google.gson.annotations.SerializedName

data class InsuredAddressApiResult(
    @SerializedName("address1") val address1: String,
    @SerializedName("address2") val address2: String,
    @SerializedName("address3") val address3: String,
    @SerializedName("businessPhone") val businessPhone: String,
    @SerializedName("city") val city: String,
    @SerializedName("country") val country: String,
    @SerializedName("email") val email: String,
    @SerializedName("homePhone") val homePhone: String,
    @SerializedName("mobilePhone") val mobilePhone: String,
    @SerializedName("postalCode") val postalCode: String,
    @SerializedName("province") val province: String
) {


    fun getAddressformat(
        address1: String, address2: String,
        address3: String, city: String, province: String, country: String, postalCode: String

    ): String {
        var fullformattedaddress = ""
        if (!address1.isEmpty()) {
            fullformattedaddress="${fullformattedaddress}${address1}\n"
        }
        if (!address2.isEmpty()) {
            fullformattedaddress="${fullformattedaddress}${address2}\n"
        }
        if (!address3.isEmpty()) {
            fullformattedaddress="${fullformattedaddress}${address3}\n"
        }

        return "${fullformattedaddress}${city}, ${province}, ${postalCode}\n${country}"
    }

    fun onPhoneClick(view: View, phonenumber: String) {
        val u = Uri.parse("tel:" + phonenumber)

        val i = Intent(Intent.ACTION_DIAL, u)

        try {
            view.context.startActivity(i)

        } catch (s: SecurityException) {


        }
    }

    public fun retrivephonenumebrformat(phonenumeber: String): String {

//        var formattednumber:String = "+1"
//        if(phonenumeber.length==10){
//            formattednumber = "${formattednumber} ( ${phonenumeber.substring(0,3)} ) ${phonenumeber.substring(3,6)} - ${phonenumeber.substring(6,10)}"
//
//            return formattednumber
//        }else
//        {
//            return "+1"+ PhoneNumberUtils.formatNumber(phonenumeber,"US")
//        }
        return "+1" + PhoneNumberUtils.formatNumber(phonenumeber, "US")
    }

    fun onNavigatetoGoogleMaps(
        view: View, address1: String, address2: String,
        address3: String, city: String, province: String, country: String, postalCode: String
    ) {

        var fullformattedaddress = ""
        if (!address1.isEmpty()) {
            fullformattedaddress="${fullformattedaddress}${address1},"
        }
        if (!address2.isEmpty()) {
            fullformattedaddress="${fullformattedaddress}${address2},"
        }
        if (!address3.isEmpty()) {
            fullformattedaddress="${fullformattedaddress}${address3},"
        }
        val url =
            "http://maps.google.co.in/maps?q=${fullformattedaddress}" +
                    "${city}, ${province}, ${postalCode}," +
                    "${country}"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        view.context.startActivity(intent)
    }

    fun sendEmail(view: View, recipient: String) {

        val mIntent = Intent(Intent.ACTION_SEND)

        mIntent.data = Uri.parse("mailto:")
        mIntent.type = "text/plain"

        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))


        try {
            //start email intent
            view.context.startActivity(Intent.createChooser(mIntent, "Choose Email Client..."))
        } catch (e: Exception) {
            //if any thing goes wrong for example no email client application or any exception
            //get and show exception message

        }

    }
}