package com.crawford.ciq.dev.ui

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.auth0.android.Auth0
import com.auth0.android.Auth0Exception
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.provider.AuthCallback
import com.auth0.android.provider.VoidCallback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.crawford.ciq.dev.R
import com.crawford.ciq.dev.ui.home.HomeActivity
import com.crawford.ciq.dev.utils.CiQSharedprefrences
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : BaseActivity() {
    private var auth0: Auth0? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        CiQSharedprefrences.init(this)
        auth0 = Auth0(
            resources.getString(R.string.com_auth0_client_id),
            resources.getString(R.string.com_auth0_domain)
        )
        auth0!!.isOIDCConformant = true


        btn_submit.setOnClickListener {
            gotoHomeScreen()
        }
    }


    fun gotoHomeScreen(){
        var networkid = ed_username.text.toString()
        CiQSharedprefrences.getAdjusterId = networkid
        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
        intent.putExtra("AdjusterID", networkid)
        startActivity(intent)
        finish()
    }
    fun auth0verfication() {
        var networkid = ed_username.text.toString()

        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
        intent.putExtra("AdjusterID", networkid)
        startActivity(intent)
        finish()

        WebAuthProvider.login(auth0!!)
            .withScheme("ciq")
            .start(this@LoginActivity, object : AuthCallback {
                override fun onFailure(dialog: Dialog) {
                    Log.d("TAG", "onFailure: ")

                }

                override fun onFailure(exception: AuthenticationException) {
                    Log.d("Failure", exception.toString())
                    //  signingErrorData.postValue(exception)
                    // Show error to user
                }

                override fun onSuccess(credentials: Credentials) {
                    Log.d("Success", credentials.idToken)
                    Log.d("access token", credentials.accessToken)
                  // logout()
                }
            })
    }

    fun logout() {
        WebAuthProvider.logout(auth0!!)
            .withScheme("ciq")
            .start(this, object : VoidCallback {
                override fun onSuccess(payload: Void) {
                    Log.d("logout successfully", payload.toString())
                }

                override fun onFailure(error: Auth0Exception) {
                    Log.d("logout error", error.toString())
                }
            })
    }

}