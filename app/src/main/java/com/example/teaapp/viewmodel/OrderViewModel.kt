package com.example.teaapp.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teaapp.dto.CreateOrderRequest
import com.example.teaapp.repository.OrderRepository
import com.example.teaapp.service.LocalPrefsService
import kotlinx.coroutines.launch

class OrderViewModel(private val localPrefsService: LocalPrefsService) : ViewModel() {

    private val orderRepository: OrderRepository

    init {
        orderRepository = OrderRepository(localPrefsService)
    }

    // Callback for displaying messages
    interface OrderCallback {
        fun onSuccess(message: String)
        fun onError(message: String)
    }

    fun createOrder(shopItemId: String, shopId: String, quantity: Int, address: String, callback: OrderCallback) {
        viewModelScope.launch {
            val userId = localPrefsService.getString("USER_ID", "") ?: ""
            val orderRequest = CreateOrderRequest(
                shopItemId = shopItemId,
                shopId = shopId,
                quantity = quantity,
                address = address,
                status = "pending",
                userId = userId
            )

            val result = orderRepository.createOrder(orderRequest)
            result.onSuccess { order ->
                callback.onSuccess("Order placed successfully!")
            }.onFailure { error ->
                Log.e("OrderViewModel", "Error placing order: ${error.message}")
                callback.onError("Failed to place order: ${error.message}")
            }
        }
    }

    fun getOrders(callback: OrderCallback) {
        viewModelScope.launch {
            val result = orderRepository.getOrders()
            result.onSuccess { orders ->
                callback.onSuccess("Orders retrieved successfully!")
            }.onFailure { error ->
                Log.e("OrderViewModel", "Error retrieving orders: ${error.message}")
                callback.onError("Failed to retrieve orders: ${error.message}")
            }
        }
    }
}
