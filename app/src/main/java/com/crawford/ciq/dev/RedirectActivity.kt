package com.crawford.ciq.dev

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.provider.AuthCallback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials


class RedirectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_redirect)


//Declare the callback that will receive the result

//Declare the callback that will receive the result
        val authCallback: AuthCallback = object : AuthCallback {
            override fun onFailure(dialog: Dialog) {
                //failed with a dialog
            }

            override fun onFailure(exception: AuthenticationException?) {
                //failed with an exception
            }

            override fun onSuccess(credentials: Credentials) {
                //succeeded!
            }
        }
        val account = Auth0("vwdchqbrt14ryg2YtElVL0ftGWNP3r7c", "authdev.crawco.com")
        WebAuthProvider.login(account)
            .start(this@RedirectActivity, authCallback)
    }
}