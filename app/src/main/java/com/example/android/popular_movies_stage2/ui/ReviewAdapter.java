package com.example.android.popular_movies_stage2.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popular_movies_stage2.R;
import com.example.android.popular_movies_stage2.model.domain.Review;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<Review> reviewList = new ArrayList<>();

    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list_item, parent, false);
        return new ReviewViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        holder.bind(reviewList.get(position));
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }


    class ReviewViewHolder extends RecyclerView.ViewHolder {

        TextView author;

        TextView content;

        void bind(Review review) {
            author.setText(review.getAuthor());
            content.setText(review.getContent());
        }


        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.review_list_author);
            content = itemView.findViewById(R.id.review_list_content);
        }


    }

}
