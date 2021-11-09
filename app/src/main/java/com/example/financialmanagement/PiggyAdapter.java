package com.example.financialmanagement;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Locale;

public class PiggyAdapter extends RecyclerView.Adapter<PiggyAdapter.MyViewHolder> {

    Context context;
    ArrayList<Piggy> list;

    public PiggyAdapter(Context context, ArrayList<Piggy> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.record_row, parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Piggy piggy = list.get(position);

        Glide.with(context)
                .load(piggy.getCategory_url())
                .into(holder.category_image);
        //holder.category_url.setText(piggy.getCategory_url());
        holder.category_name.setText(piggy.getCategory_name());
        holder.record_name.setText(piggy.getMemo());
        holder.amount.setText(String.format(Locale.US, "%.2f", piggy.getAmount()));

        // on click record to view
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewRecord.class);
                intent.putExtra("category_name", piggy.getCategory_name());
                intent.putExtra("category", piggy.getCategory());
                String amount = Double.toString(Math.round(piggy.getAmount() * 100.0) / 100.0);
                intent.putExtra("amount", amount);
                intent.putExtra("date", piggy.getDate());
                intent.putExtra("memo", piggy.getMemo());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView category_image;
        TextView category_name, record_name, amount;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            category_image = itemView.findViewById(R.id.category_image);
            category_name = itemView.findViewById(R.id.category_name);
            record_name = itemView.findViewById(R.id.record_name);
            amount = itemView.findViewById(R.id.amount);

        }
    }
}