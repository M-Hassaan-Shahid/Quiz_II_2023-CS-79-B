package com.example.application_with_api

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.application_with_api.databinding.ActivityComplaintDetailBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ComplaintDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityComplaintDetailBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComplaintDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        val complaintId = intent.getStringExtra("complaintId")

        if (complaintId == null) {
            Toast.makeText(this, "Complaint not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        fetchComplaintDetails(complaintId)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun fetchComplaintDetails(complaintId: String) {
        try {
            db.collection("complaints").document(complaintId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val complaint = document.toObject(Complaint::class.java)
                        if (complaint != null) {
                            populateUI(complaint)
                        } else {
                            Toast.makeText(this, "Error parsing complaint data", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    } else {
                        Toast.makeText(this, "Complaint document not found", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to fetch details: ${e.message}", Toast.LENGTH_LONG).show()
                    finish()
                }
        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun populateUI(complaint: Complaint) {
        binding.tvDetailTitle.text = complaint.complaintTitle
        binding.tvDetailStatus.text = complaint.status
        binding.tvDetailCategory.text = complaint.category
        binding.tvDetailStudentName.text = complaint.studentName
        binding.tvDetailRollNumber.text = complaint.rollNumber
        binding.tvDetailDescription.text = complaint.description

        binding.chipDetailPriority.text = complaint.priority
        val priorityColor = when (complaint.priority) {
            "Low" -> Color.parseColor("#4CAF50") // Green
            "Medium" -> Color.parseColor("#2196F3") // Blue
            "High" -> Color.parseColor("#FF9800") // Orange
            "Urgent" -> Color.parseColor("#F44336") // Red
            else -> Color.GRAY
        }
        binding.chipDetailPriority.chipBackgroundColor = ColorStateList.valueOf(priorityColor)

        val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        val dateString = dateFormat.format(Date(complaint.timestamp))
        binding.tvDetailDate.text = dateString
    }
}
