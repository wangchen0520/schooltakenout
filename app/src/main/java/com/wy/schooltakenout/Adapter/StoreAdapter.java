package com.wy.schooltakenout.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wy.schooltakenout.Data.Store;
import com.wy.schooltakenout.R;

import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder>{
	private Context context;
	private List<Store> storeList;

    public StoreAdapter(List<Store> storeList){
        this.storeList=storeList;
    }

	//用于连接Store列表的项中的各个View
	static class ViewHolder extends RecyclerView.ViewHolder{
	    CardView itemView;
		ImageView storeImage;
		TextView storeName;
		LinearLayout storeTags;

		private ViewHolder(View view){
			super(view);

            itemView=(CardView) view;
			storeImage=view.findViewById(R.id.store_img);
			storeName=view.findViewById(R.id.store_name);
			storeTags=view.findViewById(R.id.store_tags);
		}
    }

	//初始时调用，连接layout
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		if(context==null){
			context=parent.getContext();
		}
		View view= LayoutInflater.from(context).inflate(R.layout.store_item,parent,false);
		return new ViewHolder(view);
	}

	//用于生成Store列表中各个项
	@Override
	public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
	    //官方建议不直接使用position，因为可能会变
        position = holder.getAdapterPosition();
		Store store = storeList.get(position);

		holder.storeName.setText(store.getStoreName());
		Glide.with(context).load(store.getStoreImg()).into(holder.storeImage);
		//防止标签重复生成
        holder.storeTags.removeAllViews();
        //获取屏幕dpi，使标签可以正常显示（pixel会受分辨率影响，需要转化为dp）
        DisplayMetrics metric = context.getResources().getDisplayMetrics();
        double ddpi = metric.densityDpi / 160.0;
        //动态添加标签
		for(String storeTag: store.getStoreTags()) {
			TextView tagView = new TextView(context);
			tagView.setWidth((int) (holder.storeTags.getMeasuredHeight() * 2 * ddpi));
            tagView.setHeight((int) (holder.storeTags.getMeasuredHeight() * ddpi));
			tagView.setText(storeTag);
			tagView.setTextSize(10);
			tagView.setGravity(Gravity.CENTER);
			tagView.setBackground(context.getResources().getDrawable(R.drawable.ic_tag));
			holder.storeTags.addView(tagView);
		}

		//设置点击响应
        if(onItemClickListener!= null){
            holder.itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                	onItemClickListener.onClick(holder.getAdapterPosition(), storeList.get(holder.getAdapterPosition()));
                }
            });
        }
	}

	@Override
	public int getItemCount() {
		return storeList.size();
	}

	//设置点击响应
    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
        void onClick(int position, Store thisStore);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this.onItemClickListener=onItemClickListener;
    }
}
