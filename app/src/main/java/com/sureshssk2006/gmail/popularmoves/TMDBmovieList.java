package com.sureshssk2006.gmail.popularmoves;

import java.util.List;

/**
 * Created by Administrator on 3/14/2016.
 */
public class TMDBmovieList {


    public List<TmdbMovee> getResults() {
        return results;
    }

    public void setResults(List<TmdbMovee> results) {
        this.results = results;
    }

    private List<TmdbMovee> results;


    public static class TmdbMovee {

        String results;
        String original_title;
        String poster_path;
        String overview;
        String release_date;
        String vote_average;

        public String getPoster_path() {
            return poster_path;
        }

        public void setPoster_path(String poster_path) {
            this.poster_path = poster_path;
        }

        public String getOriginal_title() {
            return original_title;
        }

        public void setOriginal_title(String original_title) {
            this.original_title = original_title;
        }

        public String getOverview() {
            return overview;
        }

        public void setOverview(String overview) {
            this.overview = overview;
        }

        public String getRelease_date() {
            return release_date;
        }

        public void setRelease_date(String release_date) {
            this.release_date = release_date;
        }

        public String getVote_average() {
            return vote_average;
        }

        public void setVote_average(String vote_average) {
            this.vote_average = vote_average;
        }

        public String getResults() {
            return results;
        }

        public void setResults(String results) {
            this.results = results;
        }



    }
}
