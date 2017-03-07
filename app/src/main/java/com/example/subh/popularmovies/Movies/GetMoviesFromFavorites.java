package com.example.subh.popularmovies.Movies;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.example.subh.popularmovies.Database.FavMovieContract;

public class GetMoviesFromFavorites implements LoaderManager.LoaderCallbacks<Cursor> {

    private Context mContext;
    private Listener mListener;

    private String[] mProjections ={
            FavMovieContract.FavMovieEntry.COLUMN_ID_MOVIE,
            FavMovieContract.FavMovieEntry.COLUMN_TITLE_MOVIE,
            FavMovieContract.FavMovieEntry.COLUMN_POSTER_URL_MOVIE,
            FavMovieContract.FavMovieEntry.COLUMN_BACKDROP_URL_MOVIE,
            FavMovieContract.FavMovieEntry.COLUMN_DESCRIPTION_MOVIE,
            FavMovieContract.FavMovieEntry.COLUMN_RATING_MOVIE,
            FavMovieContract.FavMovieEntry.COLUMN_RELEASE_DATE_MOVIE
    };
    public GetMoviesFromFavorites(Context context, Listener listener){
        mContext = context;
        mListener = listener;
    }
    public interface Listener{
        void onFetch(Cursor cursor);
        void loaderCreate();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        mListener.loaderCreate();
        return new CursorLoader(mContext,
                FavMovieContract.FavMovieEntry.CONTENT_URI,
                mProjections,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mListener.onFetch(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
