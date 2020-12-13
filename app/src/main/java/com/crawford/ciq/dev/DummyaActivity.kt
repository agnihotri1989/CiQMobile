package com.crawford.ciq.dev

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.BaseCallback
import com.auth0.android.provider.AuthCallback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import kotlinx.android.synthetic.main.activity_dummya.*


class DummyaActivity : AppCompatActivity() {
    private var auth0: Auth0? = null
    private var client: AuthenticationAPIClient? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dummya)
        daily_weekly_button_view.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            Toast.makeText(
                applicationContext, " On checked change :" +
                        " ${radio.text}",
                Toast.LENGTH_SHORT
            ).show()
        })
        auth0 = Auth0(
            resources.getString(R.string.com_auth0_client_id),
            resources.getString(R.string.com_auth0_domain)
        )
        //auth0 = Auth0(this)
        auth0!!.isOIDCConformant = true
//        auth0!!.isLoggingEnabled = true
//        doEmailLogin(
//            resources.getString(R.string.emailaddress),
//            resources.getString(R.string.password)
//        )
        WebAuthProvider.login(auth0!!)
            .withScheme("ciq")
            .start(this@DummyaActivity, object : AuthCallback {
                override fun onFailure(dialog: Dialog) {
                    Log.d("TAG", "onFailure: ")
                    // Show error Dialog to user
                }
                override fun onFailure(exception: AuthenticationException) {
                    Log.d("Failure",exception.toString())
                  //  signingErrorData.postValue(exception)
                    // Show error to user
                }
                override fun onSuccess(credentials: Credentials) {
                    Log.d("Success",credentials.idToken)
                   // signingData.postValue(credentials)
                    // Store credentials
                    // Navigate to your main activity
                }
            })
//        WebAuthProvider.login(auth0!!)
//            .withScheme("ciq")
//            .withAudience(
//                String.format(
//                    "ciq://%s/userinfo",
//                    getString(R.string.com_auth0_domain)
//                )
//            )
//            .start(this@DummyaActivity, object : AuthCallback {
//                override fun onFailure(dialog: Dialog) {
//                    // Show error Dialog to user
//                }
//
//                override fun onFailure(exception: AuthenticationException) {
//                    val er = exception.toString()
//                }
//
//                override fun onSuccess(credentials: Credentials) {
//                    val cr = credentials
//                }
//            })
//        auth0 = Auth0(this);


//        WebAuthProvider.login(auth0!!)
//            .withScheme("ciq")
//            .withAudience(
//                String.format(
//                    "https://%s/userinfo",
//                    getString(R.string.com_auth0_domain)
//                )
//            )
//            .start(this@DummyaActivity, object : AuthCallback {
//                override fun onFailure(dialog: Dialog) {
//                    // Show error Dialog to user
//                }
//
//                override fun onFailure(exception: AuthenticationException?) {
//                    // Show error to user
//                }
//
//                override fun onSuccess(credentials: Credentials) {
//                    val cr = credentials.accessToken
//                    // Store credentials
//                    // Navigate to your main activity
//                }
//            })
        //  startActivity(Intent(this, RedirectActivity::class.java))


//Declare the callback that will receive the result

//Declare the callback that will receive the result
//        val authCallback: AuthCallback = object : AuthCallback {
//            override fun onFailure(dialog: Dialog) {
//                //failed with a dialog
//            }
//
//            override fun onFailure(exception: AuthenticationException) {
//                val e = exception
//            }
//
//            override fun onSuccess(credentials: Credentials) {
//                val cr = credentials.accessToken
//            }
//        }
//
//        val account = Auth0("vwdchqbrt14ryg2YtElVL0ftGWNP3r7c", "authdev.crawco.com")
//        val authentication = AuthenticationAPIClient(account)
//        WebAuthProvider.login(account)
//            .start(this@DummyaActivity, authCallback)
//        authentication
//            .login("DRMONT", "a secret password", "stsdev-crawco-com-SAMLP")
//            .start(object : BaseCallback<Credentials?, AuthenticationException?> {
//                override fun onSuccess(payload: Credentials?) {
//                    val p = payload
//                }
//
//                override fun onFailure(error: AuthenticationException?) {
//                    val er = error
//                }
//            })
//        authentication
//            .passwordlessWithEmail(
//                "DRMONT",
//                PasswordlessType.CODE,
//                "stsdev-crawco-com-SAMLP"
//            )
//            .start(object : BaseCallback<Void?, AuthenticationException?> {
//                override fun onSuccess(payload: Void?) {
//                    val p = payload
//                }
//
//                override fun onFailure(error: AuthenticationException?) {
//                    val er = error
//                }
//            })
    }

    fun doEmailLogin(email: String, password: String) {
        val connectionName = "stsdev-crawco-com-SAMLP"
        client = AuthenticationAPIClient(auth0!!)
        client!!.login(email, password, connectionName)
            .start(object : BaseCallback<Credentials, AuthenticationException> {
                override fun onSuccess(payload: Credentials) {
                    payload.idToken
                }

                override fun onFailure(error: AuthenticationException) {
                    Log.d("Failure>>>", error.description)
                }
            })
    }
}