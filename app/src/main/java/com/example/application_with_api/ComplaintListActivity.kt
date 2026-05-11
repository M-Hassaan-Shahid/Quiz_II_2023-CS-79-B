package com.example.application_with_api

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.application_with_api.databinding.ActivityComplaintListBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ComplaintListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityComplaintListBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var adapter: ComplaintAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComplaintListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ComplaintAdapter(emptyList()) { complaintId ->
            val intent = Intent(this, ComplaintDetailActivity::class.java)
            intent.putExtra("complaintId", complaintId)
            startActivity(intent)
        }
        binding.recyclerView.adapter = adapter

        fetchComplaints()
    }

    private fun fetchComplaints() {
        try {
            db.collection("complaints")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener(this) { snapshot, e ->
                    if (e != null) {
                        Toast.makeText(this, "Error fetching data: ${e.message}", Toast.LENGTH_LONG).show()
                        return@addSnapshotListener
                    }

                    if (snapshot != null) {
                        val complaintList = snapshot.documents.mapNotNull { it.toObject(Complaint::class.java) }
                        if (complaintList.isEmpty()) {
                            binding.recyclerView.visibility = View.GONE
                            binding.emptyView.visibility = View.VISIBLE
                        } else {
                            binding.recyclerView.visibility = View.VISIBLE
                            binding.emptyView.visibility = View.GONE
                            adapter.updateData(complaintList)
                        }
                    }
                }
        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}
