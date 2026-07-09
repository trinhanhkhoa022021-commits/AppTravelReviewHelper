package com.example.apptravelreviewhelper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptravelreviewhelper.R;
import com.example.apptravelreviewhelper.models.Review; // Import model Review của bạn

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private Context context;
    private List<Review> reviewList;

    public ReviewAdapter(Context context, List<Review> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviewList.get(position);

        // Hiển thị email người đánh giá (Có thể che đi một phần nếu muốn bảo mật)
        holder.tvReviewerEmail.setText(review.getUserEmail());
        holder.itemRatingBar.setRating(review.getRating());
        holder.tvReviewComment.setText(review.getComment());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView tvReviewerEmail, tvReviewComment;
        RatingBar itemRatingBar;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReviewerEmail = itemView.findViewById(R.id.tvReviewerEmail);
            itemRatingBar = itemView.findViewById(R.id.itemRatingBar);
            tvReviewComment = itemView.findViewById(R.id.tvReviewComment);
        }
    }
}