package com.example.subh.popularmovies.Reviews;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.example.subh.popularmovies.Api.ApiClient;
import com.example.subh.popularmovies.BuildConfig;
import com.example.subh.popularmovies.Api.ApiService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class GetReviews implements LoaderManager.LoaderCallbacks<List<Review>> {


    public static String LOG_TAG = GetReviews.class.getSimpleName();
    private Context mContext;
    private ReviewAdapter mReviewAdapter;
    public static String KEY_FOR_MOVIE_ID = "movie_id";

    @Override
    public Loader<List<Review>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<List<Review>>(mContext) {

            @Override
            protected void onStartLoading() {
                if (args == null) {
                    return;
                }
                forceLoad();
            }

            @Override
            public List<Review> loadInBackground() {
                long movieId = args.getLong(KEY_FOR_MOVIE_ID);

                ApiService service = ApiClient.getClient().create(ApiService.class);
                Call<Review.AllReviews> call = service.getReviews(movieId,
                        BuildConfig.TMDbApiKey);
                try {
                    Response<Review.AllReviews> response = call.execute();
                    Review.AllReviews reviews = response.body();
                    return reviews.getReviews();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error in fetching reviews", e);
                }
                return null;
            }
        };
    }


    @Override
    public void onLoadFinished(Loader<List<Review>> loader, List<Review> reviews) {
        if (reviews != null) {
            mReviewAdapter.addReviews(reviews);
        } else {
            mReviewAdapter.addReviews(new ArrayList<Review>());
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Review>> loader) {
        //Nothing to do here
    }


    public GetReviews(Context context, ReviewAdapter reviewAdapter) {
        mReviewAdapter = reviewAdapter;
        mContext = context;
    }
}
