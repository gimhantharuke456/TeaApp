package com.example.teaapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.teaapp.databinding.ActivitySigninBinding
import com.example.teaapp.repository.UserRepository
import com.example.teaapp.service.LocalPrefsService
import kotlinx.coroutines.launch

class SigninActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySigninBinding
    private val userRepository = UserRepository()
    private lateinit var localPrefsService: LocalPrefsService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        localPrefsService = LocalPrefsService(this)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnContinue.setOnClickListener {
            logButtonClick("Continue")
            signIn()
        }

        binding.btnGuest.setOnClickListener {
            logButtonClick("Guest")
            // Implement guest login logic here
        }

        binding.tvForgotPassword.setOnClickListener {
            logButtonClick("Forgot Password")
            // Implement forgot password logic here
        }

        binding.tvHaveAccount.setOnClickListener {
            logButtonClick("Sign Up")
            navigateToSignup()
        }
    }

    private fun signIn() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                val result = userRepository.login(email, password)
                result.onSuccess { loginResponse ->
                    // Handle successful login
                    Toast.makeText(this@SigninActivity, "Login successful", Toast.LENGTH_SHORT).show()

                    // Save the token and user ID using LocalPrefsService
                    saveUserData(loginResponse.token.token, loginResponse.token.id)

                    // Navigate to the main screen
                    navigateToMainScreen()
                }.onFailure { error ->
                    Toast.makeText(this@SigninActivity, "Login failed: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@SigninActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveUserData(token: String, userId: String) {
        localPrefsService.saveString("USER_TOKEN", token)
        localPrefsService.saveString("USER_ID", userId)
    }

    private fun navigateToMainScreen() {
        // Replace MainActivity::class.java with your main activity
        val intent = Intent(this, ViewContainerActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun navigateToSignup() {
        val intent = Intent(this, SignupActivity::class.java)
        startActivity(intent)
    }

    private fun logButtonClick(buttonName: String) {
        Log.d("SigninActivity", "Button clicked: $buttonName")
    }
}