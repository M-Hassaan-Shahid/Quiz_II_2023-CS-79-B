package com.example.application_with_api

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.application_with_api.databinding.ComplaintCardBinding

class ComplaintAdapter(
    private var complaints: List<Complaint>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<ComplaintAdapter.ComplaintViewHolder>() {

    inner class ComplaintViewHolder(val binding: ComplaintCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(complaint: Complaint) {
            binding.tvCardTitle.text = complaint.complaintTitle
            binding.tvCardStudentName.text = complaint.studentName
            binding.tvCardRollNumber.text = "(${complaint.rollNumber})"
            binding.tvCardCategory.text = "Category: ${complaint.category}"
            binding.chipPriority.text = complaint.priority

            val priorityColor = when (complaint.priority) {
                "Low" -> Color.parseColor("#4CAF50") // Green
                "Medium" -> Color.parseColor("#2196F3") // Blue
                "High" -> Color.parseColor("#FF9800") // Orange
                "Urgent" -> Color.parseColor("#F44336") // Red
                else -> Color.GRAY
            }
            binding.chipPriority.chipBackgroundColor = ColorStateList.valueOf(priorityColor)

            binding.root.setOnClickListener {
                onItemClick(complaint.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplaintViewHolder {
        val binding = ComplaintCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ComplaintViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ComplaintViewHolder, position: Int) {
        holder.bind(complaints[position])
    }

    override fun getItemCount(): Int = complaints.size

    fun updateData(newList: List<Complaint>) {
        val diffCallback = object : androidx.recyclerview.widget.DiffUtil.Callback() {
            override fun getOldListSize() = complaints.size
            override fun getNewListSize() = newList.size
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return complaints[oldItemPosition].id == newList[newItemPosition].id
            }
            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return complaints[oldItemPosition] == newList[newItemPosition]
            }
        }
        val diffResult = androidx.recyclerview.widget.DiffUtil.calculateDiff(diffCallback)
        complaints = newList
        diffResult.dispatchUpdatesTo(this)
    }
}
