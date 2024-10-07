package com.example.teaapp

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.teaapp.dto.CreateReviewResponse
import com.example.teaapp.model.CartItem
import com.example.teaapp.model.ShopItem
import com.example.teaapp.service.ReviewService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SIngleShopItemActivity : AppCompatActivity() {

    private lateinit var itemImage: ImageView
    private lateinit var itemName: TextView
    private lateinit var itemPrice: TextView
    private lateinit var itemDescription: TextView
    private lateinit var addToCartButton: Button
    private lateinit var cartService: DatabaseHelper
    private lateinit var reviewRecyclerView: RecyclerView
    private lateinit var reviewAdapter: ReviewAdapter
    private var reviews: List<CreateReviewResponse> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_single_shop_item)

        // Setup views
        itemImage = findViewById(R.id.item_image)
        itemName = findViewById(R.id.item_name)
        itemPrice = findViewById(R.id.item_price)
        itemDescription = findViewById(R.id.item_description)
        addToCartButton = findViewById(R.id.add_to_cart_button)

        // Initialize RecyclerView for reviews
        reviewRecyclerView = findViewById(R.id.reviewRecyclerView)
        reviewRecyclerView.layoutManager = LinearLayoutManager(this)
        reviewAdapter = ReviewAdapter(emptyList())
        reviewRecyclerView.adapter = reviewAdapter

        // Load the shop item data
        val shopItem = intent.getSerializableExtra("shopItem") as? ShopItem
        shopItem?.let {
            displayShopItem(it)
        }

        // Add to Cart button functionality
        addToCartButton.setOnClickListener {
            addToCart(shopItem)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        cartService = DatabaseHelper(this)
    }

    private fun displayShopItem(shopItem: ShopItem) {
        itemName.text = shopItem.name
        itemPrice.text = "$${shopItem.price}"
        itemDescription.text = "This is a detailed description of the item." // You can replace this with actual data
        Glide.with(this)
            .load(shopItem.imageUrl)
            .into(itemImage)

        // Fetch reviews using a coroutine
        fetchReviews(shopItem.id)
    }

    private fun fetchReviews(shopItemId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                reviews = ReviewService.getReviewsShopId(shopItemId)
                withContext(Dispatchers.Main) {
                    // Update the RecyclerView with the fetched reviews
                    reviewAdapter.updateReviews(reviews)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@SIngleShopItemActivity, "Error fetching reviews: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun addToCart(shopItem: ShopItem?) {
        shopItem?.let {
            val cartItem = CartItem(
                0,
                shopItem.id,
                shopItem.name,
                shopItem.price,
                1,
                shopItem.imageUrl
            )
            try {
                // Attempt to add the item to the cart
                cartService.addCartItem(cartItem)
                Toast.makeText(this, "${shopItem.name} added to cart!", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                // Handle the exception and show an error message
                Toast.makeText(this, "Error adding item to cart: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
