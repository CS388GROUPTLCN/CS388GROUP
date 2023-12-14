package com.cs388.humanbenchmark

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    lateinit var drawerLayout: DrawerLayout
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    //private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



       // TODO : initialize the fragment, create logic behind it.

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)


        val profileFragment: Fragment = ProfileFragment()
        val gameFragment: Fragment = GameFragment()
        val leaderboardFragment: Fragment = LeaderboardFragment()
        lateinit var fragment: Fragment
        fragment = gameFragment
        replaceFragment(fragment)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_profile -> fragment = profileFragment
                R.id.action_games -> fragment = gameFragment
               R.id.action_leaderboard -> fragment = leaderboardFragment
            }
        replaceFragment(fragment)
            true
        }




    }

    private fun replaceFragment(fragment: Fragment) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main_frame_layout, fragment)
        fragmentTransaction.commit()
    }




}