package com.crawford.ciq.dev.model

import android.content.Intent
import android.net.Uri
import android.telephony.PhoneNumberUtils
import android.view.View
import com.google.gson.annotations.SerializedName

data class ClaimantApiResult(
    @SerializedName("injuryDamage") var injuryDamage: String = "",
    @SerializedName("firstName") var claimantfirstname: String = "",
    @SerializedName("lastName") var claimantlastname: String = "",
    @SerializedName("address") var address: String = "",
    @SerializedName("claimId") var claimId: Int = 0,
    @SerializedName("claimantId") var claimantId: Int = 0,
    @SerializedName("contacts") var contacts: List<ContactsResult>,
    var address1: String = "updating..",
    var address2: String = "updating..",
    var city: String = "updating..",
    var province: String = "updating..",
    var country: String = "updating..",
    var postalCode: String = "updating.."
) {


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

    fun getfullname(): String {
        return if ((claimantfirstname + claimantlastname) == null) {
            "updating.."
        }
        else
        {

           "${claimantfirstname} ${claimantlastname}".trim()

        }
    }

    data class ContactsResult(
        @SerializedName("contactType") var claimantcontactType: String,
        @SerializedName("contactDetails") var contactDetails: String
    ) {

    }

    override fun toString(): String {
        return claimantfirstname + claimantlastname
    }

    fun onNavigatetoGoogleMaps(view: View, lossLocation: String) {

        var fullformattedaddress = ""
        if (address1!=null) {
            fullformattedaddress = "${fullformattedaddress}${address1}\n"
        }
        if (address2!=null) {
            fullformattedaddress = "${fullformattedaddress}${address2}\n"
        }
        val url =
            "http://maps.google.co.in/maps?q=${lossLocation}"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        view.context.startActivity(intent)
    }

    public fun retrivephonenumebrformat(phonenumeber: String): String {
//        var formattednumber:String = "+1"
//        if(phonenumeber.length==10){
//            formattednumber = "${formattednumber} ( ${phonenumeber.substring(0,3)} ) ${phonenumeber.substring(3,6)} - ${phonenumeber.substring(6,10)}"
//
//            return formattednumber
//        }else
//        {
//            return "+1"+PhoneNumberUtils.formatNumber(phonenumeber,"US")
//        }
        return "+1" + PhoneNumberUtils.formatNumber(phonenumeber, "US")
    }

    fun onPhoneClick(view: View, phonenumber: String) {
        val u = Uri.parse("tel:" + phonenumber)

        val i = Intent(Intent.ACTION_DIAL, u)

        try {
            view.context.startActivity(i)

        } catch (s: SecurityException) {


        }
    }
}