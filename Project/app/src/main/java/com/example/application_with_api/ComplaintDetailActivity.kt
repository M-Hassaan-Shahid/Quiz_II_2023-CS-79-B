package com.example.application_with_api

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Locale

class ComplaintDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complaint_detail)

        val complaint = intent.getSerializableExtra("complaint") as? Complaint

        if (complaint != null) {
            findViewById<TextView>(R.id.tvDetailTitle).text = complaint.title
            findViewById<TextView>(R.id.tvDetailStatus).text = "Status: ${complaint.status}"
            findViewById<TextView>(R.id.tvDetailStudent).text = "By: ${complaint.studentName} (${complaint.rollNumber})"
            findViewById<TextView>(R.id.tvDetailCategory).text = "Category: ${complaint.category}"
            findViewById<TextView>(R.id.tvDetailPriority).text = "Priority: ${complaint.priority}"
            findViewById<TextView>(R.id.tvDetailDescription).text = complaint.description

            val date = complaint.createdAt?.toDate()
            if (date != null) {
                val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
                findViewById<TextView>(R.id.tvDetailDate).text = "Date: ${sdf.format(date)}"
            } else {
                findViewById<TextView>(R.id.tvDetailDate).text = "Date: N/A"
            }
        }

        findViewById<Button>(R.id.btnBack).setOnClickListener {
            finish()
        }
    }
}