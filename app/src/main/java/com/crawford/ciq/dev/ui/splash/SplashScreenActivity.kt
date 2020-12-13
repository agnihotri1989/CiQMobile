package com.crawford.ciq.dev.ui.splash

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.jwt.Claim
import com.auth0.android.jwt.JWT
import com.auth0.android.provider.AuthCallback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.crawford.ciq.dev.R
import com.crawford.ciq.dev.ViewModelFactory
import com.crawford.ciq.dev.api.ApiHelper
import com.crawford.ciq.dev.api.RetrofitBuilder
import com.crawford.ciq.dev.model.UserResult
import com.crawford.ciq.dev.ui.BaseActivity
import com.crawford.ciq.dev.ui.home.HomeActivity
import com.crawford.ciq.dev.utils.CiQSharedprefrences
import com.crawford.ciq.dev.utils.Status
import kotlinx.android.synthetic.main.activity_splash_screen.*
import org.jetbrains.anko.selector
import org.jetbrains.anko.toast


class SplashScreenActivity : BaseActivity() {
    private var auth0: Auth0? = null
    private lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val locale: String =
            getResources().getConfiguration().locale.getDisplayCountry()
        Log.d("localee>>>", locale)
        CiQSharedprefrences.init(this)
        auth0 = Auth0(
            resources.getString(R.string.com_auth0_client_id),
            resources.getString(R.string.com_auth0_domain)
        )
        auth0!!.isOIDCConformant = true
        setUpViewModel()
        registerEvent()
        setupPermissions()
    }

    fun setUpViewModel() {
        viewModel =
            ViewModelProviders.of(
                this,
                ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
            )
                .get(SplashViewModel::class.java)
    }

    fun setUpObservers(netwrokid: String) {
        viewModel.initContext(this)
        viewModel.getAdjusterId(netwrokid).observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { users ->
                            users.observe(
                                this,
                                Observer { users -> retrieveadjusterid(users) })

                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(this@SplashScreenActivity, it.message, Toast.LENGTH_LONG)
                            .show()
                    }
                    Status.LOADING -> {

                    }
                }
            }
        })
    }

    private fun retrieveadjusterid(users: List<UserResult>) {
        progressBar.visibility = View.GONE
        if (users.size == 0) {
            CiQSharedprefrences.getAdjusterId = "DRMONT"
        } else {
            users.forEach {
                CiQSharedprefrences.getAdjusterId = it.id
            }
        }

        Handler().postDelayed({
            startActivity(Intent(this@SplashScreenActivity, HomeActivity::class.java))
            finish()
        }, 3000)
    }

    private fun registerEvent() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(resources.getString(R.string.eventname))
        registerReceiver(eventReceiver, intentFilter)

    }


    private val eventReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
       //  auth0verfication()
openDialog()
        }
    }

    override fun onDestroy() {
        unregisterReceiver(eventReceiver)
        super.onDestroy()

    }


    fun auth0verfication() {
        runOnUiThread {
            progressBar.visibility = View.VISIBLE
        }
        WebAuthProvider.login(auth0!!)
            .withScheme("ciq")
            .start(this@SplashScreenActivity, object : AuthCallback {
                override fun onFailure(dialog: Dialog) {
                    Log.d("TAG", "onFailure: ")
                    runOnUiThread {
                        progressBar.visibility = View.GONE
                    }
                }

                override fun onFailure(exception: AuthenticationException) {
                    Log.d("Failure", exception.toString())
                    //  signingErrorData.postValue(exception)
                    // Show error to user
                    runOnUiThread {
                        progressBar.visibility = View.GONE
                    }
                }

                override fun onSuccess(credentials: Credentials) {
                    Log.d("access_token", credentials.idToken)

                    getnetworkid(credentials.idToken!!)
                    Log.d("success", credentials.accessToken)
                    // logout()
                }
            })
    }

    fun getnetworkid(access_token: String) {
//        val token =
//            "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Ik0wVTNPRGt3UkVWR1JFTkdPRVpETkRSRFJUQXpOekl3UlVFNFF6SkJOekZFTVRnNE9ETXlNQSJ9.eyJodHRwOi8vY3Jhd2NvLmNvbS91c2VyaWQiOiJLS0FHTkkiLCJpc3MiOiJodHRwczovL2F1dGgtZGV2LmNyYXdjby5jb20vIiwic3ViIjoic2FtbHB8c3RzZGV2LWNyYXdjby1jb20tU0FNTFB8S3NoaXRpei5BZ25paG90cmlAdXMuY3Jhd2NvLmNvbSIsImF1ZCI6InZ3ZGNocWJydDE0cnlnMll0RWxWTDBmdEdXTlAzcjdjIiwiaWF0IjoxNjA1MTAyODM2LCJleHAiOjE2MDUxMzg4MzYsIm5vbmNlIjoiOWc4bl8wNHJwWmp3eEhCZ2FZSDE0MHBRNnlQV0E3UkFYR29YNExaNVQ0QSJ9.xQKkBRMBhq_P385ppfS6lQO347L5Y6sS-L2tqiMlRa8HsfewTVu6bL6bT87mqNfjV_sNrr9zffyuo4vnOEq847j7R_2s1ZWtCWjuQKTfGinhhvoRVxnRK9OUIDJFKshOi-YflNJ2Q1QaTqfpKhfi0y6zpwBT9jLsAoZtuivsvMjU-uk2KNAxs4_sBSmLPsQfbhuRJG0PouDh3SEwWkPEV-QQvYZzZUwnRUo7ARbhd60jpsVozocFcSJrmJQHBzPS3XICufQKEJc22Q2twbQiTJTIxKQp-yEwDxhgSHwUx20qC9lckiW4l3zXesIjbcCRp4OM0ZeQ2YG0ZezV_qr0Tw"
//


        val jwt = JWT(access_token)
        val subscriptionMetaData: Claim = jwt.getClaim("http://crawco.com/userid")
        var networkid = subscriptionMetaData.asString()
        //   var networkid = "JASALI"
        Log.d("parsedValue", networkid)

        if (checkfornetworkId(networkid!!)) {

            runOnUiThread {
                setUpObservers(networkid!!)
            }

        } else {
            runOnUiThread { openDialog() }

        }

    }

    fun checkfornetworkId(networkId: String): Boolean {

        val netwrokarray = arrayListOf<String>(
            "DLLEEE",
            "EEPORT",
            "DAREDN",
            "JALILL",
            "DJSTAN",
            "DDROMA",
            "AAHARA",
            "JMARAU"
        )

        return netwrokarray.contains(networkId)
    }


    fun openDialog() {
        val fakeadjusterslist = listOf(
            "ADJUSTER 1", "ADJUSTER 2", "ADJUSTER 3",
            "ADJUSTER 4"
        )
        val adjusterslist = listOf(
            "DRMONT","MMFICH","RESTEW", "CCANDE"

        )
        runOnUiThread {
            progressBar.visibility = View.GONE
        }
        selector("Choose a Field Adjuster", fakeadjusterslist, { dialogInterface, i ->
            toast("You have Selected ${fakeadjusterslist[i]}, Please wait while we redirect")

            CiQSharedprefrences.getAdjusterId = adjusterslist[i]
            Handler().postDelayed({
                startActivity(Intent(this@SplashScreenActivity, HomeActivity::class.java))
                finish()
            }, 3000)
        })


    }

}