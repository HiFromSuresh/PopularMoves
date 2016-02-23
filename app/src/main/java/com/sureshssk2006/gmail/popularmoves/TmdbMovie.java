package com.sureshssk2006.gmail.popularmoves;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2/2/2016.
 */
public class TmdbMovie implements Parcelable{

    String movieName;
    String posterPath;
    String overView;
    String releaseDate;
    String voteAverage;
    final String BASE_URL = "http://image.tmdb.org/t/p/";
    final String POSTER_SIZE = "w185";
    final String LARGE_POSTER_SIZE = "w342";

    String fullPosterPath;
    String modifiedReleaseDate;
    String modifiedVoteAverage;


    public TmdbMovie(String movieName, String posterPath, String overView, String releaseDate, String voteAverage) {
        this.posterPath = posterPath;
        this.movieName = movieName;
        this.overView = overView;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.fullPosterPath = posterPath();
        this.modifiedReleaseDate = getReleaseDate();
        this.modifiedVoteAverage = getVoteAverage();
    }

    protected TmdbMovie(Parcel in) {
        movieName = in.readString();
        overView = in.readString();
        fullPosterPath = in.readString();
        modifiedReleaseDate = in.readString();
        modifiedVoteAverage = in.readString();
    }

    public static final Creator<TmdbMovie> CREATOR = new Creator<TmdbMovie>() {
        @Override
        public TmdbMovie createFromParcel(Parcel in) {
            return new TmdbMovie(in);
        }

        @Override
        public TmdbMovie[] newArray(int size) {
            return new TmdbMovie[size];
        }
    };

    public String posterPath() {

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(POSTER_SIZE)
                .appendEncodedPath(posterPath)
                .build();
        return builtUri.toString();
        //return "http://image.tmdb.org/t/p/w185" + posterPath;

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
        //To get the first four characters of the release date which is the year
        String releaseDateYear = releaseDate.trim();
        releaseDateYear = releaseDateYear.substring(0, 4);
        return releaseDateYear;
    }

    public String getVoteAverage() {
        String voteAverageWithTotal = voteAverage + "/10";
        return voteAverageWithTotal;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieName);
        dest.writeString(overView);
        dest.writeString(fullPosterPath);
        dest.writeString(modifiedReleaseDate);
        dest.writeString(modifiedVoteAverage);
    }
}
