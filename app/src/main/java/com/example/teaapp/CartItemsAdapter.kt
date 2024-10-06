package com.example.teaapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.teaapp.model.CartItem

class CartItemsAdapter(private val cartItems: List<CartItem>) : RecyclerView.Adapter<CartItemsAdapter.CartItemViewHolder>() {

    class CartItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName: TextView = itemView.findViewById(R.id.item_name)
        val itemPrice: TextView = itemView.findViewById(R.id.item_price)
        val itemQuantity: TextView = itemView.findViewById(R.id.item_quantity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item_layout, parent, false)
        return CartItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        val cartItem = cartItems[position]
        holder.itemName.text = cartItem.itemName
        holder.itemPrice.text = "Price: ${cartItem.itemPrice}"
        holder.itemQuantity.text = "Quantity: ${cartItem.quantity}"
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }
}
