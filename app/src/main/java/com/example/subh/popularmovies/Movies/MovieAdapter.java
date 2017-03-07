package com.example.subh.popularmovies.Movies;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.subh.popularmovies.Database.FavMovieContract;
import com.example.subh.popularmovies.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    @SuppressWarnings("unused")
    private final static String LOG_TAG = MovieAdapter.class.getSimpleName();

    private final ArrayList<Movie> mMovies;
    private final Callbacks mCallbacks;

    public interface Callbacks {
        void openMovieDetails(Movie movie, int position);
    }

    public MovieAdapter(ArrayList<Movie> movies, Callbacks callbacks) {
        mMovies = movies;
        this.mCallbacks = callbacks;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_item, parent, false);
        final Context context = view.getContext();

        int gridColsNumber = context.getResources()
                .getInteger(R.integer.number_of_columns);

        view.getLayoutParams().height = (int) (parent.getWidth() / gridColsNumber *
                Movie.POSTER_ASPECT_RATIO);

        return new MovieViewHolder(view);
    }

    @Override
    public void onViewRecycled(MovieViewHolder holder) {
        super.onViewRecycled(holder);
        holder.cleanUp();
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, final int position) {
        final Movie movie = mMovies.get(position);
        final Context context = holder.mView.getContext();

        holder.mMovie = movie;
        holder.mTitleView.setText(movie.getTitle());

        String posterUrl = movie.getPosterUrl();

        if (posterUrl == null) {
            holder.mTitleView.setVisibility(View.VISIBLE);
        }

        Picasso.with(context)
                .load(movie.getPosterUrl())
                .config(Bitmap.Config.RGB_565)
                .into(holder.mThumbnailView,
                        new Callback() {
                            @Override
                            public void onSuccess() {
                                if (holder.mMovie.getMovieId() != movie.getMovieId()) {
                                    holder.cleanUp();
                                } else {
                                    holder.mThumbnailView.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onError() {
                                holder.mTitleView.setVisibility(View.VISIBLE);
                            }
                        }
                );

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.openMovieDetails(movie, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        @Bind(R.id.thumbnail)
        ImageView mThumbnailView;
        @Bind(R.id.title)
        TextView mTitleView;
        public Movie mMovie;

        public MovieViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mView = view;
        }

        public void cleanUp() {
            final Context context = mView.getContext();
            Picasso.with(context).cancelRequest(mThumbnailView);
            mThumbnailView.setImageBitmap(null);
            mThumbnailView.setVisibility(View.INVISIBLE);
            mTitleView.setVisibility(View.GONE);
        }

    }

    public void addFromList(List<Movie> movies) {
        mMovies.clear();
        mMovies.addAll(movies);
        notifyDataSetChanged();
    }

    public void addFromCursor(Cursor cursor) {
        mMovies.clear();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(FavMovieContract.FavMovieEntry.INDEX_COLUMN_ID_MOVIE);
                String title = cursor.getString(FavMovieContract.FavMovieEntry
                        .INDEX_COLUMN_TITLE_MOVIE);
                String posterUrl = cursor.getString(FavMovieContract.FavMovieEntry
                        .INDEX_COLUMN_POSTER_URL_MOVIE);
                String backdropUrl = cursor.getString(FavMovieContract.FavMovieEntry
                        .INDEX_COLUMN_BACKDROP_URL_MOVIE);
                String description = cursor.getString(FavMovieContract.FavMovieEntry
                        .INDEX_COLUMN_DESCRIPTION_MOVIE);
                String rating = cursor.getString(FavMovieContract.FavMovieEntry
                        .INDEX_COLUMN_RATING_MOVIE);
                String releaseDate = cursor.getString(FavMovieContract.FavMovieEntry
                        .INDEX_COLUMN_RELEASE_DATE_MOVIE);

                Movie movie = new Movie(id, title, posterUrl, backdropUrl, description, rating, releaseDate);
                mMovies.add(movie);
            } while (cursor.moveToNext());
        }
        notifyDataSetChanged();
    }

    public ArrayList<Movie> getMovies() {
        return mMovies;
    }
}
