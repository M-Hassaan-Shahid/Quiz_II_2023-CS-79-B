package com.example.application_with_api

import java.io.Serializable

data class Complaint(
    var id: String = "",
    var studentName: String = "",
    var rollNumber: String = "",
    var complaintTitle: String = "",
    var category: String = "",
    var priority: String = "",
    var description: String = "",
    var status: String = "Pending",
    var timestamp: Long = System.currentTimeMillis()
) : Serializable
