package com.example.teaapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.teaapp.R
import com.example.teaapp.dto.OrderResponse

class OrderAdapter(private var orders: List<OrderResponse>) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val orderId: TextView = view.findViewById(R.id.orderId)
        val shopName: TextView = view.findViewById(R.id.shopName)
        val itemName: TextView = view.findViewById(R.id.itemName)
        val orderStatus: TextView = view.findViewById(R.id.orderStatus)
        val orderDetails: TextView = view.findViewById(R.id.orderDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.order_item, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.orderId.text = "Order ID: ${order.id}"
        holder.shopName.text = "Shop: ${order.shopName}"
        holder.itemName.text = "Item: ${order.itemName}"
        holder.orderStatus.text = "Status: ${order.status}"
        holder.orderDetails.text = "Quantity: ${order.quantity}, Address: ${order.address}"

        holder.itemView.findViewById<Button>(R.id.btnReview).setOnClickListener {
            onReviewClick(order)
        }
    }

    override fun getItemCount() = orders.size

    fun updateOrders(newOrders: List<OrderResponse>) {
        orders = newOrders
        notifyDataSetChanged()
    }

    private lateinit var onReviewClick: (OrderResponse) -> Unit

    fun setOnReviewClickListener(listener: (OrderResponse) -> Unit) {
        onReviewClick = listener
    }
}