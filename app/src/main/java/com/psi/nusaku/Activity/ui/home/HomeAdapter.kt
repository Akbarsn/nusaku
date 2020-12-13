package com.psi.nusaku.Activity.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.psi.nusaku.Activity.DetailPost
import com.psi.nusaku.Model.Posting
import com.psi.nusaku.R
import com.squareup.picasso.Picasso

class HomeAdapter (var postListItems: List<Posting>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class postViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(postModel: Posting) {
            val context = itemView.context
            itemView.findViewById<CardView>(R.id.item_feedPost).setOnClickListener {
                val intent = Intent(context, DetailPost::class.java)
                intent.putExtra("JUDUL", postModel.judul)
                intent.putExtra("LOKASI", postModel.tempat)
                intent.putExtra("DESKRIPSI", postModel.deskripsi)
                intent.putExtra("TANGGAL", postModel.tanggal)
                intent.putExtra("GAMBAR", postModel.gambar)
                context.startActivity(intent)
            }
            val imageView = itemView.findViewById<ImageView>(R.id.img_itemBudaya)
            val imageURI = postModel.gambar
            Picasso.get().load(imageURI).into(imageView)
            itemView.findViewById<TextView>(R.id.tv_judulBudaya).text = postModel.judul
            itemView.findViewById<TextView>(R.id.tv_lokasiBudaya).text = postModel.tempat
            itemView.findViewById<TextView>(R.id.tv_tanggalBudaya).text = postModel.tanggal
            itemView.findViewById<TextView>(R.id.tv_penulisBudaya).text = postModel.penulis
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return postViewHolder(view)
    }

    override fun getItemCount(): Int {
        return postListItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as postViewHolder).bind(postListItems[position])
    }

}