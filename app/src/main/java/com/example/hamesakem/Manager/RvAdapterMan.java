package com.example.hamesakem.Manager;

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
import com.example.hamesakem.DownloadFile;
import com.example.hamesakem.MySummaries.RvAdapterSum;
import com.example.hamesakem.R;
import com.example.hamesakem.Result.Summary;
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
    ArrayList<String> sum_array;
    Context context;
    Activity manager_activity;
    public RvAdapterMan(ArrayList<String> sum_array, Context context, Activity manager_activity){
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
        Summary sum = getSummary(sum_array.get(position));
        System.out.println("sum.userId:" + sum.userId);
        final String[] name_from_id = new String[1];
        DocumentReference docRef = holder.fStore.collection("users").document(sum.userId);
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
        holder.l_name.setText(sum.lecturer);
        holder.c_name.setText(sum.topic);
        holder.id_name.setText(sum.userId);
        holder.u_name.setText(sum.university);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Delete d = new Delete(manager_activity, sum.uri);
                d.del();
                sum_array.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, sum_array.size());
            }
        });
        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadFile download = new DownloadFile(manager_activity, sum.uri);
                download.down();
            }
        });

    }

    private Summary getSummary(String key) {
        final Summary[] sum = new Summary[1];
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("sum");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(key)) {
                    sum[0] = (Summary)snapshot.child(key).getValue();
                    Log.d("key", ""+key);
                    System.out.println("sum.userId:" + sum[0].userId);
                }
//                    else {
//                        myRef.child(keyName).setValue(sum[0]);
//                        DatabaseReference db = database.getReference();
//                        updateValue(db, "universities", university);
//                        updateValue(db, "courses", course);
//                        updateValue(db, "lecturer", teacher);
//                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("cancel", "cancel");            }
        });
//        System.out.println("sum.userId:" + sum[0].userId);
        return sum[0];
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
        TextView u_name;
        TextView c_name;
        TextView l_name;
        TextView id_name;
        FirebaseFirestore fStore;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            delete = (Button) itemView.findViewById(R.id.button_row);
            download = (Button) itemView.findViewById(R.id.button_row2);
            u_name = itemView.findViewById(R.id.u_name);
            c_name = itemView.findViewById(R.id.c_name);
            l_name = itemView.findViewById(R.id.t_name);
            id_name = itemView.findViewById(R.id.id_name);
            fStore = FirebaseFirestore.getInstance();

        }
    }
}
