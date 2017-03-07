package com.example.subh.popularmovies.Database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

public class FavMoviesProvider extends ContentProvider{


    private static final int MOVIES = 100;
    private static final UriMatcher uriMatcher = BuildUriMatcher();
    private FavMovieDBHelper favMovieDBHelper;

    private static UriMatcher BuildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        String authority = FavMovieContract.CONTENT_AUTHORITY;
        String path = FavMovieContract.MOVIE_PATH;
        uriMatcher.addURI(authority, path, MOVIES);
        return uriMatcher;
    }
    @Override
    public boolean onCreate() {
        favMovieDBHelper = new FavMovieDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArguments,
                        String sortingOrder) {
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case MOVIES: {
                cursor = favMovieDBHelper.getReadableDatabase().query(
                        FavMovieContract.FavMovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArguments,
                        null,
                        null,
                        sortingOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (getContext() != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    //Not required in the current version of the app.
    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = favMovieDBHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case MOVIES: {
                long id = db.insert(FavMovieContract.FavMovieEntry.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = FavMovieContract.FavMovieEntry.createUriMovie(id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        final SQLiteDatabase db = favMovieDBHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowsDeleted;

        if (s==null) s = "1";
        switch (match) {
            case MOVIES: {
                rowsDeleted = db.delete(
                        FavMovieContract.FavMovieEntry.TABLE_NAME, s, strings);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted != 0 && getContext() != null)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        final SQLiteDatabase db = favMovieDBHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case MOVIES: {
                rowsUpdated = db.update(FavMovieContract.FavMovieEntry.TABLE_NAME, contentValues,
                        s, strings);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0 && getContext() != null)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsUpdated;
    }
}
