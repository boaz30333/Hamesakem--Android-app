package com.example.hamesakem.MySummaries;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hamesakem.Delete;
import com.example.hamesakem.R;
import com.example.hamesakem.Result.Summary;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class RvAdapterSum extends RecyclerView.Adapter<RvAdapterSum.MyViewHolder> {
    ArrayList<Summary> sum_array;
    Context context;
    Activity my_summaries_activity;
    public RvAdapterSum(ArrayList<Summary> sum_array, Context context, Activity my_summaries_activity){
        this.sum_array= sum_array;
        this.context=context;
        this.my_summaries_activity = my_summaries_activity;
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
        final String[] name_from_id = new String[1];
        DocumentReference docRef = holder.fStore.collection("users").document(sum_array.get(position).userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        name_from_id[0]=  (String)document.getData().get("fName");
                        holder.id_name.setText(""+name_from_id[0]);
                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
        holder.l_name.setText(sum_array.get(position).lecturer);
        holder.c_name.setText(sum_array.get(position).topic);
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
        return sum_array.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        Button delete;
        RatingBar r;
        TextView u_name;
        TextView c_name;
        TextView l_name;
        TextView id_name;
        FirebaseFirestore fStore;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            delete= (Button) itemView.findViewById(R.id.button_row);
            r=  (RatingBar)itemView.findViewById(R.id.rating);
            u_name = itemView.findViewById(R.id.u_name);
            c_name=itemView.findViewById(R.id.c_name);
            l_name=itemView.findViewById(R.id.t_name);
            id_name=itemView.findViewById(R.id.id_name);
            fStore = FirebaseFirestore.getInstance();

        }
    }
}
