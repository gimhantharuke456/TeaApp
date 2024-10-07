package com.example.teaapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teaapp.model.User
import com.example.teaapp.service.UserService
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val _profileState = MutableLiveData<ProfileState>()
    val profileState: LiveData<ProfileState> = _profileState

    fun fetchProfile(userId: String) {
        _profileState.value = ProfileState.Loading
        viewModelScope.launch {
            try {
                val user = UserService.getProfile(userId)
                _profileState.value = ProfileState.Success(user)
            } catch (e: Exception) {
                _profileState.value = ProfileState.Error(e.message ?: "Failed to fetch profile")
            }
        }
    }

    fun updateProfile(user: User) {
        _profileState.value = ProfileState.Loading
        viewModelScope.launch {
            try {
                val updatedUser = UserService.updateProfile(user)
                _profileState.value = ProfileState.Success(updatedUser)
            } catch (e: Exception) {
                _profileState.value = ProfileState.Error(e.message ?: "Failed to update profile")
            }
        }
    }
}

sealed class ProfileState {
    object Loading : ProfileState()
    data class Success(val user: User) : ProfileState()
    data class Error(val message: String) : ProfileState()
}