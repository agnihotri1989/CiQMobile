package com.crawford.ciq.dev.utils

import android.content.Context
import android.location.*
import com.google.android.gms.maps.model.LatLng
import java.io.IOException

class AddresstoLatlong(val context: Context) {
    val maddress = "320      E  150TH         ST, BRONX, NY, 10451, US"

    suspend fun getLocationFromAddress(
        strAddress: String?
    ): LatLng? {



        val coder = Geocoder(context)
        val address: List<Address>?
        var p1: LatLng? = null
        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 1)
            if (address == null) {
                return null
            }
            address.forEach {
                p1 = LatLng(it.getLatitude(), it.getLongitude())
            }
//            if (address.size > 0) {
//                val location: Address = address[0]
//                p1 = LatLng(location.getLatitude(), location.getLongitude())
//            }


        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return p1
    }

    fun getdistance(lat1:Double,lon1:Double,lat2:Double,lon2:Double):Int{
        val loc1 = Location("")
        loc1.latitude = lat1
        loc1.longitude = lon1

        val loc2 = Location("")
        loc2.latitude = lat2
        loc2.longitude = lon2

        val distanceInMiles = loc1.distanceTo(loc2)/1600
        return distanceInMiles.toInt()
    }





}