package com.example.application_with_api

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ComplaintListActivity : AppCompatActivity() {

    private lateinit var rvComplaints: RecyclerView
    private lateinit var tvEmpty: TextView
    private lateinit var fabAdd: FloatingActionButton
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complaint_list)

        rvComplaints = findViewById(R.id.rvComplaints)
        tvEmpty = findViewById(R.id.tvEmpty)
        fabAdd = findViewById(R.id.fabAdd)

        rvComplaints.layoutManager = LinearLayoutManager(this)

        fabAdd.setOnClickListener {
            startActivity(Intent(this, ComplaintFormActivity::class.java))
        }

        fetchComplaints()
    }

    private fun fetchComplaints() {
        db.collection("complaints")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                val complaintList = mutableListOf<Complaint>()
                if (snapshots != null) {
                    for (doc in snapshots) {
                        val complaint = doc.toObject(Complaint::class.java).copy(id = doc.id)
                        complaintList.add(complaint)
                    }
                }

                if (complaintList.isEmpty()) {
                    tvEmpty.visibility = View.VISIBLE
                    rvComplaints.visibility = View.GONE
                } else {
                    tvEmpty.visibility = View.GONE
                    rvComplaints.visibility = View.VISIBLE
                    rvComplaints.adapter = ComplaintAdapter(complaintList)
                }
            }
    }
}