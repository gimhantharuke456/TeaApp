package com.example.teaapp

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.teaapp.dto.CreateOrderRequest
import com.example.teaapp.model.CartItem
import com.example.teaapp.repository.OrderRepository
import com.example.teaapp.service.LocalPrefsService
import com.example.teaapp.viewmodel.OrderViewModel

class CheckoutActivity : AppCompatActivity() {
    private lateinit var orderViewModel: OrderViewModel
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_checkout)

        // Initialize ViewModel and DatabaseHelper
        val localPrefsService = LocalPrefsService(this) // Replace with your actual implementation
        orderViewModel = ViewModelProvider(this, OrderViewModelFactory(localPrefsService)).get(OrderViewModel::class.java)
        databaseHelper = DatabaseHelper(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Place orders from cart
        placeOrdersFromCart()
    }

    private fun placeOrdersFromCart() {
        val cartItems = databaseHelper.getCartItems()
        val shopId = "66fcb91da41b8319ef645ce7" // Static shop ID
        val address = findViewById<EditText>(R.id.address_input).text.toString() // Get address from input field

        for (cartItem in cartItems) {
            orderViewModel.createOrder(
                shopItemId = cartItem.itemId,
                shopId = shopId,
                quantity = cartItem.quantity,
                address = address,
                callback = object : OrderViewModel.OrderCallback {
                    override fun onSuccess(message: String) {
                        Toast.makeText(this@CheckoutActivity, message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(message: String) {
                        Toast.makeText(this@CheckoutActivity, message, Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }

        // Clear cart after placing orders
        databaseHelper.clearCart()
    }

    class OrderViewModelFactory(private val localPrefsService: LocalPrefsService) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(OrderViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return OrderViewModel(localPrefsService) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
