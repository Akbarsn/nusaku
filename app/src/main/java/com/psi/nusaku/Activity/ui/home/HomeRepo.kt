package com.psi.nusaku.Activity.ui.home

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

class HomeRepo {
    private val firebaseFirestore : FirebaseFirestore = FirebaseFirestore.getInstance()

    fun getPostlist(): Task<QuerySnapshot> {
        return firebaseFirestore.collection("posts")
            .orderBy("tanggal", Query.Direction.DESCENDING)
            .get()
    }
}