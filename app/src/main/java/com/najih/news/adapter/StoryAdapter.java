package com.najih.news.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.najih.news.R;
import com.najih.news.model.TopStory;

import java.util.ArrayList;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.vh>{
    private ArrayList<TopStory> list_story;
    EventListener listener;

    public StoryAdapter(ArrayList<TopStory> list_story, EventListener listener) {
        this.list_story = list_story;
        this.listener = listener;
    }

    public interface EventListener {
        void onClick(TopStory data);
    }

    @NonNull
    @Override
    public vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item,parent,false);
        return new StoryAdapter.vh(v);
    }

    @Override
    public void onBindViewHolder(@NonNull vh holder, int position) {
        holder.title.setText(list_story.get(position).getTitle());
        holder.comment.setText(""+list_story.get(position).getDescendants());
        holder.score.setText(""+list_story.get(position).getScore());
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(list_story.get(position));
            }
        });
        holder.cv_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(list_story.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_story.size();
    }

    class vh extends RecyclerView.ViewHolder{
        TextView title,comment,score;
        CardView cv_item;
        public vh(@NonNull View itemView) {
            super(itemView);

            cv_item = (CardView) itemView.findViewById(R.id.cv_item);
            title = (TextView)itemView.findViewById(R.id.title);
            comment = (TextView)itemView.findViewById(R.id.comment_count);
            score = (TextView)itemView.findViewById(R.id.score);
        }
    }
}
