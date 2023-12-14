package com.cs388.humanbenchmark


import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class ProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private var currentUser: GoogleSignInAccount? = null

    private var signedIn = false
    //private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        if(!signedIn ){

            auth = FirebaseAuth.getInstance()

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(com.firebase.ui.auth.R.string.default_web_client_id))
                .requestEmail()
                .build()

            googleSignInClient = context?.let { GoogleSignIn.getClient(it, gso) }!!





            view?.findViewById<Button>(R.id.signInBtn)?.setOnClickListener { // sign in button
                if(!signedIn){


                    signInGoogle()
                    view?.findViewById<Button>(R.id.signInBtn)?.text = "Sign Out"
                    signedIn = true


                }
                else {
                    auth.signOut()
                    googleSignInClient.signOut()
                    view?.findViewById<Button>(R.id.signInBtn)?.text = "Sign In"
            destroyUI()
                    signedIn = false


                }
            }

 }
        else{
            currentUser?.let { updateUI(it) }
        }

        return view


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
                currentUser = account // Store the current user
                Log.d("TEST","SUCCESS1")
                updateUI(account)
            }
        }
        else{
            Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }
    @SuppressLint("SuspiciousIndentation")
    private fun updateUI(account: GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener{
            if (it.isSuccessful){


                var username = view?.findViewById<TextView>(R.id.username)
                var image = requireView().findViewById<ImageView>(R.id.profilePicture)

                if (username != null) {
                    username.text = "Welcome: \n" + account.givenName
                }

                Glide.with(this).load(account.photoUrl).into(image)
                username?.visibility = View.VISIBLE
                image?.visibility = View.VISIBLE


            }
            else{
                Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun destroyUI(){
        var username = view?.findViewById<TextView>(R.id.username)

        var image = requireView().findViewById<ImageView>(R.id.profilePicture)
        username?.visibility = View.INVISIBLE
        image?.visibility = View.INVISIBLE

    }
}