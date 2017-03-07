package com.example.subh.popularmovies.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavMovieDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "fav_movies.db";

    public FavMovieDBHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_SQL_TABLE_COMMAND = "CREATE TABLE "
                + FavMovieContract.FavMovieEntry.TABLE_NAME + " ("
                + FavMovieContract.FavMovieEntry._ID + "INTEGER PRIMARY KEY, "
                + FavMovieContract.FavMovieEntry.COLUMN_ID_MOVIE + " INTEGER NOT NULL, "
                + FavMovieContract.FavMovieEntry.COLUMN_TITLE_MOVIE + " TEXT NOT NULL, "
                + FavMovieContract.FavMovieEntry.COLUMN_POSTER_URL_MOVIE + " TEXT NOT NULL, "
                + FavMovieContract.FavMovieEntry.COLUMN_BACKDROP_URL_MOVIE + " TEXT NOT NULL, "
                + FavMovieContract.FavMovieEntry.COLUMN_DESCRIPTION_MOVIE + " TEXT NOT NULL, "
                + FavMovieContract.FavMovieEntry.COLUMN_RATING_MOVIE + " TEXT NOT NULL, "
                + FavMovieContract.FavMovieEntry.COLUMN_RELEASE_DATE_MOVIE + " TEXT NOT NULL "
                + " );";

        sqLiteDatabase.execSQL(CREATE_SQL_TABLE_COMMAND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TALBE IF EXISTS " + FavMovieContract.FavMovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
