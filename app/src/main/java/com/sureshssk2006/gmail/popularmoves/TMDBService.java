package com.sureshssk2006.gmail.popularmoves;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 3/14/2016.
 */
public final class TMDBService {

    public interface TMDBapi {

        @GET("3/discover/movie?")
        Call<TMDBmovieList> getMovies(
            @Query("sort_by") String sortByValue,
            @Query("api_key") String apiKeyVAlue);
    }
}
