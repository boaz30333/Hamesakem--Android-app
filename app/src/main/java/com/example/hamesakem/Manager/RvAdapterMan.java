package com.example.hamesakem.Manager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hamesakem.Delete;
import com.example.hamesakem.DownloadFile;
import com.example.hamesakem.MySummaries.RvAdapterSum;
import com.example.hamesakem.R;
import com.example.hamesakem.Summary;
import com.example.hamesakem.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class RvAdapterMan extends RecyclerView.Adapter<RvAdapterMan.MyViewHolder>  {
    ArrayList<Summary> sum_array;
    Context context;
    Activity manager_activity;
    public RvAdapterMan(ArrayList<Summary> sum_array, Context context, Activity manager_activity){
        this.sum_array= sum_array;
        this.context=context;
        this.manager_activity = manager_activity;
    }
    @NonNull
    @Override
    public RvAdapterMan.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View v=  inflater.inflate(R.layout.my_man_row,parent,false);
        return new RvAdapterMan.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RvAdapterMan.MyViewHolder holder, int position) {
        final String[] name_from_id = new String[1];
        DocumentReference docRef = holder.fStore.collection("users").document(sum_array.get(position).userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        User user = document.toObject(User.class);
                        name_from_id[0]=  (String)document.getData().get("fullName");
                        holder.id_name.setText(""+name_from_id[0]);
                        switch (user.computeRank()) {
                            case 1:
                                holder.id_name.setTextColor(Color.GREEN);
                                break;
                            case 2:
                                holder.id_name.setTextColor(Color.MAGENTA);
                                break;
                            case 3:
                                holder.id_name.setTextColor(Color.GRAY);
                                break;
                        }
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
        String key = (String)sum_array.get(position).uri;
        String[] fullPath = key.split("\\.");
        key = fullPath[0];
        key = key.replaceAll("/", "") + "-" + sum_array.get(position).userId;
        final String key_= key;
        Log.d("key:  ", ""+ key_);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("summariesToManager");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.hasChild(key_)) {
                    System.out.println(""+(Long) (snapshot.child(key_).child("count").getValue()));
                    holder.num.setText(""+(Long) (snapshot.child(key_).child("count").getValue()));

                }
//                else {
//                    myRef.child(key_).child("count").setValue(1);
//
//                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
//        holder.num.setText(""+num[0]);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Delete d = new Delete(manager_activity, sum_array.get(position));
                d.del();
                sum_array.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, sum_array.size());
            }
        });

        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadFile d = new DownloadFile(manager_activity, sum_array.get(position));
                d.down();
            }
        });

        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child(key_).removeValue();
                Toast.makeText(manager_activity.getApplicationContext(), "Report denied ", Toast.LENGTH_LONG).show();

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
        Button download;
        Button reject;
        RatingBar r;
        TextView u_name;
        TextView c_name;
        TextView l_name;
        TextView id_name;
        TextView num;
        FirebaseFirestore fStore;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            delete= (Button) itemView.findViewById(R.id.button_row);
            download= (Button) itemView.findViewById(R.id.button_row2);
            reject= (Button) itemView.findViewById(R.id.button_row3);
            r=  (RatingBar)itemView.findViewById(R.id.rating);
            u_name = itemView.findViewById(R.id.u_name);
            c_name=itemView.findViewById(R.id.c_name);
            l_name=itemView.findViewById(R.id.t_name);
            id_name=itemView.findViewById(R.id.id_name);
            num = itemView.findViewById(R.id.num);
            fStore = FirebaseFirestore.getInstance();

        }
    }
}