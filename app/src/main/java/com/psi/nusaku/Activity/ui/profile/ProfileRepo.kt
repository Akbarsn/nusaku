package com.psi.nusaku.Activity.ui.profile

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

class ProfileRepo {
    private val firebaseFirestore : FirebaseFirestore = FirebaseFirestore.getInstance()

    fun getPostlist(penulis: String): Task<QuerySnapshot> {
        return firebaseFirestore.collection("posts")
            .whereEqualTo("penulis",penulis)
            .get()
    }
}