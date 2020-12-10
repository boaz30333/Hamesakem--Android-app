package com.example.hamesakem.MySummaries;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

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
//holder.b.setText("ghgh");
        holder.l_name.setText(sum_array.get(position).lecturer);
        holder.c_name.setText(sum_array.get(position).topic);
        holder.id_name.setText(sum_array.get(position).userId);
        holder.u_name.setText(sum_array.get(position).university);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return sum_array.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        Button delete;
        RatingBar r;
        TextView u_name;
        TextView c_name;
        TextView l_name;
        TextView id_name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            delete= (Button) itemView.findViewById(R.id.button_row);
            r=  (RatingBar)itemView.findViewById(R.id.rating);
            u_name = itemView.findViewById(R.id.u_name);
            c_name=itemView.findViewById(R.id.c_name);
            l_name=itemView.findViewById(R.id.t_name);
            id_name=itemView.findViewById(R.id.id_name);

        }
    }
}
