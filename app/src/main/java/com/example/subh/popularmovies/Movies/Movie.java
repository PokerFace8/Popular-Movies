package com.example.subh.popularmovies.Movies;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Movie implements Parcelable {

    public static final String LOG_TAG = Movie.class.getSimpleName();
    public static final float POSTER_ASPECT_RATIO = 1.5f;

    @SerializedName("id")
    private long mMovieId;
    @SerializedName("original_title")
    private String mTitle;
    @SerializedName("poster_path")
    private String mPoster;
    @SerializedName("backdrop_path")
    private String mBackdrop;
    @SerializedName("overview")
    private String mOverview;
    @SerializedName("vote_average")
    private String mRating;
    @SerializedName("release_date")
    private String mReleaseDate;


    private Movie(){}


    public Movie(long id, String title, String poster, String backdrop, String overview, String userRating,
                 String releaseDate) {
        mMovieId = id;
        mTitle = title;
        mPoster = poster;
        mOverview = overview;
        mRating = userRating;
        mReleaseDate = releaseDate;
        mBackdrop = backdrop;
    }

    @Nullable
    public String getTitle() {
        return mTitle;
    }

    public long getMovieId() {
        return mMovieId;
    }

    @Nullable
    public String getPosterUrl() {
        if (mPoster != null && !mPoster.isEmpty()) {
            String posterUrl =  "http://image.tmdb.org/t/p/w342" + mPoster;
            return posterUrl;
        }
        return null;
    }

    public String getPoster() {
        return this.mPoster;
    }

    @Nullable
    public String getBackdropUrl() {
        if (mBackdrop != null && !mBackdrop.isEmpty()) {
            return "http://image.tmdb.org/t/p/original" + mBackdrop;
        }
        return null;
    }

    public String getBackdrop() {
        return mBackdrop;
    }
    public String getFormattedReleaseDate() {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormatormat = new SimpleDateFormat(pattern, Locale.UK);
        if (mReleaseDate != null && !mReleaseDate.isEmpty()) {
            try {
                Date date = simpleDateFormatormat.parse(mReleaseDate);
                return DateFormat.getDateInstance().format(date);
            } catch (ParseException e) {
                Log.e(LOG_TAG, "Release date not parsed " + mReleaseDate);
            }
        } else {
            mReleaseDate = "Release date not available";
        }

        return this.mReleaseDate;
    }

    public String getReleaseDate() {
        return this.mReleaseDate;
    }

    @Nullable
    public String getOverview() {
        return this.mOverview;
    }

    @Nullable
    public String getRating() {
        return this.mRating;
    }


    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        public Movie createFromParcel(Parcel source) {
            Movie movie = new Movie();
            movie.mMovieId = source.readLong();
            movie.mTitle = source.readString();
            movie.mPoster = source.readString();
            movie.mBackdrop = source.readString();
            movie.mOverview = source.readString();
            movie.mRating = source.readString();
            movie.mReleaseDate = source.readString();
            return movie;
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(mMovieId);
        parcel.writeString(mTitle);
        parcel.writeString(mPoster);
        parcel.writeString(mBackdrop);
        parcel.writeString(mOverview);
        parcel.writeString(mRating);
        parcel.writeString(mReleaseDate);
    }

    public class AllMovies {

        @SerializedName("results")
        private List<Movie> movies = new ArrayList<>();

        public List<Movie> getMovies() {
            return movies;
        }
    }
}
