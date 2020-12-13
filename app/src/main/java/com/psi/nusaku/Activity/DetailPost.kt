package com.psi.nusaku.Activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.psi.nusaku.R
import com.squareup.picasso.Picasso

class DetailPost : AppCompatActivity() {
    lateinit var tvJudul: TextView
    lateinit var tvLokasi: TextView
    lateinit var tvDeskripsi: TextView
    lateinit var tvTanggal: TextView
    lateinit var imgDetail: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_detail_post)
        supportActionBar?.hide()

        tvJudul = findViewById(R.id.tv_detailJudul)
        tvLokasi = findViewById(R.id.tv_detailLokasi)
        tvDeskripsi = findViewById(R.id.tv_detailDeskripsi)
        tvTanggal = findViewById(R.id.tv_detailTanggal)
        imgDetail = findViewById(R.id.img_detailBudaya)

        //Get data from intent
        val judul = intent.getStringExtra("JUDUL")
        val lokasi = intent.getStringExtra("LOKASI")
        val deskripsi = intent.getStringExtra("DESKRIPSI")
        val tanggal = intent.getStringExtra("TANGGAL")
        val imageURI = intent.getStringExtra("GAMBAR")

        tvJudul.text = judul
        tvDeskripsi.text = deskripsi
        tvLokasi.text = lokasi
        tvTanggal.text = tanggal
        Picasso.get().load(imageURI).into(imgDetail)
    }
}
