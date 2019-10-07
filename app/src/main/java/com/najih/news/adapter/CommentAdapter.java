package com.najih.news.adapter;

import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.najih.news.R;
import com.najih.news.model.Comment;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.vh>{
    private ArrayList<Comment> list_comment;

    public CommentAdapter(ArrayList<Comment> list_comment) {
        this.list_comment = list_comment;
    }

    @NonNull
    @Override
    public vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item,parent,false);
        return new CommentAdapter.vh(v);
    }

    @Override
    public void onBindViewHolder(@NonNull vh holder, int position) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.comment.setText(Html.fromHtml(list_comment.get(position).getText(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.comment.setText(Html.fromHtml(list_comment.get(position).getText()));
        }
        holder.owner.setText(list_comment.get(position).getBy());
    }

    @Override
    public int getItemCount() {
        return list_comment.size();
    }

    class vh extends RecyclerView.ViewHolder{
        TextView comment,owner;
        public vh(@NonNull View itemView) {
            super(itemView);
            comment = (TextView) itemView.findViewById(R.id.comment);
            owner = (TextView) itemView.findViewById(R.id.owner);
        }
    }
}
