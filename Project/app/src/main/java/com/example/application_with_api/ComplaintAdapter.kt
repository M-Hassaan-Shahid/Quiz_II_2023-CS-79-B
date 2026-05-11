package com.example.application_with_api

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ComplaintAdapter(private val complaints: List<Complaint>) :
    RecyclerView.Adapter<ComplaintAdapter.ComplaintViewHolder>() {

    class ComplaintViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvStudentInfo: TextView = view.findViewById(R.id.tvStudentInfo)
        val tvCategory: TextView = view.findViewById(R.id.tvCategory)
        val tvPriority: TextView = view.findViewById(R.id.tvPriority)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplaintViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_complaint, parent, false)
        return ComplaintViewHolder(view)
    }

    override fun onBindViewHolder(holder: ComplaintViewHolder, position: Int) {
        val complaint = complaints[position]
        holder.tvTitle.text = complaint.title
        holder.tvStudentInfo.text = "${complaint.studentName} (${complaint.rollNumber})"
        holder.tvCategory.text = "Category: ${complaint.category}"
        holder.tvPriority.text = "Priority: ${complaint.priority}"

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ComplaintDetailActivity::class.java)
            intent.putExtra("complaint", complaint)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = complaints.size
}