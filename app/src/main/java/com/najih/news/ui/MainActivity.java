package com.najih.news.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.najih.news.R;
import com.najih.news.adapter.StoryAdapter;
import com.najih.news.model.Favorite;
import com.najih.news.model.Story;
import com.najih.news.model.TopStory;
import com.najih.news.network.ApiInterfaces;
import com.najih.news.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements StoryAdapter.EventListener{
    ProgressBar progress;
    RecyclerView rv_news;
    TextView fav_content;
    private RecyclerView.LayoutManager layoutManager;
    ApiInterfaces service;
    StoryAdapter adapter;
    ArrayList<TopStory> data = new ArrayList<>();
    int maxLoad = 20;
    int currLoad = 0;
    int DETAIL_CODE = 777;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getActionBar()!=null)getActionBar().setTitle("Top Story");
        if (getSupportActionBar()!=null)getSupportActionBar().setTitle("Top Story");
        progress = (ProgressBar) findViewById(R.id.progress);
        fav_content = (TextView) findViewById(R.id.fav_content);
        rv_news = (RecyclerView) findViewById(R.id.rv_news);
        service = ApiService.gerService().create(ApiInterfaces.class);
        progress.setMax(maxLoad);
        initLayoutManager();
        getData();
    }

    void initLayoutManager(){
        layoutManager = new GridLayoutManager(this, 2);
        rv_news.setLayoutManager(layoutManager);
        rv_news.setHasFixedSize(true);
    }

    void getData(){
        service.getTopStories().enqueue(new Callback<List<Integer>>() {
            @Override
            public void onResponse(Call<List<Integer>> call, Response<List<Integer>> response) {
                List<Integer> topStories = response.body();
                for (int a = 0; a <= maxLoad; a++) {
                    service.getStory(topStories.get(a)).enqueue(new Callback<TopStory>() {
                        @Override
                        public void onResponse(Call<TopStory> call, Response<TopStory> data_res) {
                            data.add(data_res.body());
                            adapter = new StoryAdapter(data,MainActivity.this);
                            rv_news.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                            progress.setProgress(currLoad);
                            currLoad+=1;
                            if (currLoad==maxLoad)progress.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onFailure(Call<TopStory> call, Throwable t) {
                            t.printStackTrace();
                            Toast.makeText(MainActivity.this, "err", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
//                Toast.makeText(MainActivity.this, ""+topStories.size(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<Integer>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(MainActivity.this, "err", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(TopStory data) {
        Favorite.setLiked(false);
        Intent detail = new Intent(MainActivity.this,DetailStory.class);
        detail.putExtra("id",data.getId());
        startActivityForResult(detail,DETAIL_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==DETAIL_CODE){
            if (Favorite.isLiked()){
                fav_content.setText(Favorite.getTitle());
            }
        }
    }
}
