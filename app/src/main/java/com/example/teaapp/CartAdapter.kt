package com.example.teaapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.teaapp.databinding.ItemCartBinding
import com.example.teaapp.model.CartItem

class CartAdapter(
    private val onQuantityChanged: (CartItem, Int) -> Unit,
    private val onRemoveClicked: (CartItem) -> Unit
) : ListAdapter<CartItem, CartAdapter.CartViewHolder>(CartDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CartViewHolder(private val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CartItem) {
            binding.apply {
                itemName.text = item.itemName
                itemPrice.text = "Rs.${String.format("%.2f", item.itemPrice)}"
                quantityText.text = item.quantity.toString()

                Glide.with(itemImage)
                    .load(item.imageUrl)
                    .centerCrop()
                    .into(itemImage)

                decreaseButton.setOnClickListener {
                    if (item.quantity > 1) {
                        onQuantityChanged(item, item.quantity - 1)
                    }
                }

                increaseButton.setOnClickListener {
                    onQuantityChanged(item, item.quantity + 1)
                }

                removeButton.setOnClickListener {
                    onRemoveClicked(item)
                }
            }
        }
    }

    class CartDiffCallback : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem == newItem
        }
    }
}