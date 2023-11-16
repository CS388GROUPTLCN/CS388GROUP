package com.cs388.humanbenchmark

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient : GoogleSignInClient
    private lateinit var database: DatabaseReference;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        database = Firebase.database.reference
        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        val email = intent.getStringExtra("email")
        val name = intent.getStringExtra("name")
        findViewById<TextView>(R.id.account).text = email + "\n" + name
        val uid = auth.currentUser!!.uid
        val checkUser = database.child("users").child(uid)
        var counter = 0
        checkUser.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //Log.i("firebase", checkUser.key)
                if (dataSnapshot.exists()){
                    Log.i("firebase", "USER EXISTS")
                    counter = dataSnapshot.child("counter").getValue(Int::class.java)!!

                }
                else{
                    Log.i("firebase", "USER DOESNT EXIST")
                    writeNewUser(uid,name,email)
                }
                findViewById<TextView>(R.id.counter).text = counter.toString()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.i("firebase",databaseError.getMessage())
            }
        })

        findViewById<Button>(R.id.signOutBtn).setOnClickListener {
            auth.signOut()
            googleSignInClient.signOut().addOnCompleteListener() {
                startActivity(Intent(this, MainActivity::class.java))
            }
        }

        findViewById<Button>(R.id.tapBtn).setOnClickListener {
            counter++
            findViewById<TextView>(R.id.counter).text = counter.toString()
            database.child("users").child(uid).child("counter").setValue(counter)
        }
    }
    @IgnoreExtraProperties
    data class User(val username: String? = null, val email: String? = null, val counter: Int? = 0) {
        // Null default values create a no-argument default constructor, which is needed
        // for deserialization from a DataSnapshot.
    }
    fun writeNewUser(userId: String, name: String?, email: String?){
        val user= User(name, email)
        Log.i("firebase", "CREATED USER")
        if (userId != null) {
            database.child("users").child(userId).setValue(user)
        }
    }
}