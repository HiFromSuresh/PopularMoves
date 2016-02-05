package com.sureshssk2006.gmail.popularmoves;

/**
 * Created by Administrator on 2/2/2016.
 */
public class TmdbMovie {
    
    String movieName;
    String posterPath;

    public TmdbMovie(String movieName, String posterPath) {
        this.posterPath = posterPath;
        this.movieName = movieName;
    }

    public String posterPath() {
        return "http://image.tmdb.org/t/p/w500" + posterPath;

    }
}
