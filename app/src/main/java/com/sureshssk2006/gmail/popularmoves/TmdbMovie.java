package com.sureshssk2006.gmail.popularmoves;

import android.net.Uri;

/**
 * Created by Administrator on 2/2/2016.
 */
public class TmdbMovie {
    
    String movieName;
    String posterPath;
    String overView;
    String releaseDate;
    String voteAverage;
    final String BASE_URL = "http://image.tmdb.org/t/p/";
    final String POSTER_SIZE = "w185";
    final String LARGE_POSTER_SIZE = "w342";

    public TmdbMovie(String movieName, String posterPath, String overView, String releaseDate, String voteAverage) {
        this.posterPath = posterPath;
        this.movieName = movieName;
        this.overView = overView;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
    }

    public String posterPath() {

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(POSTER_SIZE)
                .appendEncodedPath(posterPath)
                .build();
        return builtUri.toString();
        //return "http://image.tmdb.org/t/p/w500" + posterPath;

    }

    public String largePosterPath() {

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(LARGE_POSTER_SIZE)
                .appendEncodedPath(posterPath)
                .build();
        return builtUri.toString();

    }

    public String getMovieName() {
        return movieName;
    }

    public String getOverView() {
        return overView;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getVoteAverage() {
        return voteAverage;
    }
}
