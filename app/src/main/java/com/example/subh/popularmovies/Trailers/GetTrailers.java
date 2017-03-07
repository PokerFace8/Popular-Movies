package com.example.subh.popularmovies.Trailers;

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

public class GetTrailers implements LoaderManager.LoaderCallbacks<List<Trailer>> {


    public static String LOG_TAG = GetTrailers.class.getSimpleName();
    private TrailerAdapter mTrailerAdapter;
    private Context mContext;
    public Trailer mTrailer;
    public static String KEY_FOR_MOVIE_ID = "movie_id";

    @Override
    public Loader<List<Trailer>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<List<Trailer>>(mContext) {
            @Override
            protected void onStartLoading() {
                if (null == args) {
                    return;
                }
                forceLoad();
            }

            @Override
            public List<Trailer> loadInBackground() {
                long movieId = args.getLong(KEY_FOR_MOVIE_ID);
                ApiService service = ApiClient.getClient().create(ApiService.class);
                Call<Trailer.AllTrailers> call = service.getTrailers(movieId, BuildConfig.TMDbApiKey);
                try {
                    Response<Trailer.AllTrailers> response = call.execute();
                    Trailer.AllTrailers trailers = response.body();
                    return trailers.getTrailers();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error fetching trailers ", e);
                }

                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Trailer>> loader, List<Trailer> trailers) {
        if (trailers != null) {
            mTrailerAdapter.addTrailers(trailers);
            mTrailer = trailers.get(0);

        } else {
            mTrailerAdapter.addTrailers(new ArrayList<Trailer>());
            mTrailer = null;
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Trailer>> loader) {

    }

    public GetTrailers(Context context, TrailerAdapter trailerAdapter) {
        mTrailerAdapter = trailerAdapter;
        mContext = context;
    }
}
