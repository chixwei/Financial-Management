package com.example.financialmanagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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
        holder.category_name.setText(piggy.getCategory_name());
        holder.record_name.setText(piggy.getMemo());
        holder.amount.setText(String.format(Locale.US, "%.2f", piggy.getAmount()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView category_name, record_name, amount;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            category_name = itemView.findViewById(R.id.category_name);
            record_name = itemView.findViewById(R.id.record_name);
            amount = itemView.findViewById(R.id.amount);

        }
    }
}