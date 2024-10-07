package com.example.teaapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.teaapp.dto.CreateReviewResponse

class ReviewAdapter(private var reviews: List<CreateReviewResponse>) :
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    class ReviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val reviewText: TextView = view.findViewById(R.id.review_text)
        val reviewStars: TextView = view.findViewById(R.id.review_stars)
        val reviewDate: TextView = view.findViewById(R.id.review_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.review_item, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]
        holder.reviewText.text = review.reviewText
        holder.reviewStars.text = "⭐".repeat(review.stars) // Display stars as emojis
        holder.reviewDate.text = review.createdAt // Assuming createdAt is in a readable format
    }

    override fun getItemCount() = reviews.size

    fun updateReviews(newReviews: List<CreateReviewResponse>) {
        reviews = newReviews
        notifyDataSetChanged()
    }
}
