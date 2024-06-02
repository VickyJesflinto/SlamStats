package com.example.slamstat.api;

import com.example.slamstat.models.NBANews;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface ApiService {
    String RAPID_API_KEY = "5abac1bbbcmsh7caa37cb8c257e6p12257bjsn02d2e52ffb10";
    String RAPID_API_HOST = "nba-latest-news.p.rapidapi.com";

    @Headers({
            "X-RapidAPI-Key:" + RAPID_API_KEY,
            "X-RapidAPI-Host:" + RAPID_API_HOST})
    @GET("articles")
    Call<List<NBANews>> getNBANews();

}
