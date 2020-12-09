package com.example.hamesakem.MySummaries;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hamesakem.R;
import com.example.hamesakem.Result.Summary;

import java.util.ArrayList;

public class RvAdapterSum extends RecyclerView.Adapter<RvAdapterSum.MyViewHolder> {
    ArrayList<Summary> sum_array;
    Context context;
    public RvAdapterSum(ArrayList<Summary> sum_array, Context context){
        this.sum_array= sum_array;
        this.context=context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View v=  inflater.inflate(R.layout.my_sum_row,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.b.setText("ghgh");
    }

    @Override
    public int getItemCount() {
        return 30;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        Button b;
        RatingBar r;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            b= (Button) itemView.findViewById(R.id.button_row);
            r=  (RatingBar)itemView.findViewById(R.id.rating);
        }
    }
}
