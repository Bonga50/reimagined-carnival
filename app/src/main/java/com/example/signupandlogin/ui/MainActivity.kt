package com.example.signupandlogin.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.signupandlogin.R
import com.example.signupandlogin.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        //setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Firebase Auth
        auth = Firebase.auth

        auth = Firebase.auth
        val user = auth.currentUser

        if (user == null) {
            Log.d(TAG, "User is null in onCreate, redirecting to Login.")
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return // Important: Prevent rest of onCreate if user is null
        } else {
            Log.d(TAG, "User found: ${user.email}. Setting welcome message.")
            binding.tvWelcome.text = "Welcome ${user.email}"
        }

        binding.btnLogout.setOnClickListener {
            performLogout()
        }


    }



    private fun performLogout() {
        Log.d("MainActivity", "Logout button clicked. Signing out...") // <-- Add Log
        auth.signOut()
        Log.d("MainActivity", "Sign out called. Navigating to LoginActivity...") // <-- Add Log
        Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish() // Close MainActivity
        Log.d("MainActivity", "finish() called.") // <-- Add Log
    }
}