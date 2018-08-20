package com.wy.schooltakenout.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wy.schooltakenout.Activity.ArticleActivity;
import com.wy.schooltakenout.Activity.ArticleListActivity;
import com.wy.schooltakenout.Data.Tag;
import com.wy.schooltakenout.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder>{
    private Context mContext;
    private List<Tag> mTagList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        CircleImageView tagImage;
        TextView tagName;

        public ViewHolder(View view){
            super(view);
            cardView=(CardView)view;
            tagImage=(CircleImageView) view.findViewById(R.id.tag_image);
            tagName=(TextView)view.findViewById(R.id.tag_name);
        }
    }

    public TagAdapter(List<Tag> tagList){
        mTagList=tagList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext==null){
            mContext=parent.getContext();
        }
        View view= LayoutInflater.from(mContext).inflate(R.layout.sortlist_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tag tag=mTagList.get(position);
        holder.tagName.setText(tag.getName());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, ArticleListActivity.class);
                mContext.startActivity(intent);
            }
        });
        Glide.with(mContext).load(tag.getImageId()).into(holder.tagImage);
    }

    @Override
    public int getItemCount() {
        return mTagList.size();
    }
}
