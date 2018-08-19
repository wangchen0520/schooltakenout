package com.wy.schooltakenout.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wy.schooltakenout.Data.Article;
import com.wy.schooltakenout.R;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {
    private Context mContext;
    private List<Article> mArticleList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout linearLayout;
        ImageView foodImage;
        TextView title;
        TextView content;
        TextView time;
        public ViewHolder(View view){
            super(view);
            linearLayout=(LinearLayout) view;
            foodImage=(ImageView)view.findViewById(R.id.food);
            title=(TextView)view.findViewById(R.id.tv_title);
            content=(TextView)view.findViewById(R.id.tv_content);
            time=(TextView)view.findViewById(R.id.tv_time);
        }
    }

    public ArticleAdapter(List<Article> articleList){
        mArticleList=articleList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext==null){
            mContext=parent.getContext();
        }
        View view= LayoutInflater.from(mContext).inflate(R.layout.article_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Article article=mArticleList.get(position);
        holder.title.setText(article.getTitle());
        holder.content.setText(article.getContent());
        holder.time.setText(article.getTime());
        Glide.with(mContext).load(article.getImageId()).into(holder.foodImage);
    }

    @Override
    public int getItemCount() {
        return mArticleList.size();
    }
}
