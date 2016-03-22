package com.sureshssk2006.gmail.popularmoves;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieListFragment extends Fragment {

    private static final String STATE_MOVIES = "state_movies";
    private static final String OBJECT_KEY = "object_key";
    private MovieAdapter movieAdapter;
    ArrayList<TMDBmovieList.TmdbMovee> movieArray = new ArrayList<TMDBmovieList.TmdbMovee>();
    GridView gridView;
    //FetchMovieListTask fetchMovieListTask;
    SharedPreferences sharedPreferences;
    String sortByValue;

    final String BASE_URL = "https://api.themoviedb.org/";
    String apiKeyVAlue;
    Retrofit retrofit;
    private Call<TMDBmovieList> call;
    private TMDBmovieList movieList;
    private List<TMDBmovieList.TmdbMovee> items;

    public MovieListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.sort_by, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.popularity) {
            String sortValue = getResources().getString(R.string.popularity_desc);
            if (sharedPreferences.getString("SORT_VALUE", "vote_count.desc").equals(sortValue)) {
                //do nothing
            } else {
                ReloadPosters(sortValue);

            }
        }
        if (id == R.id.rating) {
            String sortValue = getResources().getString(R.string.rating_desc);
            if (sharedPreferences.getString("SORT_VALUE", "popularity.desc").equals(sortValue)) {
                //do nothing
            } else {
                ReloadPosters(sortValue);
            }

        }

        return super.onOptionsItemSelected(item);
    }

    private void ReloadPosters(String sortValue) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("SORT_VALUE", sortValue);
        editor.commit();
        fetchPosters(sortValue);
        //fetchMovieListTask = new FetchMovieListTask();
        //fetchMovieListTask.execute(sortValue);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        sharedPreferences = getActivity().getSharedPreferences("PrefData", Context.MODE_PRIVATE);
        sortByValue = sharedPreferences
                .getString("SORT_VALUE", getResources().getString(R.string.popularity_desc));

        View rootView = inflater.inflate(R.layout.fragment_blank, container, false);

        gridView = (GridView) rootView.findViewById(R.id.gridview);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Detailsfragment detailsfragment = new Detailsfragment();
                Bundle args = new Bundle();
                TmdbMovie tmdbObject = (TmdbMovie) movieAdapter.getMovieObject(position);
                args.putParcelable(OBJECT_KEY, tmdbObject);
                detailsfragment.setArguments(args);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, detailsfragment)
                        .addToBackStack(null).commit();

            }
        });

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiKeyVAlue = BuildConfig.TMDB_API_KEY;

        if(savedInstanceState == null) {
            fetchPosters(sortByValue);
            //fetchMovieListTask = new FetchMovieListTask();
            //fetchMovieListTask.execute(sortByValue);
        }else{
            fetchPosters(sortByValue);
            //movieArray = savedInstanceState.getParcelableArrayList(STATE_MOVIES);
            //movieAdapter = new MovieAdapter(getContext(), movieArray);
            //gridView.setAdapter(movieAdapter);
        }
        return rootView;

    }

    private void fetchPosters(String sortValue) {
        movieAdapter = new MovieAdapter(getContext(), (ArrayList<TMDBmovieList.TmdbMovee>) movieArray);
        gridView.setAdapter(movieAdapter);
        TMDBService.TMDBapi tmdBapi = retrofit.create(TMDBService.TMDBapi.class);
        call = tmdBapi.getMovies(sortValue, apiKeyVAlue);
        call.enqueue(new Callback<TMDBmovieList>() {
            @Override
            public void onResponse(Call<TMDBmovieList> call, Response<TMDBmovieList> response) {
                try {

                    Log.d("TAG", "onResponse: " + response.body().toString());
                    movieList = response.body();
                    items = movieList.getResults();
                    movieAdapter.swapList(items);
                }catch (NullPointerException e){
                    Toast.makeText(getContext(), "Null", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TMDBmovieList> call, Throwable t) {
                Toast.makeText(getContext(), "Onfailure called", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putParcelableArrayList(STATE_MOVIES, (ArrayList<? extends Parcelable>) movieArray);
    }


/*    public class FetchMovieListTask extends AsyncTask<String, Void, String> {


        private final String LOG_TAG = FetchMovieListTask.class.getSimpleName();

        @Override
        protected String doInBackground(String... params) {

            //To empty the arraylist if it is already loaded
            movieArray.clear();

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movielistJsonStr = null;

            String apiKeyVAlue = BuildConfig.TMDB_API_KEY;

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
            } finally {
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
            final String TMDB_OVERVIEW = "overview";
            final String TMDB_RELEASE_DATE = "release_date";
            final String TMDB_VOTE_AVERAGE = "vote_average";

            JSONObject movielistJson = new JSONObject(s);
            JSONArray resultsArray = movielistJson.getJSONArray(TMDB_RESULTS);

            for (int i = 0; i < resultsArray.length(); i++) {

                JSONObject movieObject = resultsArray.getJSONObject(i);
                String movieName = movieObject.getString(TMDB_MOVIE_TITLE);
                String posterPath = movieObject.getString(TMDB_POSTERPATH);
                String overview = movieObject.getString(TMDB_OVERVIEW);
                String releaseDate = movieObject.getString(TMDB_RELEASE_DATE);
                String vote_average = movieObject.getString(TMDB_VOTE_AVERAGE);

                TmdbMovie tmdbMovie = new TmdbMovie(movieName, posterPath, overview, releaseDate, vote_average);
                movieArray.add(tmdbMovie);
            }
        }
    }*/

}
