package com.example.signupandlogin.ui

import android.os.Bundle
import android.util.Patterns
import android.content.Intent
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.signupandlogin.R
import com.example.signupandlogin.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import android.widget.Toast
import com.google.firebase.auth.ktx.auth

class SignupActivity : AppCompatActivity() {


    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        //setContentView(R.layout.activity_signup)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth

        binding.btnSignUp.setOnClickListener {
            performSignUp()
        }

        binding.tvLoginLink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun performSignUp() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val termsAccepted = binding.cbTerms.isChecked

        // --- Input Validations ---
        if (email.isEmpty()) {
            binding.etEmail.error = "Email is required"
            binding.etEmail.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Please enter a valid email"
            binding.etEmail.requestFocus()
            return
        }

        if (password.isEmpty()) {
            binding.etPassword.error = "Password is required"
            binding.etPassword.requestFocus()
            return
        }

        if (password.length < 6) { // Firebase requires at least 6 characters
            binding.etPassword.error = "Password must be at least 6 characters long"
            binding.etPassword.requestFocus()
            return
        }

        if (!termsAccepted) {
            Toast.makeText(baseContext, "Please accept the Terms & Conditions.",
                Toast.LENGTH_SHORT).show()
            binding.cbTerms.requestFocus() // Indicate the checkbox needs attention
            return
        }
        // --- End Input Validations ---


        setLoading(true) // Show progress bar

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                setLoading(false) // Hide progress bar
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    // Log.d(TAG, "createUserWithEmail:success") // Optional logging
                    Toast.makeText(baseContext, "Sign Up Successful.",
                        Toast.LENGTH_SHORT).show()

                    // Navigate to MainActivity (or LoginActivity for email verification first)
                    val intent = Intent(this, MainActivity::class.java)
                    // Clear back stack so user can't navigate back to SignUp/Login after success
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish() // Finish SignUpActivity

                } else {
                    // If sign in fails, display a message to the user.
                    // Log.w(TAG, "createUserWithEmail:failure", task.exception) // Optional logging
                    Toast.makeText(baseContext, "Authentication failed: ${task.exception?.message}",
                        Toast.LENGTH_LONG).show() // Show specific Firebase error
                }
            }
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.btnSignUp.isEnabled = false
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.btnSignUp.isEnabled = true
            binding.progressBar.visibility = View.GONE
        }
    }
}