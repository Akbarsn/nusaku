package com.psi.nusaku.Activity.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.psi.nusaku.Model.Posting
import com.psi.nusaku.R

class DashboardAdapter(var postListItems : List<Posting>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val firebaseFirestore = FirebaseFirestore.getInstance()
    class postViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(postModel : Posting){
            // Gambar
            itemView.findViewById<TextView>(R.id.txt_namaBudaya).text = postModel.judul
            itemView.findViewById<TextView>(R.id.txt_lokasiBudaya).text = postModel.tempat
            itemView.findViewById<TextView>(R.id.txt_tanggal).text = postModel.tanggal
            itemView.findViewById<TextView>(R.id.txt_username).text = postModel.penulis
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.posting,parent,false)
        return postViewHolder(view)
    }

    override fun getItemCount(): Int {
        return postListItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as postViewHolder).bind(postListItems[position])
    }

}