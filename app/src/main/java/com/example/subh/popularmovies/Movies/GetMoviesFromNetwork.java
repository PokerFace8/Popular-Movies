package com.example.subh.popularmovies.Movies;

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

public class GetMoviesFromNetwork implements LoaderManager.LoaderCallbacks<List<Movie>> {

    @SuppressWarnings("unused")
    public static String LOG_TAG = GetMoviesFromNetwork.class.getSimpleName();

    public final static String MOST_POPULAR = "popular";
    public final static String TOP_RATED = "top_rated";
    public final static String FAVORITES = "favorites";
    public final static String SORT_KEY = "key";
    private Listener mListener;
    private Context mContext;
    private String mSortBy;

    public GetMoviesFromNetwork(Listener listener, Context context) {
        mListener = listener;
        mContext = context;
    }

    public interface Listener {
        void onFetchFinished(List<Movie> movies);
    }
    @Override
    public Loader<List<Movie>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<List<Movie>>(mContext) {
            @Override
            protected void onStartLoading() {
                forceLoad();
            }

            @Override
            public List<Movie> loadInBackground() {
                mSortBy = args.getString(SORT_KEY,MOST_POPULAR);
                Log.v("GETMOVIESFROM","NETWORK");
                ApiService service = ApiClient.getClient().create(ApiService.class);
                Call<Movie.AllMovies> call = service.getMovies(mSortBy, BuildConfig.TMDbApiKey);
                try {
                    Response<Movie.AllMovies> response = call.execute();
                    Movie.AllMovies movies = response.body();
                    return movies.getMovies();

                } catch (IOException e) {
                    Log.e(LOG_TAG, e.toString());
                }
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
        List<Movie> movieList;
        if (movies != null) {
            movieList = movies;
        } else {
            movieList = new ArrayList<>();
        }
        mListener.onFetchFinished(movieList);
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {

    }

}
