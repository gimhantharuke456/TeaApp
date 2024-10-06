package com.example.teaapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.teaapp.databinding.ItemShopItemBinding
import com.example.teaapp.model.ShopItem

class ShopItemAdapter(
    private val onAddToCartClick: (ShopItem) -> Unit
) : ListAdapter<ShopItem, ShopItemAdapter.ShopItemViewHolder>(ShopItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val binding = ItemShopItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShopItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        holder.bind(getItem(position), onAddToCartClick)
    }

    class ShopItemViewHolder(private val binding: ItemShopItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(shopItem: ShopItem, onAddToCartClick: (ShopItem) -> Unit) {
            binding.itemName.text = shopItem.name
            binding.itemPrice.text = "$${shopItem.price}"
            // Load the image using your preferred image loading library (e.g., Glide, Picasso)

            binding.addToCartButton.setOnClickListener {
                onAddToCartClick(shopItem)
            }
        }
    }

    class ShopItemDiffCallback : DiffUtil.ItemCallback<ShopItem>() {
        override fun areItemsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean {
            return oldItem == newItem
        }
    }
}
