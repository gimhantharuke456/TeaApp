package com.example.teaapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teaapp.dto.RegisterRequest
import com.example.teaapp.dto.RegisterResponse
import com.example.teaapp.service.UserService
import kotlinx.coroutines.launch

class SignupViewModel : ViewModel() {

    private val _signupResult = MutableLiveData<SignupResult>()
    val signupResult: LiveData<SignupResult> = _signupResult

    fun signup(name: String, email: String, password: String, confirmPassword: String,firstName:String , lastName:String, address:String,phoneNumber:String,gender:String) {
        if (password != confirmPassword) {
            _signupResult.value = SignupResult.Error("Passwords do not match")
            return
        }

        _signupResult.value = SignupResult.Loading

        viewModelScope.launch {
            try {
                val request = RegisterRequest(
                    username = name, // Using email as username
                    email = email,
                    password = password,
                    role = "USER", // Assuming default role
                    firstName = firstName,
                    lastName = lastName,
                    address = address, // These fields are not in the UI, so leaving them blank
                    phoneNumber =phoneNumber,
                    gender =gender
                )
                val response = UserService.register(request)
                _signupResult.value = SignupResult.Success(response)
            } catch (e: Exception) {
                _signupResult.value = SignupResult.Error(e.message ?: "An unknown error occurred")
            }
        }
    }
}

sealed class SignupResult {
    object Loading : SignupResult()
    data class Success(val user: RegisterResponse) : SignupResult()
    data class Error(val message: String) : SignupResult()
}