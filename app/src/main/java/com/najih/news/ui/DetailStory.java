package com.najih.news.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.najih.news.R;
import com.najih.news.adapter.CommentAdapter;
import com.najih.news.model.Comment;
import com.najih.news.model.Favorite;
import com.najih.news.model.TopStory;
import com.najih.news.network.ApiInterfaces;
import com.najih.news.network.ApiService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailStory extends AppCompatActivity {
    boolean like_data = false;
    ImageView favorite;
    TextView title, owner, date, desc, comment_title;
    int id = 0;
    ArrayList<Comment> data = new ArrayList<>();
    ApiInterfaces service;
    CommentAdapter adapter;
    RecyclerView rv_comment;
    ProgressBar progress;
    NestedScrollView nsv_main;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_story);
        if (getActionBar() != null) getActionBar().setTitle("Story Detail");
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Story Detail");
        favorite = (ImageView) findViewById(R.id.favorite);
        title = (TextView) findViewById(R.id.title);
        owner = (TextView) findViewById(R.id.owner);
        date = (TextView) findViewById(R.id.date);
        desc = (TextView) findViewById(R.id.desc);
        comment_title = (TextView) findViewById(R.id.comment_title);
        rv_comment = (RecyclerView) findViewById(R.id.rv_comment);
        progress = (ProgressBar) findViewById(R.id.progress);
        nsv_main = (NestedScrollView) findViewById(R.id.nsv_main);
        service = ApiService.gerService().create(ApiInterfaces.class);

        showLoading(true);
        cekFavorite();
        initLayoutManager();
        getExtras();
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickFav();
            }
        });
    }

    void initLayoutManager() {
        layoutManager = new LinearLayoutManager(this);
        rv_comment.setLayoutManager(layoutManager);
        rv_comment.setHasFixedSize(true);
    }

    void getExtras() {
        Bundle data = getIntent().getExtras();
        if (data != null) {
            id = data.getInt("id", 0);
            getData();
        }
    }

    void showLoading(boolean a) {
        if (a) {
            nsv_main.setVisibility(View.INVISIBLE);
            progress.setVisibility(View.VISIBLE);
        } else {
            nsv_main.setVisibility(View.VISIBLE);
            progress.setVisibility(View.INVISIBLE);
        }
    }

    void getData() {
        service.getStory(id).enqueue(new Callback<TopStory>() {
            @Override
            public void onResponse(Call<TopStory> call, Response<TopStory> response) {
                title.setText(response.body().getTitle());
                if (response.body().getTitle().equalsIgnoreCase(Favorite.getTitle())) {
                    like_data = true;
                    cekFavorite();
                }
                owner.setText(response.body().getBy());
                date.setText(getDate(response.body().getTime()));
                // deskripsi masih kurang belum paham api di documentationya :(
                if (response.body().getDescendants() != null) {
                    comment_title.setText("Comment (" + response.body().getDescendants() + ") : ");
                    if (response.body().getKids() != null) {
                        for (int a = 0; a < response.body().getKids().size(); a++) {
                            service.getComment(response.body().getKids().get(a)).enqueue(new Callback<Comment>() {
                                @Override
                                public void onResponse(Call<Comment> call, Response<Comment> comn) {
//                                Log.d("COMMENT",comn.body().getText());
                                    data.add(comn.body());
                                    adapter = new CommentAdapter(data);
                                    rv_comment.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onFailure(Call<Comment> call, Throwable t) {
                                    t.printStackTrace();
                                }
                            });
                        }
                    }
                }
                showLoading(false);
            }

            @Override
            public void onFailure(Call<TopStory> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public static String getDate(long milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    void cekFavorite() {
        if (like_data) {
            favorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorited));
        } else {
            favorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite));
        }
    }

    void onClickFav() {
        if (like_data) {
            like_data = false;
        } else {
            like_data = true;
        }
        cekFavorite();
    }

    @Override
    public void onBackPressed() {
        if (like_data) {
            Favorite.setLiked(true);
            Favorite.setTitle(title.getText().toString());

            //Error kaga bisa entah apa yang merasukinya
            Intent returnIntent = getIntent();
            returnIntent.putExtra("result", title.getText().toString());
            setResult(RESULT_OK, returnIntent);
            finish();
        }
        super.onBackPressed();
    }
}
