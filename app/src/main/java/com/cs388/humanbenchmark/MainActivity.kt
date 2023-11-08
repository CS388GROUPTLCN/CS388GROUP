package com.cs388.humanbenchmark

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var googleSignInClient : GoogleSignInClient

    //private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        findViewById<Button>(R.id.signInBtn).setOnClickListener {
            signInGoogle()
        }
    }

    private fun signInGoogle(){
        val signInIntent = googleSignInClient.signInIntent
        Log.d("TEST","SUCCESS-1")
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
                if (result.resultCode == Activity.RESULT_OK){
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    //this isn't being reached
                    Log.d("TEST","SUCCESS0")
                    handleResults(task)
                }
                else{
                    Log.d("TEST", result.resultCode.toString())
                }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>)  {
        if (task.isSuccessful){
            val account : GoogleSignInAccount? = task.result
            if (account != null){
                Log.d("TEST","SUCCESS1")
                updateUI(account)
            }
        }
        else{
            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }
    private fun updateUI(account: GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener{
            if (it.isSuccessful){
                Log.d("TEST","SUCCESS2")
                val intent : Intent = Intent(this, HomeActivity::class.java)

                intent.putExtra("email",account.email.toString())
                intent.putExtra("name", account.displayName.toString())
                startActivity(intent)
                Log.d("TEST",account.email.toString())

            }
            else{
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
}