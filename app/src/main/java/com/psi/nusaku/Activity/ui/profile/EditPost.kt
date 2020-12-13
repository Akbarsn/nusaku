package com.psi.nusaku.Activity.ui.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.psi.nusaku.Activity.DashboardActivity
import com.psi.nusaku.R
import com.psi.nusaku.Utils.SharedPref
import com.squareup.picasso.Picasso
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class EditPost : AppCompatActivity() {
    lateinit var etJudul: TextView
    lateinit var etLokasi: TextView
    lateinit var etDeskripsi: TextView
    lateinit var imgEdit: ImageView
    lateinit var btnUploadImage: Button
    lateinit var btnSubmit: Button

    lateinit var db: FirebaseFirestore
    lateinit var preferences: SharedPref
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    var imageURI = ""
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    val sdf = SimpleDateFormat("dd/MMM/yyyy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_edit_post)
        supportActionBar?.hide()

        etJudul = findViewById(R.id.et_editJudul)
        etLokasi = findViewById(R.id.et_editLokasi)
        etDeskripsi = findViewById(R.id.et_editDeskripsi)
        imgEdit = findViewById(R.id.img_editUpload)
        btnUploadImage = findViewById(R.id.btn_editImg)
        btnSubmit = findViewById(R.id.btn_editBudaya)

        val judul = intent.getStringExtra("JUDUL")
        val lokasi = intent.getStringExtra("LOKASI")
        val deskripsi = intent.getStringExtra("DESKRIPSI")
        val imageURI = intent.getStringExtra("GAMBAR")

        etJudul.setText(judul)
        etLokasi.setText(lokasi)
        etDeskripsi.setText(deskripsi)
        Picasso.get().load(imageURI).into(imgEdit)

        preferences = SharedPref(this)
        db = FirebaseFirestore.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        btnUploadImage.setOnClickListener() {
            launchGallery()
        }

        btnSubmit.setOnClickListener() {
            var namaBudaya = etJudul.text.toString()
            var lokasi = etLokasi.text.toString()
            var deskripsi = etDeskripsi.text.toString()
            if (namaBudaya != null && lokasi != null && deskripsi != null) {
                Log.i("Post", "Test")
                updateBarang(namaBudaya, lokasi, deskripsi, judul!!)
            } else {
                Log.e("Post", "Data is not valid")
            }
        }
    }

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    private fun uploadImage() {
        if (filePath != null) {
            val ref = storageReference?.child("uploads/" + UUID.randomUUID().toString())
            val uploadTask = ref?.putFile(filePath!!)
            Log.i("Image", "Uploading")

            uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                        Log.e("Image", "Error, $it")
                    }
                }
                Log.i("Image", "Uploading Here")
                return@Continuation ref.downloadUrl
            })?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    imageURI = downloadUri.toString()
                } else {
                    Log.e("Image", "Gagal upload")
                }
            }?.addOnFailureListener {
                Log.e("Image", "Error, $it")
            }
        } else {
            Toast.makeText(this, "Please Upload an Image", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data == null || data.data == null) {
                return
            }
            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                imgEdit.setImageBitmap(bitmap)
                uploadImage()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun updateBarang(namaBudaya: String, lokasi: String, deskripsi: String, judulAwal: String) {
        var penulis = preferences.GetUser().username
        var tanggal = sdf.format(Date()).toString()
        val newPost = hashMapOf(
            "judul" to namaBudaya,
            "tempat" to lokasi,
            "deskripsi" to deskripsi,
            "gambar" to imageURI,
            "tanggal" to tanggal,
            "penulis" to penulis
        )
        db.collection("posts")
            .whereEqualTo("judul", judulAwal)
            .get()
            .addOnSuccessListener {
                it.forEach { doc ->
                    doc.reference.update(
                        "judul",
                        namaBudaya,
                        "tempat",
                        lokasi,
                        "deskripsi",
                        deskripsi,
                        "gambar",
                        imageURI,
                        "tanggal",
                        tanggal,
                    ).addOnCompleteListener {
                        Log.i("Post", "")
                    }.addOnFailureListener { error ->
                        Log.e("Posting", "Posting gagal dihapus,${error.message}")
                    }
                }
            }
            .addOnFailureListener { error ->
                Log.e("Posting", "Posting gagal dihapus,${error.message}")
            }
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }


}