package com.psi.nusaku.Activity.ui.dashboard

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

class DashboardRepo {
    private val firebaseFirestore : FirebaseFirestore = FirebaseFirestore.getInstance()
    fun getPostlist(): Task<QuerySnapshot> {
        return firebaseFirestore.collection("Posting")
            .orderBy("Posting", Query.Direction.DESCENDING)
            .get()
    }
}