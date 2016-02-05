package com.sureshssk2006.gmail.popularmoves;

import android.net.Uri;

/**
 * Created by Administrator on 2/2/2016.
 */
public class TmdbMovie {
    
    String movieName;
    String posterPath;
    final String BASE_URL = "http://image.tmdb.org/t/p/";
    final String POSTER_SIZE = "w185";

    public TmdbMovie(String movieName, String posterPath) {
        this.posterPath = posterPath;
        this.movieName = movieName;
    }

    public String posterPath() {

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(POSTER_SIZE)
                .appendEncodedPath(posterPath)
                .build();
        return builtUri.toString();
        //return "http://image.tmdb.org/t/p/w500" + posterPath;

    }
}
