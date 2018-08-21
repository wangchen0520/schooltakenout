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

import com.wy.schooltakenout.Data.Address;
import com.wy.schooltakenout.R;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    private Context mContext;

    private List<Address> mAddressList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView name;
        TextView phone;
        TextView address;
        ImageView modify;

        public ViewHolder(View view){
            super(view);
            cardView=(CardView)view;
            name=(TextView)view.findViewById(R.id.name);
            phone=(TextView)view.findViewById(R.id.phone_num);
            address=(TextView)view.findViewById(R.id.address);
            modify=(ImageView)view.findViewById(R.id.modify);
        }
    }

    public AddressAdapter(List<Address> addressList){
        mAddressList=addressList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext==null){
            mContext=parent.getContext();
        }
        View view= LayoutInflater.from(mContext).inflate(R.layout.address_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Address address=mAddressList.get(position);
        holder.name.setText(address.getName());
        holder.phone.setText(address.getPhone());
        holder.address.setText(address.getAddress());
        holder.modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mAddressList.size();
    }
}
