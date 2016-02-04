package com.sureshssk2006.gmail.popularmoves;


import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieListFragment extends Fragment {

    private MovieAdapter movieAdapter;
    List<TmdbMovie> movieArray = new ArrayList<TmdbMovie>();
    GridView gridView;

    public MovieListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String sortByVAlue = "popularity.desc";//To be brought from the user prefs from settings
        FetchMovieListTask fetchMovieListTask = new FetchMovieListTask();
        fetchMovieListTask.execute(sortByVAlue);

        View rootView = inflater.inflate(R.layout.fragment_blank, container, false);

        gridView = (GridView)rootView.findViewById(R.id.gridview);

        return rootView;

    }

    public class FetchMovieListTask extends AsyncTask<String, Void, String> {


        private final String LOG_TAG = FetchMovieListTask.class.getSimpleName();
        List<TmdbMovie> movieArray = new ArrayList<TmdbMovie>();

        @Override
        protected String doInBackground(String... params) {

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movielistJsonStr = null;

            String apiKeyVAlue = "";//Have to add api key mannually

            try {
                // Construct the URL for the TMDB query
                // Possible parameters are avaiable at TMDB API page
                final String BASE_URL = "https://api.themoviedb.org/3/discover/movie?";
                final String SORT_PARAM = "sort_by";
                final String API_KEY = "api_key";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_PARAM, params[0])
                        .appendQueryParameter(API_KEY, apiKeyVAlue)
                        .build();
                URL url = new URL(builtUri.toString());
                //URL url = new URL("https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=");

                // Create the request to TMDB, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movielistJsonStr = buffer.toString();

                Log.v(LOG_TAG, movielistJsonStr);
                try {
                    getMovieDataFromJson(movielistJsonStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            movieAdapter = new MovieAdapter(getContext(), (ArrayList<TmdbMovie>) movieArray);
            gridView.setAdapter(movieAdapter);
        }

        private void getMovieDataFromJson(String s) throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String TMDB_RESULTS = "results";
            final String TMDB_MOVIE_TITLE = "original_title";
            final String TMDB_POSTERPATH = "poster_path";

            JSONObject movielistJson = new JSONObject(s);
            JSONArray resultsArray = movielistJson.getJSONArray(TMDB_RESULTS);

            for (int i = 0; i < resultsArray.length(); i++){
                String movieName;
                String posterPath;

                JSONObject movieObject = resultsArray.getJSONObject(i);
                movieName = movieObject.getString(TMDB_MOVIE_TITLE);
                posterPath = movieObject.getString(TMDB_POSTERPATH);

                TmdbMovie tmdbMovie = new TmdbMovie(movieName, posterPath);
                movieArray.add(tmdbMovie);
                Log.v(LOG_TAG, movieName);
            }
        }
    }

}
