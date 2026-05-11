package com.example.application_with_api

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.application_with_api.databinding.ActivityComplaintFormBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore

// For development, Firestore rules should allow read/write:
// rules_version = '2';
// service cloud.firestore {
//   match /databases/{database}/documents {
//     match /{document=**} {
//       allow read, write: if true;
//     }
//   }
// }

class ComplaintFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityComplaintFormBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComplaintFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSubmit.setOnClickListener {
            submitComplaint()
        }

        binding.btnViewComplaints.setOnClickListener {
            startActivity(Intent(this, ComplaintListActivity::class.java))
        }
    }

    private fun submitComplaint() {
        val studentName = binding.etStudentName.text?.toString()?.trim() ?: ""
        val rollNumber = binding.etRollNumber.text?.toString()?.trim() ?: ""
        val title = binding.etComplaintTitle.text?.toString()?.trim() ?: ""
        val description = binding.etDescription.text?.toString()?.trim() ?: ""
        val category = binding.spinnerCategory.selectedItem.toString()
        val priority = binding.spinnerPriority.selectedItem.toString()

        if (studentName.isEmpty() || rollNumber.isEmpty() || title.isEmpty() || description.isEmpty()) {
            Snackbar.make(binding.root, "Please fill all fields", Snackbar.LENGTH_SHORT).show()
            return
        }

        binding.btnSubmit.isEnabled = false
        binding.btnSubmit.text = "Submitting..."

        val complaintRef = db.collection("complaints").document()
        val complaint = Complaint(
            id = complaintRef.id,
            studentName = studentName,
            rollNumber = rollNumber,
            complaintTitle = title,
            category = category,
            priority = priority,
            description = description,
            status = "Pending",
            timestamp = System.currentTimeMillis()
        )

        try {
            complaintRef.set(complaint)
                .addOnSuccessListener {
                    Snackbar.make(binding.root, "Complaint submitted successfully", Snackbar.LENGTH_SHORT).show()
                    clearFields()
                    resetButton()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to submit: ${e.message}", Toast.LENGTH_LONG).show()
                    resetButton()
                }
        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            resetButton()
        }
    }

    private fun resetButton() {
        binding.btnSubmit.isEnabled = true
        binding.btnSubmit.text = "Submit Complaint"
    }

    private fun clearFields() {
        binding.etStudentName.text?.clear()
        binding.etRollNumber.text?.clear()
        binding.etComplaintTitle.text?.clear()
        binding.etDescription.text?.clear()
        binding.spinnerCategory.setSelection(0)
        binding.spinnerPriority.setSelection(0)
    }
}
