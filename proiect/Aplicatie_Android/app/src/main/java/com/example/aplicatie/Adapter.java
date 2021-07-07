package com.example.aplicatie;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.myViewHolder>
{
    Context mContext;
    List<ErrorCar> mData;

    public Adapter(Context mContext, List<ErrorCar> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View v = layoutInflater.inflate(R.layout.card_item_error,parent,false);
        return new myViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.brand.setText(mData.get(position).getCarBrand() + " "+mData.get(position).getCarModel());
        holder.errMsg.setText(mData.get(position).getErrorText());
        holder.errTitle.setText(mData.get(position).getErrorName());
        holder.date.setText(mData.get(position).getTodayDate());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{

        TextView brand,errTitle, errMsg, date, hour;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            brand = itemView.findViewById(R.id.carBrandModelCard);
            errTitle = itemView.findViewById(R.id.carErrorNameCard);
            errMsg = itemView.findViewById(R.id.carErrorMsgCard);
            date = itemView.findViewById(R.id.carDateCard);

        }
    }
}