package com.example.application_with_api

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.database.FirebaseDatabase

object FirebaseHelper {
    val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    val realtimeDb: FirebaseDatabase by lazy { FirebaseDatabase.getInstance() }

    // Auth Snippets
    fun signIn(email: String, pass: String, onResult: (Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task -> onResult(task.isSuccessful) }
    }

    // Firestore Snippets
    fun addDataToFirestore(collection: String, data: Map<String, Any>) {
        firestore.collection(collection).add(data)
    }

    // Realtime DB Snippets
    fun addDataToRealtimeDb(path: String, value: Any) {
        realtimeDb.getReference(path).setValue(value)
    }
}
