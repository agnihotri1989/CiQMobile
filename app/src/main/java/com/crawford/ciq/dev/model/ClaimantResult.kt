package com.crawford.ciq.dev.model

import com.google.gson.annotations.SerializedName

//data class ClaimantResult(@SerializedName("contacts") var listcontactlist:List<ContactsResult>
//                          ,@SerializedName("claimId") var claimId:Int,
//                          @SerializedName("firstName")var firstName:String,
//                          @SerializedName("lastName")var lastName:String) {


    data class ClaimantResult(var listcontactlist:List<ContactsResult>,
                              var firstName:String,
                              @SerializedName("injuryDamage") var injuryDamage:String,
                              @SerializedName("firstName") var claimantfirstname:String,
                              @SerializedName("lastName") var claimantlastname:String,
                              @SerializedName("contacts") var contacts:List<ContactsResult>) {
//    data class ContactsResult(@SerializedName("contactType") var contactType:String,
//    @SerializedName("contactDetails") var contactDetails:String){
//
//    }

//    data class ContactsResult(var contactType:String,  @SerializedName("contactType") var claimantcontactType:String,
//                              @SerializedName("contactDetails") var contactDetails:String){
//
//    }

        data class ContactsResult(var contactType:String,  @SerializedName("contactType") var claimantcontactType:String,
                                  @SerializedName("contactDetails") var contactDetails:String){

        }
}