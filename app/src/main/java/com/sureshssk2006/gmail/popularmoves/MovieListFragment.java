package com.sureshssk2006.gmail.popularmoves;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieListFragment extends Fragment {


    public MovieListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FetchMovieListTask fetchMovieListTask = new FetchMovieListTask();
        fetchMovieListTask.execute();

        return inflater.inflate(R.layout.fragment_blank, container, false);

    }

    public class FetchMovieListTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            return null;
        }
    }

}
