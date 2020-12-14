package com.psi.nusaku.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.psi.nusaku.R
import com.psi.nusaku.Utils.SharedPref

class RegisterActivity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    lateinit var etEmail: EditText;
    lateinit var etFullName: EditText;
    lateinit var etPassword: EditText;
    lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()

        etEmail = findViewById(R.id.et_RinputEmail)
        etFullName = findViewById(R.id.et_RinputFullName)
        etPassword = findViewById(R.id.et_RinputPassword)
        btnRegister = findViewById(R.id.btn_register)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        btnRegister.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            val fullName = etFullName.text.toString()

            mAuth!!.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Toast.makeText(this, "Registrasi Gagal", Toast.LENGTH_SHORT).show()
                        return@OnCompleteListener
                    } else {
                        val newUser = hashMapOf(
                            "fullName" to fullName,
                            "email" to email,
                            "password" to password
                        )

                        db.collection("user").add(newUser)
                            .addOnSuccessListener { documentReference ->
                                Log.i(
                                    "register",
                                    "Registrasi berhasil, tersimpan dengan ${documentReference.id}"
                                )
                                Toast.makeText(this, "Registrasi Berhasil", Toast.LENGTH_SHORT).show()

                            }
                            .addOnFailureListener { error ->
                                Log.e("register", "Registrasi gagal, ${error.message}")
                                Toast.makeText(this, "Registrasi Berhasil", Toast.LENGTH_SHORT).show()
                            }

                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
                })
                .addOnFailureListener { exception -> Log.e("Error", exception.message.toString()) }
        }
    }
}