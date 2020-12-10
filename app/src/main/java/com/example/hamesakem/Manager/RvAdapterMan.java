package com.example.hamesakem.Manager;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hamesakem.Delete;
import com.example.hamesakem.MySummaries.RvAdapterSum;
import com.example.hamesakem.R;
import com.example.hamesakem.Result.Summary;

import java.util.ArrayList;

public class RvAdapterMan extends RecyclerView.Adapter<RvAdapterMan.MyViewHolder>  {
    ArrayList<Summary> sum_array;
    Context context;
    Activity my_summaries_activity;
    public RvAdapterMan(ArrayList<Summary> sum_array, Context context, Activity my_summaries_activity){
        this.sum_array= sum_array;
        this.context=context;
        this.my_summaries_activity = my_summaries_activity;
    }
    @NonNull
    @Override
    public RvAdapterMan.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View v=  inflater.inflate(R.layout.my_sum_row,parent,false);
        return new RvAdapterMan.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RvAdapterMan.MyViewHolder holder, int position) {
//holder.b.setText("ghgh");
        holder.l_name.setText(sum_array.get(position).lecturer);
        holder.c_name.setText(sum_array.get(position).topic);
        holder.id_name.setText(sum_array.get(position).userId);
        holder.u_name.setText(sum_array.get(position).university);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Delete d = new Delete(my_summaries_activity, sum_array.get(position).uri);
                d.del();
                sum_array.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, sum_array.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        if(sum_array == null)
            return 0;
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
