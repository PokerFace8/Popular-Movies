package com.example.subh.popularmovies.UI;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.subh.popularmovies.Movies.GetMoviesFromFavorites;
import com.example.subh.popularmovies.Movies.GetMoviesFromNetwork;
import com.example.subh.popularmovies.Movies.Movie;
import com.example.subh.popularmovies.Movies.MovieAdapter;
import com.example.subh.popularmovies.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivityMovieList extends AppCompatActivity implements GetMoviesFromFavorites.Listener,
        GetMoviesFromNetwork.Listener, MovieAdapter.Callbacks {

    private static final String ON_SAVE_MOVIES = "ON_SAVE_MOVIES";
    private static final String PREF_SORT_BY = "PREF_SORT_BY";
    private static final int FAVORITE_MOVIES_LOADER = 201;
    private static final int POPULAR_MOVIES_LOADER = 202;
    private static final int TOP_RATED_MOVIES_LOADER = 203;

    private boolean mTwoPane;
    private MovieAdapter mAdapter;
    private String mSortBy = GetMoviesFromNetwork.MOST_POPULAR;

    @Bind(R.id.movie_list)
    RecyclerView mRecyclerView;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_list_activity);
        ButterKnife.bind(this);

        mToolbar.setTitle(R.string.title_movie_list);
        setSupportActionBar(mToolbar);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, getResources()
                .getInteger(R.integer.number_of_columns)));
        mAdapter = new MovieAdapter(new ArrayList<Movie>(), this);
        mRecyclerView.setAdapter(mAdapter);

        mTwoPane = (findViewById(R.id.movie_detail_container) != null);

        if (savedInstanceState != null) {
            mSortBy = savedInstanceState.getString(PREF_SORT_BY);
            if (savedInstanceState.containsKey(ON_SAVE_MOVIES)) {
                List<Movie> movies = savedInstanceState.getParcelableArrayList(ON_SAVE_MOVIES);
                mAdapter.addFromList(movies);
                findViewById(R.id.progressBar).setVisibility(View.GONE);
                if (mSortBy.equals(GetMoviesFromNetwork.FAVORITES)) {
                    getSupportLoaderManager()
                            .initLoader(FAVORITE_MOVIES_LOADER, null
                                    ,new GetMoviesFromFavorites(this,this));
                }
            }
        } else {
            fetchMovies();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<Movie> movies = mAdapter.getMovies();
        if (movies != null && !movies.isEmpty()) {
            outState.putParcelableArrayList(ON_SAVE_MOVIES, movies);
        }
        outState.putString(PREF_SORT_BY, mSortBy);

        if(!mSortBy.equals(GetMoviesFromNetwork.TOP_RATED))
            getSupportLoaderManager().destroyLoader(TOP_RATED_MOVIES_LOADER);
        if(!mSortBy.equals(GetMoviesFromNetwork.MOST_POPULAR))
            getSupportLoaderManager().destroyLoader(POPULAR_MOVIES_LOADER);
        if (!mSortBy.equals(GetMoviesFromNetwork.FAVORITES)) {
            getSupportLoaderManager().destroyLoader(FAVORITE_MOVIES_LOADER);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movie_list_activity, menu);

        switch (mSortBy) {
            case GetMoviesFromNetwork.MOST_POPULAR:
                menu.findItem(R.id.sort_by_most_popular).setChecked(true);
                break;
            case GetMoviesFromNetwork.TOP_RATED:
                menu.findItem(R.id.sort_by_top_rated).setChecked(true);
                break;
            case GetMoviesFromNetwork.FAVORITES:
                menu.findItem(R.id.sort_by_favorites).setChecked(true);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by_top_rated:
                if (mSortBy.equals(GetMoviesFromNetwork.FAVORITES)) {
                    getSupportLoaderManager().destroyLoader(FAVORITE_MOVIES_LOADER);
                }
                else if(mSortBy.equals(GetMoviesFromNetwork.MOST_POPULAR)){
                    getSupportLoaderManager().destroyLoader(POPULAR_MOVIES_LOADER);
                }
                mSortBy = GetMoviesFromNetwork.TOP_RATED;
                fetchMovies();
                item.setChecked(true);
                break;
            case R.id.sort_by_most_popular:
                if (mSortBy.equals(GetMoviesFromNetwork.FAVORITES)) {
                    getSupportLoaderManager().destroyLoader(FAVORITE_MOVIES_LOADER);
                }
                else if(mSortBy.equals(GetMoviesFromNetwork.TOP_RATED)){
                    getSupportLoaderManager().destroyLoader(TOP_RATED_MOVIES_LOADER);
                }
                mSortBy = GetMoviesFromNetwork.MOST_POPULAR;
                fetchMovies();
                item.setChecked(true);
                break;
            case R.id.sort_by_favorites:
                if(mSortBy.equals(GetMoviesFromNetwork.MOST_POPULAR)){
                    getSupportLoaderManager().destroyLoader(POPULAR_MOVIES_LOADER);
                }
                else if(mSortBy.equals(GetMoviesFromNetwork.TOP_RATED)){
                    getSupportLoaderManager().destroyLoader(TOP_RATED_MOVIES_LOADER);
                }
                mSortBy = GetMoviesFromNetwork.FAVORITES;
                item.setChecked(true);
                fetchMovies();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void openMovieDetails(Movie movie, int position) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(MovieDetailFragment.ON_SAVE_MOVIE, movie);
            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra(MovieDetailFragment.ON_SAVE_MOVIE, movie);
            startActivity(intent);
        }
    }

    @Override
    public void onFetchFinished(List<Movie> movies) {
        mAdapter.addFromList(movies);
        findViewById(R.id.progressBar).setVisibility(View.GONE);
    }

    @Override
    public void onFetch(Cursor cursor) {
        mAdapter.addFromCursor(cursor);
        findViewById(R.id.progressBar).setVisibility(View.GONE);
    }

    @Override
    public void loaderCreate() {
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
    }

    private void fetchMovies() {
        if (!mSortBy.equals(GetMoviesFromNetwork.FAVORITES)) {
            GetMoviesFromNetwork getMoviesFromNetwork =
                    new GetMoviesFromNetwork(this,this.getApplicationContext());
            findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
            Bundle bundle = new Bundle();
            bundle.putString(GetMoviesFromNetwork.SORT_KEY,mSortBy);
            if(mSortBy.equals(GetMoviesFromNetwork.MOST_POPULAR)) {
                getSupportLoaderManager().initLoader(POPULAR_MOVIES_LOADER, bundle, getMoviesFromNetwork);
            }
            else if(mSortBy.equals(GetMoviesFromNetwork.TOP_RATED)) {
                getSupportLoaderManager().initLoader(TOP_RATED_MOVIES_LOADER, bundle, getMoviesFromNetwork);
            }
        } else {
            GetMoviesFromFavorites getMoviesFromFavorites = new GetMoviesFromFavorites(this,this);
            getSupportLoaderManager().initLoader(FAVORITE_MOVIES_LOADER, null, getMoviesFromFavorites);
        }
    }

}
