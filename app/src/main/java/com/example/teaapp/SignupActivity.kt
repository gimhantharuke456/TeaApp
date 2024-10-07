package com.example.teaapp

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.teaapp.databinding.ActivitySignupBinding
import com.example.teaapp.viewmodel.SignupResult
import com.example.teaapp.viewmodel.SignupViewModel

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private val viewModel: SignupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        setupGenderDropdown()
        observeViewModel()
    }

    private fun setupListeners() {
        binding.backButton.setOnClickListener { finish() }

        binding.signupButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val confirmPassword = binding.confirmPasswordEditText.text.toString()
            val firstName = binding.firstNameEditText.text.toString()
            val lastName = binding.lastNameEditText.text.toString()
            val address = binding.addressEditText.text.toString()
            val phoneNumber = binding.phoneNumberEditText.text.toString()
            val gender = binding.genderAutoCompleteTextView.text.toString()

            if (binding.termsCheckBox.isChecked) {
                viewModel.signup(username, email, password, confirmPassword, firstName, lastName, address, phoneNumber, gender)
            } else {
                Toast.makeText(this, "Please agree to the Terms & Conditions", Toast.LENGTH_SHORT).show()
            }
        }

        binding.loginPromptTextView.setOnClickListener {
            // Navigate to login screen
            finish()
        }
    }

    private fun setupGenderDropdown() {
        val genders = arrayOf("Male", "Female", "Other")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, genders)
        binding.genderAutoCompleteTextView.setAdapter(adapter)
    }

    private fun observeViewModel() {
        viewModel.signupResult.observe(this, Observer { result ->
            when (result) {
                is SignupResult.Success -> {
                    hideLoading()
                    // Navigate to main screen or show success message
                    Toast.makeText(this, "Signup successful", Toast.LENGTH_SHORT).show()
                    finish()
                }
                is SignupResult.Error -> {
                    hideLoading()
                    Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
                }
                SignupResult.Loading -> showLoading()
            }
        })
    }

    private fun showLoading() {
        binding.loadingProgressBar.visibility = View.VISIBLE
        binding.signupButton.isEnabled = false
    }

    private fun hideLoading() {
        binding.loadingProgressBar.visibility = View.GONE
        binding.signupButton.isEnabled = true
    }
}