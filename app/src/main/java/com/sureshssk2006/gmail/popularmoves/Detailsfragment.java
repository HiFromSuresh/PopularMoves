package com.sureshssk2006.gmail.popularmoves;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class Detailsfragment extends Fragment {

    private final String OBJECT_KEY = "object_key";
    //member variables
    private TextView mTitleTextView;
    private ImageView mImageView;
    private TextView mOverviewTextView;
    private TextView mReleasedateTextView;
    private TextView mRatingTextView;

    public Detailsfragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_details, container, false);

        //Typecasting
        mTitleTextView = (TextView) rootView.findViewById(R.id.details_original_title);
        mImageView = (ImageView) rootView.findViewById(R.id.details_image_poster);
        mOverviewTextView = (TextView) rootView.findViewById(R.id.details_plot_synopsis);
        mRatingTextView = (TextView) rootView.findViewById(R.id.details_rating);
        mReleasedateTextView = (TextView) rootView.findViewById(R.id.details_release_date);

        //get Data from bundle
        Bundle bundle = this.getArguments();

        TmdbMovie tmdbMovie = bundle.getParcelable(OBJECT_KEY);

        mTitleTextView.setText(tmdbMovie.getMovieName());
        Picasso.with(getContext()).load(tmdbMovie.posterPath()).into(mImageView);
        mOverviewTextView.setText(tmdbMovie.getOverView());
        mRatingTextView.setText(tmdbMovie.getVoteAverage());
        mReleasedateTextView.setText(tmdbMovie.getReleaseDate());

        return rootView;
    }

}
