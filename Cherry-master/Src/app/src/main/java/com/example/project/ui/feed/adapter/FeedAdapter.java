package com.example.project.ui.feed.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;


import com.example.project.CherryApplication;
import com.example.project.ui.feed.PictureFeedActivityFragment;
import com.example.project.R;
import com.example.project.models.Picture;

import java.util.List;


public class FeedAdapter extends RecyclerView.Adapter<FeedViewHolder> {

    //fields
    public List<Picture> data;
    private PictureFeedActivityFragment fragment;
    private CherryApplication application;


    //constructor
    public FeedAdapter(List<Picture> pics, PictureFeedActivityFragment fragment, CherryApplication app){
        this.data = pics;
        this.fragment = fragment;
        this.application = app;
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.picture_card, parent, false);
        FeedViewHolder holder = new FeedViewHolder(root, fragment, this, application);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
        holder.set(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
