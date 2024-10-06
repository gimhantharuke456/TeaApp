package com.example.teaapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teaapp.model.ShopItem
import com.example.teaapp.repository.ShopItemRepository
import kotlinx.coroutines.launch

class ShopItemViewModel(private val repository: ShopItemRepository) : ViewModel() {

    private val _shopItems = MutableLiveData<List<ShopItem>>()
    val shopItems: LiveData<List<ShopItem>> = _shopItems

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun fetchShopItems() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.getShopItems()
            _isLoading.value = false
            result.fold(
                onSuccess = { items ->
                    _shopItems.value = items
                },
                onFailure = { exception ->
                    _error.value = exception.message ?: "An unknown error occurred"
                }
            )
        }
    }


}