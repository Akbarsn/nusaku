package com.psi.nusaku.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.psi.nusaku.R
import com.psi.nusaku.Utils.SharedPref
import java.text.SimpleDateFormat
import java.util.*

class PostingActivity : AppCompatActivity(){

    lateinit var db : FirebaseFirestore
    lateinit var btnSubmit : Button
    lateinit var btnTambahGambar :Button
    lateinit var btnpostingKembali : ImageButton
    lateinit var preferences: SharedPref
    //gambar
    lateinit var etnamaBudaya : EditText
    lateinit var etlokasi : EditText
    lateinit var etdeskripsi :EditText
    val sdf = SimpleDateFormat("dd/MMM/yyyy")

    override fun  onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posting)
        btnSubmit = findViewById(R.id.btn_submit)
        btnTambahGambar = findViewById(R.id.btn_tambahGambar)
        preferences = SharedPref(this)
        //gambar
        etnamaBudaya = findViewById(R.id.et_inputNamaBudaya)
        etlokasi = findViewById(R.id.et_inputLokasi)
        etdeskripsi = findViewById(R.id.et_deskripsiBudaya)
        db = FirebaseFirestore.getInstance()
        btnpostingKembali = findViewById(R.id.btn_postingKembali)
        btnpostingKembali.setOnClickListener(){
            startActivity(Intent(this, DashboardActivity::class.java))
        }
        btnTambahGambar.setOnClickListener(){
            val intent = Intent()
            intent.action = Intent.ACTION_PICK
            intent.type = "image/*"
            startActivity(intent)
        }
        btnSubmit.setOnClickListener(){
            var namaBudaya = etnamaBudaya.text.toString()
            var lokasi = etlokasi.text.toString()
            //gambar
            var deskripsi = etdeskripsi.text.toString()
            if(namaBudaya!=null && lokasi!=null && deskripsi!=null){
                tambahBarang(namaBudaya,lokasi,deskripsi)
            }else{

            }
        }
    }
    fun tambahBarang(namaBudaya:String,lokasi:String,deskripsi:String){
        var penulis = preferences.GetUser().username
        var tanggal = sdf.format(Date()).toString()
        val newPost = hashMapOf(
            //gambar
            "judul" to namaBudaya,
            "tempat" to lokasi,
            "deskripsi" to deskripsi,
            "tanggal" to tanggal,
            "penulis" to penulis
        )
        db.collection("Posting").add(newPost)
            .addOnSuccessListener {documentReference ->
                Log.i("Posting","Postingan berhasil disimpan, tersimpan dengan id ${documentReference.id}")
            }
            .addOnFailureListener{ error ->
                Log.e("Error","Postingan gagal disimpan ${error.message}")
            }
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }
}
