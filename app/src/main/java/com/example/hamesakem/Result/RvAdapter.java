package com.example.hamesakem.Result;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hamesakem.DownloadFile;
import com.example.hamesakem.MainActivity;
import com.example.hamesakem.R;

import java.util.ArrayList;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.MyViewHolder> {
    ArrayList<Summary> sum_array;
    Context context;
    Activity result_activity;
    public RvAdapter(ArrayList<Summary> sum_array, Context context, Activity result_activity){
        this.sum_array= sum_array;
        this.context=context;
        this.result_activity=result_activity;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(context);
      View v=  inflater.inflate(R.layout.my_row,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//holder.b.setText("ghgh");
        holder.l_name.setText(sum_array.get(position).lecturer);
        holder.c_name.setText(sum_array.get(position).topic);
        holder.id_name.setText(sum_array.get(position).userId);
        holder.u_name.setText(sum_array.get(position).university);
        holder.b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadFile d= new DownloadFile(result_activity,sum_array.get(position).uri);
                d.down();
                Toast.makeText(context, "ygy",  Toast.LENGTH_SHORT).show();


            }
        });

    }

    @Override
    public int getItemCount() {
        return sum_array.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
Button b;
        RatingBar r;
        TextView u_name;
        TextView c_name;
        TextView l_name;
        TextView id_name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            b= (Button) itemView.findViewById(R.id.button_row);
            r=  (RatingBar)itemView.findViewById(R.id.rating);
            u_name = itemView.findViewById(R.id.u_name);
            c_name=itemView.findViewById(R.id.c_name);
            l_name=itemView.findViewById(R.id.t_name);
            id_name=itemView.findViewById(R.id.id_name);

        }
    }
}
