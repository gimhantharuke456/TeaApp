package com.example.teaapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.teaapp.databinding.FragmentCartBinding
import com.example.teaapp.model.CartItem

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private lateinit var cartAdapter: CartAdapter
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseHelper = DatabaseHelper(requireContext())
        setupRecyclerView()
        loadCartItems()
        setupPromoCode()
        updateTotals()

        binding.checkoutButton.setOnClickListener {
          var intent = Intent(requireContext(),CheckoutActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(
            onQuantityChanged = { item, newQuantity ->
                updateCartItemQuantity(item, newQuantity)
            },
            onRemoveClicked = { item ->
                removeCartItem(item)
            }
        )
        binding.cartRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = cartAdapter
        }
    }

    private fun loadCartItems() {
        val cartItems = databaseHelper.getCartItems()
        cartAdapter.submitList(cartItems)
        updateTotals()
    }

    private fun updateCartItemQuantity(item: CartItem, newQuantity: Int) {
        val updatedItem = item.copy(quantity = newQuantity)
        databaseHelper.updateCartItem(updatedItem)
        loadCartItems()
    }

    private fun removeCartItem(item: CartItem) {
        databaseHelper.deleteCartItem(item.id)
        loadCartItems()
    }

    private fun setupPromoCode() {

    }

    private fun updateTotals() {
        val cartItems = databaseHelper.getCartItems()
        val subtotal = cartItems.sumOf { it.itemPrice * it.quantity }
        val tax = subtotal * 0.1 // Assuming 10% tax
        val delivery = 500.0 // Fixed delivery charge
        val total = subtotal + tax + delivery

        binding.subtotalTextView.text = "Rs.${String.format("%.2f", subtotal)}"
        binding.taxTextView.text = "Rs.${String.format("%.2f", tax)}"
        binding.deliveryTextView.text = "Rs.${String.format("%.2f", delivery)}"
        binding.totalTextView.text = "Rs.${String.format("%.2f", total)}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}