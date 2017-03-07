package com.example.subh.popularmovies.Api;

import com.example.subh.popularmovies.Movies.Movie;
import com.example.subh.popularmovies.Reviews.Review;
import com.example.subh.popularmovies.Trailers.Trailer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiService {

    @GET("{sort_by}")
    Call<Movie.AllMovies> getMovies(@Path("sort_by") String sortBy, @Query("api_key") String apiKey);
    @GET("{id}/reviews")
    Call<Review.AllReviews> getReviews(@Path("id") long movieId, @Query("api_key") String apiKey);
    @GET("{id}/videos")
    Call<Trailer.AllTrailers> getTrailers(@Path("id") long movieId, @Query("api_key") String apiKey);

}
