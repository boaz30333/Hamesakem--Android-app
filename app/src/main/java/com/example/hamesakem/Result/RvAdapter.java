package com.example.hamesakem.Result;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hamesakem.DownloadFile;
import com.example.hamesakem.MainActivity;
import com.example.hamesakem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.MyViewHolder> {
    ArrayList<Summary> sum_array;
    Context context;
    Activity result_activity;

    public RvAdapter(ArrayList<Summary> sum_array, Context context, Activity result_activity) {
        this.sum_array = sum_array;
        this.context = context;
        this.result_activity = result_activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.my_row, parent, false);
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
                        name_from_id[0] = (String) document.getData().get("fName");
                        holder.id_name.setText("" + name_from_id[0]);
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
        holder.b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadFile d = new DownloadFile(result_activity, sum_array.get(position).uri);
                d.down();
                Toast.makeText(context, "ygy", Toast.LENGTH_SHORT).show();


            }
        });
        holder.report.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String key = (String)sum_array.get(position).uri;
                String[] fullPath = key.split("\\.");
                key = fullPath[0];
                key = key.replaceAll("/", "") + "-" + sum_array.get(position).userId;
                final String key_= key;
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("summariesToManager");
                if (isChecked) {
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if(snapshot.hasChild(key_)) {
                                myRef.child(key_).setValue((Long) (snapshot.child(key_).getValue()) + 1);
                                Toast.makeText(context,"add value",  Toast.LENGTH_LONG).show();

                            } else {
                                myRef.child(key_).setValue(1);
                                Toast.makeText(context,"new key",  Toast.LENGTH_LONG).show();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else { // regret to report
                   myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.hasChild(key_)) {
                                if((Long)(snapshot.child(key_).getValue())>1) {
                                    Toast.makeText(context,"remove value",  Toast.LENGTH_LONG).show();

                                    myRef.child(key_).setValue((Long) (snapshot.child(key_).getValue()) - 1);
                                }else{
                                    myRef.child(key_).removeValue();
                                    Toast.makeText(context,"remove key",  Toast.LENGTH_LONG).show();

                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return sum_array.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        Button b;
        RatingBar r;
        TextView u_name;
        TextView c_name;
        TextView l_name;
        TextView id_name;
        ToggleButton report;
        FirebaseFirestore fStore;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            b = (Button) itemView.findViewById(R.id.button_row);
            r = (RatingBar) itemView.findViewById(R.id.rating);
            u_name = itemView.findViewById(R.id.u_name);
            c_name = itemView.findViewById(R.id.c_name);
            l_name = itemView.findViewById(R.id.t_name);
            id_name = itemView.findViewById(R.id.id_name);
            report = (ToggleButton)itemView.findViewById(R.id.report);
            fStore = FirebaseFirestore.getInstance();

        }
    }
}
