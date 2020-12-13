package com.crawford.ciq.dev.model

import android.content.Intent
import android.net.Uri
import android.view.View
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MyClaimResult(@SerializedName("claimId") var filenumber: Int = 0,
                         @SerializedName("receiveDate") var receiveDate: String = "",
                         @SerializedName("leadAdjuster") var leadAdjuster: String = "",
                         @SerializedName("catastrophicLossCode") var catastrophicLossCode: String = "",
                         @SerializedName("claimType") var claimType: String = "",
                         @SerializedName("claimTypeCode") var claimTypeCode: String = "",
                         @SerializedName("lossLocation") var lossLocation: String = "",
                         @SerializedName("lossLocationCity") var lossLocationCity: String = "",
                         @SerializedName("lossLocationProvince") var lossLocationProvince: String = "",
                         @SerializedName("insuredName") var insuredName: String = "",
                         @SerializedName("organizationDescription") var organizationCode: String = "",
                         @SerializedName("claimant1Name") var claimant1: String = "",
                         @SerializedName("claimant1Phone") var claimant1Phone: String = "",
                         @SerializedName("claimant2Name") var claimant2: String = "",
                         @SerializedName("claimant2Phone") var claimant2Phone: String = "",
                         @SerializedName("claimant3Name") var claimant3: String = "",
                         @SerializedName("claimant3Phone") var claimant3Phone: String = "",
                         @SerializedName("claimant4Name") var claimant4: String = "",
                         @SerializedName("claimant4Phone") var claimant4Phone: String = "",
                         @SerializedName("claimant5Name") var claimant5: String = "",
                         @SerializedName("claimant5Phone") var claimant5Phone: String = "",
                         @SerializedName("fcCreatedDate") var fcCreatedDate: String = "",
                         @SerializedName("fvCreatedDate") var fvCreatedDate: String = "",
                         @SerializedName("erCreatedDate") var erCreatedDate: String = "",
                         @SerializedName("frCreatedDate") var frCreatedDate: String = "",
                         @SerializedName("isAssist") var isAssist: Boolean = false,
                         var alertstatus:String="",
                         var distance: Int = 0,
//                         var distance: Int = (10..1000).random(),
                         var claimstatus:String = ""):Serializable{

    fun onNavigatetoGoogleMaps(view : View, lossLocation:String){
        val url =
            "http://maps.google.co.in/maps?q=${lossLocation}"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        view.context.startActivity(intent)
    }
}