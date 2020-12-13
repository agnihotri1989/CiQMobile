package com.crawford.ciq.dev.utils

import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.widget.TextView
import java.util.*


class TimePickerUtils(val context: Context) {


    fun settime(view : TextView){
        val mcurrentTime: Calendar = Calendar.getInstance()
        var hour: Int = mcurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute: Int = mcurrentTime.get(Calendar.MINUTE)
        val second:Int = mcurrentTime.get(Calendar.SECOND)
        val mTimePicker: TimePickerDialog
        mTimePicker = TimePickerDialog(context,
            OnTimeSetListener { timePicker, selectedHour, selectedMinute->
                var timeSet = ""
                var hourofday = selectedHour
                if (hourofday > 12) {
                    hourofday -= 12
                    timeSet = "PM"
                } else if (hourofday === 0) {
                    hourofday += 12
                    timeSet = "AM"
                } else if (hourofday === 12) {
                    timeSet = "PM"
                } else {
                    timeSet = "AM"
                }

                var min: String? = ""
                if (selectedMinute < 10) min = "0$selectedMinute" else min = java.lang.String.valueOf(selectedMinute)

                // Append in a StringBuilder

                // Append in a StringBuilder
                val zeros ="00"
                val aTime = StringBuilder().append(if(hourofday.toInt()>10) hourofday else "0${hourofday}").append(':')
                    .append(min).append(':').append(zeros).append(" ").append(timeSet).toString()


                view.setText(aTime) },
            hour,
            minute,
            false
        ) //Yes 24 hour time

        mTimePicker.setTitle("Select Time")
        mTimePicker.show()
    }


}