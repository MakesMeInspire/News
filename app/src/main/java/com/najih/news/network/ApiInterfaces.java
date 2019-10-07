package com.najih.news.network;

import com.najih.news.model.Comment;
import com.najih.news.model.TopStory;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterfaces {
    @GET("topstories.json?print=pretty")
    Call<List<Integer>> getTopStories();

    @GET("item/{id}.json?print=pretty")
    Call<TopStory> getStory(@Path("id") int id);

    @GET("item/{id}.json?print=pretty")
    Call<Comment> getComment(@Path("id") int id);
}
