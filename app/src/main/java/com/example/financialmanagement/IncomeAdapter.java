package com.example.financialmanagement;


import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.Model;

import java.util.ArrayList;

public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.ViewHolder> {

    private static final String Tag = "RecycleView";
    private Context mContext;
    private ArrayList<DataModel> categoryList;
    View v;

    public IncomeAdapter(Context mContext, ArrayList<DataModel> categoryList) {
        this.mContext = mContext;
        this.categoryList = categoryList;
    }


    @NonNull
    @Override
    public IncomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final DataModel temp = categoryList.get(position);

        //TextView
        holder.textView.setText(categoryList.get(position).getName());

        //ImageView : Glide Library
        Glide.with(mContext)
                .load(categoryList.get(position).getImageUrl())
                .into(holder.imageView);
//TESTING-------------------------------------------------------------------------
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, AddIncome.class);
                intent.putExtra("image", temp.getImageUrl());
                intent.putExtra("title", temp.getName());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);

            }
        });

        //---------------------------------------------------------------------------
    }

    @Override
    public int getItemCount() {

        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //widgets
        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);


        }
    }


}

