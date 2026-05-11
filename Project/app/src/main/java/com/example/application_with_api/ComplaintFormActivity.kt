package com.example.application_with_api

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

class ComplaintFormActivity : AppCompatActivity() {

    private lateinit var etStudentName: EditText
    private lateinit var etRollNumber: EditText
    private lateinit var etComplaintTitle: EditText
    private lateinit var spinnerCategory: Spinner
    private lateinit var spinnerPriority: Spinner
    private lateinit var etDescription: EditText
    private lateinit var btnSubmit: Button

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complaint_form)

        etStudentName = findViewById(R.id.etStudentName)
        etRollNumber = findViewById(R.id.etRollNumber)
        etComplaintTitle = findViewById(R.id.etComplaintTitle)
        spinnerCategory = findViewById(R.id.spinnerCategory)
        spinnerPriority = findViewById(R.id.spinnerPriority)
        etDescription = findViewById(R.id.etDescription)
        btnSubmit = findViewById(R.id.btnSubmit)

        setupSpinners()

        btnSubmit.setOnClickListener {
            submitComplaint()
        }
    }

    private fun setupSpinners() {
        val categories = arrayOf("IT", "Library", "Transport", "Hostel", "Accounts", "Examination", "Cafeteria", "Administration")
        val priorities = arrayOf("Low", "Medium", "High", "Urgent")

        spinnerCategory.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)
        spinnerPriority.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, priorities)
    }

    private fun submitComplaint() {
        val name = etStudentName.text.toString().trim()
        val roll = etRollNumber.text.toString().trim()
        val title = etComplaintTitle.text.toString().trim()
        val category = spinnerCategory.selectedItem.toString()
        val priority = spinnerPriority.selectedItem.toString()
        val description = etDescription.text.toString().trim()

        if (name.isEmpty() || roll.isEmpty() || title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val complaint = hashMapOf(
            "studentName" to name,
            "rollNumber" to roll,
            "title" to title,
            "category" to category,
            "priority" to priority,
            "description" to description,
            "status" to "Pending",
            "createdAt" to Timestamp.now()
        )

        btnSubmit.isEnabled = false
        db.collection("complaints")
            .add(complaint)
            .addOnSuccessListener {
                Toast.makeText(this, "Complaint submitted successfully", Toast.LENGTH_SHORT).show()
                clearForm()
                btnSubmit.isEnabled = true
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                btnSubmit.isEnabled = true
            }
    }

    private fun clearForm() {
        etStudentName.text.clear()
        etRollNumber.text.clear()
        etComplaintTitle.text.clear()
        etDescription.text.clear()
        spinnerCategory.setSelection(0)
        spinnerPriority.setSelection(0)
    }
}