package com.example.teaapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teaapp.dto.CreateOrderRequest
import com.example.teaapp.dto.OrderResponse
import com.example.teaapp.repository.OrderRepository
import com.example.teaapp.service.LocalPrefsService
import com.example.teaapp.service.OrderService
import kotlinx.coroutines.launch

class OrderViewModel(private val localPrefsService: LocalPrefsService) : ViewModel() {

    private val orderRepository: OrderRepository = OrderRepository(localPrefsService)


    // Callback for displaying messages
    interface OrderCallback {
        fun onSuccess(message: String)
        fun onError(message: String)
        fun onOrdersFetched(orders: List<OrderResponse>)
    }

    fun createOrder(shopItemId: String, shopId: String, quantity: Int, address: String, callback: OrderCallback) {
        viewModelScope.launch {
            try {
                val userId = localPrefsService.getString("USER_ID", "") ?: ""
                val token = localPrefsService.getString("USER_TOKEN") ?: ""
                val orderRequest = CreateOrderRequest(
                    shopItemId = shopItemId,
                    shopId = shopId,
                    quantity = quantity,
                    address = address,
                    status = "pending",
                    userId = userId
                )

                val result = OrderService.createOrder(token, orderRequest)

                // Assuming the API returns a successful response
                callback.onSuccess("Order created successfully!")
            } catch (e: Exception) {
                Log.e("OrderViewModel", "Error creating order: ${e.message}")
                callback.onError("Failed to create order: ${e.message}")
            }
        }
    }

    fun getOrders(callback: OrderCallback) {
        viewModelScope.launch {
            val result = orderRepository.getOrders()
            result.onSuccess { orders ->
                val userId = localPrefsService.getString("USER_ID")
                val filteredOrders = orders.filter { it.userId == userId }

                if (filteredOrders.isNotEmpty()) {
                    callback.onOrdersFetched(filteredOrders)
                    callback.onSuccess("Orders retrieved successfully!")
                } else {
                    callback.onOrdersFetched(emptyList())
                    callback.onSuccess("No orders found for the current user.")
                }
            }.onFailure { error ->
                Log.e("OrderViewModel", "Error retrieving orders: ${error.message}")
                callback.onError("Failed to retrieve orders: ${error.message}")
            }
        }
    }

}