package com.psi.nusaku.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.psi.nusaku.R
import com.psi.nusaku.Utils.SharedPref

class LoginActivity : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth;
    lateinit var db: FirebaseFirestore
    lateinit var etEmail: EditText;
    lateinit var etPassword: EditText;
    lateinit var btnLogin: Button
//    lateinit var btnLoginGoogle: Button
    lateinit var btnIntentRegister: TextView
    lateinit var preferences: SharedPref
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var gso: GoogleSignInOptions
    private val RC_SIGN_IN: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        btnLogin = findViewById(R.id.btn_login)
//        btnLoginGoogle = findViewById(R.id.btn_loginWithGoogle)
        btnIntentRegister = findViewById(R.id.btn_intentRegister)
        etEmail = findViewById(R.id.et_inputEmail)
        etPassword = findViewById(R.id.et_inputPassword)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        preferences = SharedPref(this)

        btnIntentRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        btnLogin.setOnClickListener {
            Toast.makeText(this, "Sedang diproses", Toast.LENGTH_SHORT).show()
            var email = etEmail.text.toString()
            var password = etPassword.text.toString()

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Sign In Sucessful", Toast.LENGTH_SHORT)
                    onAuthSuccessLogin(email)
                } else {
                    Toast.makeText(this, "Sign In Failed", Toast.LENGTH_SHORT)
                }
            }
        }

//        btnLoginGoogle.setOnClickListener {
//            signInGoogle()
//        }
    }

    private fun signInGoogle() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)!!
            onAuthSuccessGoogle(account.displayName.toString(), account.email.toString())
        } catch (e: ApiException) {
            d("handleRequest", e.toString())
        }
    }

    fun onAuthSuccessLogin(email: String) {
        db.collection("user").whereEqualTo("email", email).get()
            .addOnSuccessListener {
                Log.i("login", "Success saving session")
                it.documents.forEach { document ->
                    Log.i("userData", "${document}")
                    val displayName = document["fullName"].toString()
                    val email = document["email"].toString()
                    preferences.SaveUser(displayName, email)
                }
                startActivity(Intent(this, DashboardActivity::class.java))
            }
            .addOnFailureListener {exception ->
                Log.e("login", "Login Error, ${exception}")
            }
    }

    fun onAuthSuccessGoogle(displayName: String, email: String) {
        preferences.SaveUser(displayName, email)
        startActivity(Intent(this, DashboardActivity::class.java))
    }
}