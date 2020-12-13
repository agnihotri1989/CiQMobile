package com.crawford.ciq.dev.utils

import android.content.Context
import android.content.SharedPreferences

object CiQSharedprefrences {
    private const val NAME = "CiQSharedprefrences"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences
    private val IS_FIRST_RUN_PREF = Pair("is_first_run", false)
    private val APP_TOKEN = Pair("app_token", "")
    private val ADJUSTERID = Pair("adjuster_id", "")

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var firstRun: Boolean
        // custom getter to get a preference of a desired type, with a predefined default value
        get() = preferences.getBoolean(IS_FIRST_RUN_PREF.first, IS_FIRST_RUN_PREF.second)
        // custom setter to save a preference back to preferences file
        set(value) = preferences.edit {
            it.putBoolean(IS_FIRST_RUN_PREF.first, value)
        }


    var getAdjusterId:String?
    get() = preferences.getString(ADJUSTERID.first,ADJUSTERID.second)
        set(value) = preferences.edit {
            it.putString(ADJUSTERID.first, value)
        }




    var gettoken: String?
        // custom getter to get a preference of a desired type, with a predefined default value
        get() = preferences.getString(APP_TOKEN.first, APP_TOKEN.second)
        // custom setter to save a preference back to preferences file
        set(value) = preferences.edit {
            it.putString(APP_TOKEN.first, value)
        }

}