package com.example.subh.popularmovies.Reviews;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.subh.popularmovies.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private final ArrayList<Review> mReviews;

    public ReviewAdapter(ArrayList<Review> reviews){
        mReviews = reviews;
    }


    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_list_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ReviewViewHolder holder, final int position) {
        final Review review = mReviews.get(position);

        holder.mReview = review;
        holder.mReviewContent.setText(review.getContent());
        holder.mAuthorTextView.setText(review.getAuthor());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(review.getUrl()));
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public void addReviews(List<Review> reviews) {
        mReviews.clear();
        mReviews.addAll(reviews);
        notifyDataSetChanged();
    }

    public ArrayList<Review> getReviews() {
        return mReviews;
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        @Bind(R.id.review_content)
        TextView mReviewContent;
        @Bind(R.id.review_author)
        TextView mAuthorTextView;
        public Review mReview;

        public ReviewViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mView = view;
        }
    }
}
