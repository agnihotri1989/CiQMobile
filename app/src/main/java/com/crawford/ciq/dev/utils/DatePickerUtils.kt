package com.crawford.ciq.dev.utils

import android.app.DatePickerDialog
import android.content.Context
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*

class DatePickerUtils(val context: Context) {
    var cal = Calendar.getInstance()

    fun setdate(mview: View,mindate:Date) {


        val dateSetListener = object : DatePickerDialog.OnDateSetListener {


            override fun onDateSet(
                view: DatePicker, year: Int, monthOfYear: Int,
                dayOfMonth: Int
            ) {
             val currenttime =   SimpleDateFormat("HH:mm:ss a", Locale.US).format(Date())
                if (mview is TextView) {
                    mview as TextView
                    mview.text = "${monthOfYear + 1}/${dayOfMonth}/${year}"

                }
//                cal.set(Calendar.YEAR, year)
//                cal.set(Calendar.MONTH, monthOfYear)
//                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            }
        }

       val datepickdialog = DatePickerDialog(
            context,
            dateSetListener,
            // set DatePickerDialog to point to today's date when it loads up
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )

        datepickdialog.datePicker.maxDate =Date().time
        datepickdialog.datePicker.minDate =mindate.time
        datepickdialog.show()
    }

    fun updateDateInView(view: View) {
        val myFormat = "MM/dd/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        if (view is TextView) {
            view as TextView
            view.setText(sdf.format(cal.getTime()))

        }

    }
}