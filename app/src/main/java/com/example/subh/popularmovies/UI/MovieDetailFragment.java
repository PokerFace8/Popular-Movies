package com.example.subh.popularmovies.UI;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.subh.popularmovies.Movies.Favorites;
import com.example.subh.popularmovies.Movies.Movie;
import com.example.subh.popularmovies.R;
import com.example.subh.popularmovies.Reviews.GetReviews;
import com.example.subh.popularmovies.Reviews.Review;
import com.example.subh.popularmovies.Reviews.ReviewAdapter;
import com.example.subh.popularmovies.Trailers.GetTrailers;
import com.example.subh.popularmovies.Trailers.Trailer;
import com.example.subh.popularmovies.Trailers.TrailerAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieDetailFragment extends Fragment {

    private static final int LOADER_REVIEW_ID = 201;
    private static final int LOADER_TRAILER_ID = 202;
    public static final String ON_SAVE_MOVIE = "ON_SAVE_MOVIE";
    public static final String ON_SAVE_TRAILERS = "ON_SAVE_TRAILERS";
    public static final String ON_SAVE_REVIEWS = "ON_SAVE_REVIEWS";

    private Movie mMovie;
    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;
    private CollapsingToolbarLayout mCollapsingappBarLayout;
    private FloatingActionButton mAddToFavorites, mDeleteFromFavorites;
    private Favorites mFavorites;

    @Bind(R.id.rv_trailer)
    RecyclerView mTrailersRecyclerView;
    @Bind(R.id.rv_reviews)
    RecyclerView mReviewsRecyclerView;
    @Bind(R.id.ratingBar1)
    RatingBar ratingBar;
    @Bind(R.id.title_movie)
    TextView mTitleMovie;
    @Bind(R.id.overview_movie_description)
    TextView mDescriptionMovie;
    @Bind(R.id.release_date)
    TextView mReleaseDate;
    @Bind(R.id.rating_text)
    TextView mRatingText;
    @Bind(R.id.poster_movie)
    ImageView mMoviePoster;


    public MovieDetailFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ON_SAVE_MOVIE)) {
            mMovie = getArguments().getParcelable(ON_SAVE_MOVIE);
        }
        setHasOptionsMenu(true);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Activity activity = getActivity();
        AppBarLayout appBarLayout = (AppBarLayout)activity.findViewById(R.id.appbar);
        mCollapsingappBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange() == 0)
                {
                    if ( mCollapsingappBarLayout!= null && activity instanceof MovieDetailActivity) {
                        mCollapsingappBarLayout.setTitle(mMovie.getTitle());
                    }
                }
                else
                {
                    if ( mCollapsingappBarLayout!= null && activity instanceof MovieDetailActivity) {
                        mCollapsingappBarLayout.setTitle("");
                    }
                }
            }
        });

        ImageView movieBackdrop = ((ImageView) activity.findViewById(R.id.movie_backdrop));
        if (movieBackdrop != null) {
            Picasso.with(activity)
                    .load(mMovie.getBackdropUrl())
                    .config(Bitmap.Config.RGB_565)
                    .into(movieBackdrop);
        }
        mAddToFavorites = ((FloatingActionButton) activity.findViewById(R.id.fab1));
        mDeleteFromFavorites = ((FloatingActionButton) activity.findViewById(R.id.fab2));
        if(!(mAddToFavorites ==null && mDeleteFromFavorites ==null)) {
            updateFavorites(mAddToFavorites, mDeleteFromFavorites);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<Trailer> listTrailers = mTrailerAdapter.getTrailers();
        if (listTrailers != null && !listTrailers.isEmpty()) {
            outState.putParcelableArrayList(ON_SAVE_TRAILERS, listTrailers);
        }
        ArrayList<Review> listReviews = mReviewAdapter.getReviews();
        if (listReviews != null && !listReviews.isEmpty()) {
            outState.putParcelableArrayList(ON_SAVE_REVIEWS, listReviews);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.detail_movie_fragment, container, false);
        ButterKnife.bind(this, rootView);

        ImageView movieBackdrop = ((ImageView) rootView.findViewById(R.id.movie_backdrop_tablet));
        if(movieBackdrop!=null) {
            Picasso.with(getContext())
                    .load(mMovie.getBackdropUrl())
                    .config(Bitmap.Config.RGB_565)
                    .into(movieBackdrop);
        }

        mAddToFavorites = ((FloatingActionButton) rootView.findViewById(R.id.fab3));
        mDeleteFromFavorites = ((FloatingActionButton) rootView.findViewById(R.id.fab4));
        if(!(mAddToFavorites ==null && mDeleteFromFavorites ==null)) {
            updateFavorites(mAddToFavorites, mDeleteFromFavorites);
        }

        String description = "\t\t"+mMovie.getOverview();
        mTitleMovie.setText(mMovie.getTitle());
        mDescriptionMovie.setText(description);
        mReleaseDate.setText(mMovie.getFormattedReleaseDate());

        Picasso.with(getContext())
                .load(mMovie.getPosterUrl())
                .config(Bitmap.Config.RGB_565)
                .into(mMoviePoster);

        if (mMovie.getRating() != null && !mMovie.getRating().isEmpty()) {
            String ratingStr = getResources().getString(R.string.user_rating_movie,
                    mMovie.getRating());
            mRatingText.setText(ratingStr);
            ratingBar.setRating(Float.valueOf(mMovie.getRating()) / 2.0f);
        }

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        mTrailersRecyclerView.setLayoutManager(layoutManager);
        mTrailerAdapter = new TrailerAdapter(new ArrayList<Trailer>());
        mTrailersRecyclerView.setHasFixedSize(true);

        mTrailersRecyclerView.setAdapter(mTrailerAdapter);
        mTrailersRecyclerView.setNestedScrollingEnabled(false);

        mReviewAdapter = new ReviewAdapter(new ArrayList<Review>());
        mReviewsRecyclerView.setAdapter(mReviewAdapter);


        if (savedInstanceState != null && savedInstanceState.containsKey(ON_SAVE_TRAILERS)) {
            List<Trailer> trailers = savedInstanceState.getParcelableArrayList(ON_SAVE_TRAILERS);
            mTrailerAdapter.addTrailers(trailers);
        }
        else {
            getTrailers();
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(ON_SAVE_REVIEWS)) {
            List<Review> reviews = savedInstanceState.getParcelableArrayList(ON_SAVE_REVIEWS);
            mReviewAdapter.addReviews(reviews);
        }
        else {
            getReviews();
        }
        return rootView;
    }

    private void getTrailers() {
        GetTrailers getTrailers = new GetTrailers(getContext(), mTrailerAdapter);
        Bundle bundle = new Bundle();
        bundle.putLong(GetTrailers.KEY_FOR_MOVIE_ID,mMovie.getMovieId());
        android.support.v4.app.LoaderManager loaderManager = getActivity().getSupportLoaderManager();
        loaderManager.restartLoader(LOADER_TRAILER_ID,bundle,getTrailers);
    }

    private void getReviews() {
        GetReviews getReviews = new GetReviews(getContext(), mReviewAdapter);
        Bundle bundle = new Bundle();
        bundle.putLong(GetReviews.KEY_FOR_MOVIE_ID,mMovie.getMovieId());
        android.support.v4.app.LoaderManager loaderManager =getActivity().getSupportLoaderManager();
        loaderManager.restartLoader(LOADER_REVIEW_ID,bundle,getReviews);
    }

    public void updateFavorites(FloatingActionButton fab1, FloatingActionButton fab2){
        mFavorites = new Favorites(getContext(), mMovie, fab1, fab2);
        mAddToFavorites.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mFavorites.AddToFavoritesDatabase();
                    }
                });

        mDeleteFromFavorites.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mFavorites.DeleteFromFavoritesDatabase();
                    }
                });

        mFavorites.RefreshFABFavoriteButtons();
    }

}

