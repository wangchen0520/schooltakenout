package com.wy.schooltakenout.Adapter;

import android.content.Context;
import android.graphics.Color;
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
import com.wy.schooltakenout.Data.Seller;
import com.wy.schooltakenout.R;
import com.wy.schooltakenout.Tool.IOTool;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder>{
	private Context context;
	private List<Seller> sellerList;

    public StoreAdapter(List<Seller> sellerList){
        this.sellerList = sellerList;
    }

	//用于连接Store列表的项中的各个View
	static class ViewHolder extends RecyclerView.ViewHolder{
	    CardView itemView;
		ImageView storeImage;
		TextView storeName;

		private ViewHolder(View view){
			super(view);

            itemView=(CardView) view;
			storeImage=view.findViewById(R.id.store_img);
			storeName=view.findViewById(R.id.store_name);
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
		Seller seller = sellerList.get(position);

		holder.storeName.setText(seller.getName());

		// 读出商家头像
		String filename = seller.getSellerID()+".jpg";
		String path = this.context.getFilesDir().getAbsolutePath();
		File file = new File(path+"store_"+filename);
		if(!file.exists()) {
			// 向服务器请求商家头像并存储
			String url = IOTool.ip+"read/resources/seller/head/"+filename;
			String result = IOTool.upAndDown(url, null);
			IOTool.save(result, "store_"+filename, this.context);
		}
		Glide.with(context).load(file).into(holder.storeImage);

		//设置点击响应
        if(onItemClickListener!= null){
            holder.itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                	onItemClickListener.onClick(sellerList.get(holder.getAdapterPosition()));
                }
            });
        }
	}

	@Override
	public int getItemCount() {
		return sellerList.size();
	}

	//设置点击响应
    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
        void onClick(Seller thisSeller);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this.onItemClickListener=onItemClickListener;
    }
}
