package com.cs388.humanbenchmark

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient : GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        val email = intent.getStringExtra("email") // PULLS from UPDATE GUI in mainActivity
        val name = intent.getStringExtra("name")

        findViewById<TextView>(R.id.welcomeMessage).text = email + "\n" + name
        findViewById<Button>(R.id.signOutBtn).setOnClickListener {
            auth.signOut()
            googleSignInClient.signOut().addOnCompleteListener() {
                startActivity(Intent(this, MainActivity::class.java))
            }



        }
    }
}