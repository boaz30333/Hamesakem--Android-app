package com.example.hamesakem.Result;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hamesakem.DownloadFile;
import com.example.hamesakem.LoadActivity;
import com.example.hamesakem.R;
import com.example.hamesakem.SendEmail;
import com.example.hamesakem.Summary;
import com.example.hamesakem.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
    FirebaseDatabase database;
    FirebaseAuth auth;
    String current_uid;
    User current_user;
    public RvAdapter(ArrayList<Summary> sum_array, Context context, Activity result_activity,User current_user) {
        this.sum_array = sum_array;
        this.context = context;
        this.result_activity = result_activity;
        this.database = FirebaseDatabase.getInstance();
         this.auth = FirebaseAuth.getInstance();
        this.current_uid= auth.getCurrentUser().getUid();
        this.current_user= current_user;
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
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
//                    User user = User.getUser(sum_array.get(position).userId);
                    User user = document.toObject(User.class);
                    holder.iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SendEmail se =new SendEmail(result_activity);
                            //send to..
                            assert user != null;
                            se.send(user.email);
                        }
                    });

                    assert user != null;
                    holder.id_name.setText(user.fullName);
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
        });
        final Summary[] sum = {sum_array.get(position)};
        String rank=String.format("%.1f", sum[0].getRank());
        String count = "("+rank+")"+" ("+ sum[0].num_of_rates+")";
        holder.count.setText(count);
//        holder.l_name.setText(sum_array.get(position).lecturer);
//        holder.c_name.setText(sum_array.get(position).topic);
//        holder.u_name.setText(sum_array.get(position).university);
        holder.b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadFile d = new DownloadFile(result_activity, sum_array.get(position));
                d.down();
            }
        });

        holder.r.setRating(sum[0].getRank());
        holder.r.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                final String key_ ;
                sum[0] = sum_array.get(position);
                String key = (String) sum[0].uri;
                String[] fullPath = key.split("\\.");
                key = fullPath[0];
                key = key.replaceAll("/", "") + "-" + sum[0].userId;
                key_ = key;
                if(fromUser==true){
                final double[] diff = {0};
                final boolean[] new_rater = {false};
                DatabaseReference myRef = database.getReference("summariesRank");
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        //somebody rate
                        DatabaseReference myRef2 = database.getReference("sum");
                        ratingBar.getId();
                        if(snapshot.hasChild(key_)) {
                            String e= key_ +"/users/"+current_uid;
                            //this user already rate
                            if(snapshot.hasChild(e)) {

                                    double old_rating = snapshot.child(key_).child("users").child(current_uid).getValue(Double.class);
                                   diff[0] = rating -old_rating;
                                    sum[0].sum_of_rate += diff[0];

                                myRef.child(key_).child("users").child(current_uid).setValue(rating);
                                ratingBar.setRating(sum[0].getRank());

                            }
                            //this user never rate before
                            else{
                                diff[0] =rating;
                                new_rater[0] = true;
                                sum[0].num_of_rates++;
                                sum[0].sum_of_rate+=rating;
                                ratingBar.setRating(sum[0].getRank());
                                myRef.child(key_).child("count").setValue((snapshot.child(key_).child("count").getValue(Long.class)) + 1);
                                myRef.child(key_).child("users").child(current_uid).setValue(rating);

                            }
                        }
                        //this sum never get rate before
                        else{
                            diff[0] =rating;
                            new_rater[0] =true;
                          sum[0].num_of_rates++;
                          sum[0].sum_of_rate+=rating;
                          ratingBar.setRating(sum[0].getRank());
                          myRef.child(key_).child("count").setValue(1);
                          myRef.child(key_).child("users").child(current_uid).setValue(rating);

                        }
                        myRef2.child(key_).setValue(sum[0]);
                        String rank=String.format("%.1f", sum[0].getRank());
                        String count = "("+rank+")"+" ("+ sum[0].num_of_rates+")";
                        holder.count.setText(count);
                        final User[] user = new User[1];
                        DocumentReference docRef = holder.fStore.collection("users").document(sum_array.get(position).userId);
                        docRef.get().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
//                    User user = User.getUser(sum_array.get(position).userId);
                                    user[0] = document.toObject(User.class);
                                    if(new_rater[0] ==true) {
                                        user[0].setNum_of_rates(user[0].getNum_of_rates() + 1);
                                    }
                                    user[0].setRank(user[0].getRank()+diff[0]);
                                    user[0].updateFirestore();
                                    name_from_id[0] = (String) document.getData().get("fullName");
                                    Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                                } else {
                                    Log.d("TAG", "No such document");
                                }
                            } else {
                                Log.d("TAG", "get failed with ", task.getException());
                            }
                        });
//                        user[0].setNum_of_rates(user[0].getNum_of_rates()+1);
//                        user[0].setRank(user[0].getRank()+rating);
//                        user[0].updateFirestore();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }}
        });

        //-----check if this user already report and allow him to cancel his report

      DatabaseReference myRef = database.getReference("summariesToManager");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                final String key_ ;
                sum[0] = sum_array.get(position);
                String key = (String) sum[0].uri;
                String[] fullPath = key.split("\\.");
                key = fullPath[0];
                key = key.replaceAll("/", "") + "-" + sum[0].userId;
                key_ = key;

                if(snapshot.hasChild(key_)) {
                    String e= key_ +"/reporters/"+current_uid;
                    if(snapshot.hasChild(e)) {
                    holder.report.setChecked(true);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //---- listen to report
        holder.report.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DatabaseReference myRef = database.getReference("summariesToManager");
                if (isChecked) {
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            final String key_ ;
                            sum[0] = sum_array.get(position);
                            String key = (String) sum[0].uri;
                            String[] fullPath = key.split("\\.");
                            key = fullPath[0];
                            key = key.replaceAll("/", "") + "-" + sum[0].userId;
                            key_ = key;

                            if(snapshot.hasChild(key_)) {
                                String e= key_+"/reporters/"+current_uid;
                                if(!snapshot.hasChild(e)) {
                                    myRef.child(key_).child("count").setValue((Long) (snapshot.child(key_).child("count").getValue()) + 1);
                                    Toast.makeText(context, "add value", Toast.LENGTH_LONG).show();
                                    myRef.child(key_).child("reporters").child(current_uid).setValue(1);

                                }
                                Toast.makeText(context,"you are already reports",  Toast.LENGTH_LONG).show();

                            } else {

                                myRef.child(key_).child("count").setValue(1);
                                myRef.child(key_).child("reporters").child(current_uid).setValue(1);
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
                            final String key_ ;
                            sum[0] = sum_array.get(position);
                            String key = (String) sum[0].uri;
                            String[] fullPath = key.split("\\.");
                            key = fullPath[0];
                            key = key.replaceAll("/", "") + "-" + sum[0].userId;
                            key_ = key;
                            if (snapshot.hasChild(key_)) {
                                if((Long)(snapshot.child(key_).child("count").getValue())>1) {

                                    myRef.child(key_).child("count").setValue((Long) (snapshot.child(key_).getValue()) - 1);
                                    myRef.child(key_).child("reporters").child(current_uid).removeValue();
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
        TextView count;
        TextView id_name;
        ToggleButton report;
        FirebaseFirestore fStore;
        ImageView iv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            b = (Button) itemView.findViewById(R.id.button_row);
            r = (RatingBar) itemView.findViewById(R.id.rating);
            count = itemView.findViewById(R.id.count);
//            c_name = itemView.findViewById(R.id.c_name);
//            l_name = itemView.findViewById(R.id.t_name);
            id_name = itemView.findViewById(R.id.id_name);
            report = (ToggleButton)itemView.findViewById(R.id.report);
            fStore = FirebaseFirestore.getInstance();
            iv= itemView.findViewById(R.id.email);
        }
    }
}
