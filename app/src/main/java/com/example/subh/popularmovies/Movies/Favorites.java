package com.example.subh.popularmovies.Movies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.example.subh.popularmovies.Database.FavMovieContract;
import com.example.subh.popularmovies.Movies.Movie;

public class Favorites {
    private Context mContext;
    private Movie mMovie;
    private FloatingActionButton mAddToFavorites;
    private FloatingActionButton mDeleteFromFavorites;
    public Favorites(Context context, Movie movie, FloatingActionButton b1, FloatingActionButton b2){
        mContext = context;
        mMovie = movie;
        mAddToFavorites = b1;
        mDeleteFromFavorites = b2;
    }
    public void AddToFavoritesDatabase() {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                if (!inFavoriteDatabase()) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(FavMovieContract.FavMovieEntry.COLUMN_ID_MOVIE,mMovie.getMovieId());
                    contentValues.put(FavMovieContract.FavMovieEntry.COLUMN_TITLE_MOVIE, mMovie.getTitle());
                    contentValues.put(FavMovieContract.FavMovieEntry.COLUMN_POSTER_URL_MOVIE, mMovie.getPoster());
                    contentValues.put(FavMovieContract.FavMovieEntry.COLUMN_BACKDROP_URL_MOVIE, mMovie.getBackdrop());
                    contentValues.put(FavMovieContract.FavMovieEntry.COLUMN_DESCRIPTION_MOVIE, mMovie.getOverview());
                    contentValues.put(FavMovieContract.FavMovieEntry.COLUMN_RATING_MOVIE, mMovie.getRating());
                    contentValues.put(FavMovieContract.FavMovieEntry.COLUMN_RELEASE_DATE_MOVIE,
                            mMovie.getReleaseDate());

                    mContext.getContentResolver().insert(
                            FavMovieContract.FavMovieEntry.CONTENT_URI,
                            contentValues
                    );
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void params) {
                RefreshFABFavoriteButtons();
            }
        }.execute();
    }

    public void DeleteFromFavoritesDatabase() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                if (inFavoriteDatabase()) {
                    mContext.getContentResolver().delete(FavMovieContract.FavMovieEntry.CONTENT_URI,
                            FavMovieContract.FavMovieEntry.COLUMN_ID_MOVIE + " = " + mMovie.getMovieId(), null);

                }
                return null;
            }

            @Override
            protected void onPostExecute(Void params) {
                RefreshFABFavoriteButtons();
            }
        }.execute();
    }

    public void RefreshFABFavoriteButtons() {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                return inFavoriteDatabase();
            }

            @Override
            protected void onPostExecute(Boolean inFavoriteDB) {
                if (inFavoriteDB) {
                    mDeleteFromFavorites.setVisibility(View.VISIBLE);
                    mAddToFavorites.setVisibility(View.GONE);
                } else {
                    mAddToFavorites.setVisibility(View.VISIBLE);
                    mDeleteFromFavorites.setVisibility(View.GONE);
                }
            }
        }.execute();


    }

    private boolean inFavoriteDatabase() {
        Cursor movieCursor = mContext.getContentResolver().query(
                FavMovieContract.FavMovieEntry.CONTENT_URI,
                new String[]{FavMovieContract.FavMovieEntry.COLUMN_ID_MOVIE},
                FavMovieContract.FavMovieEntry.COLUMN_ID_MOVIE + " = " + mMovie.getMovieId(),
                null,
                null);

        if (movieCursor != null && movieCursor.moveToFirst()) {
            movieCursor.close();
            return true;
        } else {
            return false;
        }
    }
}

