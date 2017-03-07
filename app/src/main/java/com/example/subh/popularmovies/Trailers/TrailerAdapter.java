package com.example.subh.popularmovies.Trailers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.subh.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by subh on 06/01/17.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    @SuppressWarnings("unused")
    private final static String LOG_TAG = TrailerAdapter.class.getSimpleName();

    private final ArrayList<Trailer> mTrailers;


    public TrailerAdapter(ArrayList<Trailer> trailers) {
        mTrailers = trailers;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_list_item, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TrailerViewHolder holder, final int position) {
        final Trailer trailer = mTrailers.get(position);
        final Context context = holder.mView.getContext();

        holder.mTrailer = trailer;

        String trailerImage = "http://img.youtube.com/vi/" + trailer.getKey() + "/0.jpg";

        Picasso.with(context)
                .load(trailerImage)
                .config(Bitmap.Config.RGB_565)
                .into(holder.TrailerImageView);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(trailer.getTrailerUrl())));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        @Bind(R.id.trailer_image)
        ImageView TrailerImageView;
        public Trailer mTrailer;

        public TrailerViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mView = view;
        }
    }

    public void addTrailers(List<Trailer> trailers) {
        mTrailers.clear();
        mTrailers.addAll(trailers);
        notifyDataSetChanged();
    }

    public ArrayList<Trailer> getTrailers() {
        return mTrailers;
    }
}

