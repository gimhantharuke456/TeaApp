package com.example.teaapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.teaapp.databinding.FragmentShopItemBinding
import com.example.teaapp.model.CartItem
import com.example.teaapp.model.ShopItem
import com.example.teaapp.repository.ShopItemRepository
import com.example.teaapp.viewmodel.ShopItemViewModel

class ShopItemFragment : Fragment() {

    private lateinit var viewModel: ShopItemViewModel
    private lateinit var adapter: ShopItemAdapter
    private var _binding: FragmentShopItemBinding? = null
    private val binding get() = _binding!!

    // Initialize DatabaseHelper
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize DatabaseHelper
        dbHelper = DatabaseHelper(requireContext())

        val repository = ShopItemRepository()
        viewModel = ViewModelProvider(this, ShopItemViewModelFactory(repository))
            .get(ShopItemViewModel::class.java)

        setupRecyclerView()
        observeViewModel()

        viewModel.fetchShopItems()
    }

    private fun setupRecyclerView() {
        adapter = ShopItemAdapter { shopItem ->
            addToCart(shopItem) // Handle add to cart click
        }
        binding.recyclerView.layoutManager = GridLayoutManager(context, 2) // 2 columns
        binding.recyclerView.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.shopItems.observe(viewLifecycleOwner) { items ->
            adapter.submitList(items)
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun addToCart(shopItem: ShopItem) {
        // Create CartItem
        val cartItem = CartItem(
            itemId = shopItem.id.toString(), // assuming ShopItem has an id property
            itemName = shopItem.name,
            itemPrice = shopItem.price,
            quantity = 1, // Default quantity to 1
            imageUrl = shopItem.imageUrl // Assuming ShopItem has an imageUrl property
        )

        // Add to cart in the database
        val id = dbHelper.addCartItem(cartItem)
        if (id != -1L) {
            Toast.makeText(context, "${shopItem.name} added to cart", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Failed to add ${shopItem.name} to cart", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class ShopItemViewModelFactory(private val repository: ShopItemRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ShopItemViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ShopItemViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
