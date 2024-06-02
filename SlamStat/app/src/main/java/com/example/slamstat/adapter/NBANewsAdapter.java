package com.example.slamstat.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slamstat.DetailNewsActivity;
import com.example.slamstat.models.NBANews;
import com.example.slamstat.R;

import java.util.List;

public class NBANewsAdapter extends RecyclerView.Adapter<NBANewsAdapter.ViewHolder> {
    private List<NBANews> nbaNews;
    private Context context;

    public NBANewsAdapter(List<NBANews> nbaNews, Context context) {
        this.nbaNews = nbaNews;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NBANews nbaNews1 = nbaNews.get(position);
        holder.source.setText(nbaNews1.getSource());
        holder.title.setText(nbaNews1.getTitle());
        holder.url.setText(nbaNews1.getUrl());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailNewsActivity.class);
            intent.putExtra("title", nbaNews1.getTitle());
            intent.putExtra("source", nbaNews1.getSource());
            intent.putExtra("url", nbaNews1.getUrl());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return nbaNews.size();
    }

    public void updateList(List<NBANews> filteredNews) {
        nbaNews = filteredNews;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView source;
        public TextView title;
        public TextView url;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            source = itemView.findViewById(R.id.tv_source);
            title = itemView.findViewById(R.id.tv_title);
            url = itemView.findViewById(R.id.tv_url);
        }
    }
}
