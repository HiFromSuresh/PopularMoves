package com.sureshssk2006.gmail.popularmoves;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2/4/2016.
 */
public class MovieAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<TmdbMovie> movieArrayList;
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    public MovieAdapter(Context context, ArrayList<TmdbMovie> movieArray) {
        mContext = context;
        movieArrayList = movieArray;
        notifyDataSetChanged();
    }

    //View holder class to make scrolling smooth
    static class ViewHolder{
        @Bind(R.id.image_view)ImageView imageView;
        public ViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public int getCount() {
        return movieArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return movieArrayList.get(position).posterPath;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Log.v(LOG_TAG, String.valueOf(position));
        if(convertView==null){
            LayoutInflater inflater = ((MainActivity)mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_item_movie, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        Picasso.with(mContext).load(movieArrayList.get(position).posterPath()).into(holder.imageView);
        return convertView;
    }
}
