package com.example.teaapp

import android.os.Bundle
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.teaapp.OrderAdapter
import com.example.teaapp.dto.CreateReviewDTO
import com.example.teaapp.dto.OrderResponse
import com.example.teaapp.service.LocalPrefsService
import com.example.teaapp.service.ReviewService
import com.example.teaapp.viewmodel.OrderViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyOrderActivity : AppCompatActivity(), OrderViewModel.OrderCallback {
    private lateinit var orderViewModel: OrderViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var orderAdapter: OrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_my_order)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.orderRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        orderAdapter = OrderAdapter(emptyList())
        orderAdapter.setOnReviewClickListener { order ->
            showReviewDialog(order)
        }
        recyclerView.adapter = orderAdapter

        val localPrefsService = LocalPrefsService(this)
        orderViewModel = ViewModelProvider(this, OrderViewModelFactory(localPrefsService))
            .get(OrderViewModel::class.java)

        fetchOrders()
    }

    private fun fetchOrders() {
        orderViewModel.getOrders(this)
    }

    override fun onSuccess(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onOrdersFetched(orders: List<OrderResponse>) {
        orderAdapter.updateOrders(orders)
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

    private fun showReviewDialog(order: OrderResponse) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_review, null)
        val ratingBar = dialogView.findViewById<RatingBar>(R.id.ratingBar)
        val editTextReview = dialogView.findViewById<EditText>(R.id.editTextReview)

        AlertDialog.Builder(this)
            .setTitle("Submit Review")
            .setView(dialogView)
            .setPositiveButton("Submit") { _, _ ->
                // Get user ID from local shared preferences
                val userId = LocalPrefsService(this).getString("USER_ID") ?:""
                val reviewText = editTextReview.text.toString()
                val stars = ratingBar.rating.toInt()

                // Create the review request
                val reviewRequest = CreateReviewDTO(userId, order.shopItemId, reviewText, stars)

                // Use a coroutine scope to call the suspend function
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response = ReviewService.create(reviewRequest)
                        withContext(Dispatchers.Main) {
                            // Show a success message to the user
                            Toast.makeText(this@MyOrderActivity, "Review submitted!", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            // Show an error message to the user
                            Toast.makeText(this@MyOrderActivity, "Error submitting review: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}