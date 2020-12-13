package com.psi.nusaku.Activity

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.psi.nusaku.R
import com.psi.nusaku.Utils.SharedPref
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class PostingActivity : AppCompatActivity(){

    lateinit var etNamaBudaya: EditText
    lateinit var etLokasiBudaya: EditText
    lateinit var etDeskripsiBudaya: EditText
    lateinit var imgShowUpload: ImageView
    lateinit var btnUploadImage: Button
    lateinit var btnSubmit: Button

    lateinit var db : FirebaseFirestore
    lateinit var preferences: SharedPref
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    var imageURI =""
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    val sdf = SimpleDateFormat("dd/MMM/yyyy")


    override fun  onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_form_post)
        supportActionBar?.hide()
        preferences = SharedPref(this)

        etNamaBudaya = findViewById(R.id.et_nama_budaya)
        etLokasiBudaya = findViewById(R.id.et_lokasi_budaya)
        etDeskripsiBudaya = findViewById(R.id.et_textarea)
        btnSubmit = findViewById(R.id.btn_postBudaya)
        btnUploadImage = findViewById(R.id.btn_upload_img)
        imgShowUpload = findViewById(R.id.img_showUpload)

        db = FirebaseFirestore.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        btnUploadImage.setOnClickListener(){
            launchGallery()
        }

        btnSubmit.setOnClickListener(){
            var namaBudaya = etNamaBudaya.text.toString()
            var lokasi = etLokasiBudaya.text.toString()
            //gambar
            var deskripsi = etDeskripsiBudaya.text.toString()
            if(namaBudaya!=null && lokasi!=null && deskripsi!=null){
                tambahBarang(namaBudaya,lokasi,deskripsi)
            }else{
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

    private fun uploadImage(){
        if(filePath != null){
            val ref = storageReference?.child("uploads/" + UUID.randomUUID().toString())
            val uploadTask = ref?.putFile(filePath!!)
            Log.i("Image","Uploading")

            uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                        Log.e("Image","Error, $it")
                    }
                }
                Log.i("Image","Uploading Here")
                return@Continuation ref.downloadUrl
            })?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    imageURI = downloadUri.toString()
                    Log.i("Image", "${downloadUri.toString()}")
                } else {
                    Log.e("Image", "Gagal upload")
                }
            }?.addOnFailureListener{
                Log.e("Image", "Error, $it")
            }
        }else{
            Toast.makeText(this, "Please Upload an Image", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }
            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                imgShowUpload.setImageBitmap(bitmap)
                uploadImage()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun tambahBarang(namaBudaya:String,lokasi:String,deskripsi:String){
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
        db.collection("posts").add(newPost)
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
