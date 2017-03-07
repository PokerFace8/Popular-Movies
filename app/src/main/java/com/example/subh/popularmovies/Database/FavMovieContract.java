package com.example.subh.popularmovies.Database;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;


public class FavMovieContract {
    public static final String CONTENT_AUTHORITY = "com.example.subh.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String MOVIE_PATH = "movie";

    public static final class FavMovieEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(MOVIE_PATH)
                .build();

        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_ID_MOVIE = "id_movie";
        public static final String COLUMN_TITLE_MOVIE = "title";
        public static final String COLUMN_POSTER_URL_MOVIE = "poster_url";
        public static final String COLUMN_BACKDROP_URL_MOVIE = "backdrop_url";
        public static final String COLUMN_DESCRIPTION_MOVIE = "description";
        public static final String COLUMN_RATING_MOVIE = "rating";
        public static final String COLUMN_RELEASE_DATE_MOVIE = "release_date";

        public static Uri createUriMovie(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

        public static final int INDEX_COLUMN_ID_MOVIE = 0;
        public static final int INDEX_COLUMN_TITLE_MOVIE = 1;
        public static final int INDEX_COLUMN_POSTER_URL_MOVIE = 2;
        public static final int INDEX_COLUMN_BACKDROP_URL_MOVIE = 3;
        public static final int INDEX_COLUMN_DESCRIPTION_MOVIE = 4;
        public static final int INDEX_COLUMN_RATING_MOVIE = 5;
        public static final int INDEX_COLUMN_RELEASE_DATE_MOVIE = 6;

    }



}
