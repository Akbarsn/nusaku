package com.psi.nusaku.Activity.ui.profile

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.psi.nusaku.Activity.DetailPost
import com.psi.nusaku.Model.Posting
import com.psi.nusaku.R

class ProfileAdapter  (var postListItems: List<Posting>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class postViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(postModel: Posting) {
            val db = FirebaseFirestore.getInstance()
            val context = itemView.context
            itemView.findViewById<Button>(R.id.btn_edit).setOnClickListener {
                val intent = Intent(context, EditPost::class.java)
                intent.putExtra("JUDUL", postModel.judul)
                intent.putExtra("LOKASI", postModel.tempat)
                intent.putExtra("DESKRIPSI", postModel.deskripsi)
                intent.putExtra("TANGGAL", postModel.tanggal)
                intent.putExtra("GAMBAR", postModel.gambar)
                context.startActivity(intent)
            }
            itemView.findViewById<Button>(R.id.btn_delete).setOnClickListener {
                db.collection("posts")
                    .whereEqualTo("judul", postModel.judul)
                    .get()
                    .addOnSuccessListener{
                        it.forEach {
                            doc ->
                            doc.reference.delete()
                            Log.i("Post", "Postingan berhasil dihapus")
                        }
                    }
                    .addOnFailureListener{ error->
                        Log.e("Posting","Posting gagal dihapus,${error.message}")}
            }
            itemView.findViewById<TextView>(R.id.tv_profileJudul).text = postModel.judul
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sejarahposting, parent, false)
        return postViewHolder(view)
    }

    override fun getItemCount(): Int {
        return postListItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as postViewHolder).bind(postListItems[position])
    }

}