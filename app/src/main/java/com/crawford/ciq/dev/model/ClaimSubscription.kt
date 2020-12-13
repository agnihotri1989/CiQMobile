package com.crawford.ciq.dev.model

import android.content.Intent
import android.net.Uri
import android.telephony.PhoneNumberUtils
import android.view.View
import com.google.gson.annotations.SerializedName

data class ClaimSubscription(
    @SerializedName("percentage") var percentage: Int = 0,
    @SerializedName("policyNumber") var policyNumber: String = "",
    @SerializedName("examinersName") var examinersName: String = "",
    @SerializedName("examinersPhone") var examinersPhone: String = "",
    @SerializedName("examinersEmailAddress") var examinersEmailAddress: String = "" ,
    @SerializedName("address1") var address1: String = "",
    @SerializedName("address2") var address2: String = "",
    @SerializedName("city") var city: String = "",
    @SerializedName("province") var province: String = "",
    @SerializedName("country") var country: String = "",
    @SerializedName("postalCode") var postalCode: String = ""
) {


    fun getAddressformat(address1: String,address2: String,city: String,province: String,country: String,postalCode: String

    ):String{

        return "${address1}\n${address2}\n{city}, ${province}, ${postalCode}\n${country}"
    }
    public fun retrivephonenumebrformat(phonenumeber:String): String{

//        var formattednumber:String = "+1"
//        if(phonenumeber.length==10){
//         formattednumber = "${formattednumber} ( ${phonenumeber.substring(0,3)} ) ${phonenumeber.substring(3,6)} - ${phonenumeber.substring(6,10)}"
//
//        return formattednumber
//        }else
//        {
//            return "+1"+PhoneNumberUtils.formatNumber(phonenumeber,"US")
//        }
        return "+1"+PhoneNumberUtils.formatNumber(phonenumeber,"US")
    }

    fun onPhoneClick(view : View, phonenumber:String){
        val u = Uri.parse("tel:" + phonenumber)

        val i = Intent(Intent.ACTION_DIAL, u)

        try {
            view.context.startActivity(i)

        } catch (s: SecurityException) {


        }
    }

    fun onNavigatetoGoogleMaps(view : View, address1: String,address2: String,city: String,province: String,country: String,postalCode: String){
        val url =
            "http://maps.google.co.in/maps?q=${address1}," +
                    "${address2}," +
                    "${city}, ${province}, ${postalCode}," +
                    "${country}"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        view.context.startActivity(intent)
    }

     fun sendEmail(view :View,recipient: String) {

        val mIntent = Intent(Intent.ACTION_SEND)

        mIntent.data = Uri.parse("mailto:")
        mIntent.type = "text/plain"

        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))


        try {
            //start email intent
            view.context.startActivity(Intent.createChooser(mIntent, "Choose Email Client..."))
        }
        catch (e: Exception){
            //if any thing goes wrong for example no email client application or any exception
            //get and show exception message

        }

    }

}