package com.example.teaapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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
        val shopItem = getItem(position)
        holder.bind(shopItem, onAddToCartClick) { onShopItemClick(shopItem, holder) }
    }

    private fun onShopItemClick(shopItem: ShopItem, holder: ShopItemViewHolder) {
        // Start SingleShopActivity
        val context = holder.itemView.context
        val intent = Intent(context, SIngleShopItemActivity::class.java).apply {
            putExtra("shopItem", shopItem) // Pass the shop item
        }
        context.startActivity(intent)
    }

    class ShopItemViewHolder(private val binding: ItemShopItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(shopItem: ShopItem, onAddToCartClick: (ShopItem) -> Unit, onItemClick: () -> Unit) {
            binding.itemName.text = shopItem.name
            binding.itemPrice.text = "$${shopItem.price}"

            // Load the image using Glide
            Glide.with(binding.itemImage.context)
                .load(shopItem.imageUrl)
                .into(binding.itemImage)

            binding.addToCartButton.setOnClickListener {
                onAddToCartClick(shopItem)
            }

            // Set the onClickListener for the whole item
            binding.root.setOnClickListener {
                onItemClick() // Navigate to SingleShopActivity
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
