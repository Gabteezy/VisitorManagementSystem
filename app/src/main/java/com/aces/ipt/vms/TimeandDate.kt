package com.aces.ipt.vms

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class TimeandDate : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeand_date)

        val currentDateTime = Calendar.getInstance().time

        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(currentDateTime)

        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val formattedTime = timeFormat.format(currentDateTime)

        val dateTextView = findViewById<TextView>(R.id.date)
        val timeTextView = findViewById<TextView>(R.id.time)

        dateTextView.text = formattedDate
        timeTextView.text = formattedTime

        val appointment = Appointment("Gabriel", 1, currentDateTime)

        val appointmentDetailsTextView = findViewById<TextView>(R.id.appointmentDetails)
        val appointmentDetails = "Name: ${appointment.name}\n" +
                "Appointment Number: ${appointment.appointmentNumber}\n" +
                "Date and Time: ${appointment.getFormattedDateTime()}"
        appointmentDetailsTextView.text = appointmentDetails
    }

    data class Appointment(val name: String, val appointmentNumber: Int, val dateTime: Date) {
        fun getFormattedDateTime(): String {
            val dateTimeFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
            return dateTimeFormat.format(dateTime)
        }
    }
}