package com.wy.schooltakenout.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wy.schooltakenout.Data.Seller;
import com.wy.schooltakenout.R;
import com.wy.schooltakenout.Tool.IOTool;
import com.wy.schooltakenout.Tool.TestPrinter;

import java.io.File;
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

		// 读出商家头像
		String filename = seller.getSellerID()+".jpg";
		String url = IOTool.pictureIp+"resources/seller/head/"+filename;
        String path = this.context.getFileStreamPath("store_"+filename).getPath();
		File file = new File(path);
		IOTool.savePicture(url, path);

		Glide.with(context).load(file).into(holder.storeImage);
		holder.storeName.setText(seller.getName());

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
