package com.crawford.ciq.dev.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.crawford.ciq.dev.R


open class BaseActivity : AppCompatActivity() {
    private val CAMERA_CODE = 101
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    open fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        )

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) + ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) + ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) + ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) + ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECEIVE_SMS
            )+ ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.SEND_SMS
            )!= PackageManager.PERMISSION_GRANTED
        ) {
            makeRequest()
        } else {
            launchEvent()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.SEND_SMS,
                Manifest.permission.RECEIVE_SMS
            ),
            CAMERA_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERA_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission has been denied by user", Toast.LENGTH_LONG)
                        .show()
                } else {
                    Toast.makeText(this, "Permission has been granted by user", Toast.LENGTH_LONG)
                        .show()
                }
                launchEvent()
            }
        }
    }

    fun openlocationservice(context: Context) {
        val lm =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var gps_enabled = false
        var network_enabled = false
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: java.lang.Exception) {
        }

        if (!gps_enabled && !network_enabled) {
            context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }

    }

    private fun launchEvent() {
        openlocationservice(this)
        val eventintent = Intent(resources.getString(R.string.eventname))
        this.sendBroadcast(eventintent)
    }
}