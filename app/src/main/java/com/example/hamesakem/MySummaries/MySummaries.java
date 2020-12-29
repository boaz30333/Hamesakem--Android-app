package com.example.hamesakem.MySummaries;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.hamesakem.MenuApp;
import com.example.hamesakem.R;
import com.example.hamesakem.Summary;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class MySummaries extends MenuApp {

  public  ArrayList<Summary> my_summaries=new ArrayList<>();
  public  RecyclerView rv ;
  public  RvAdapterSum rv_adapter;
  public  String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        rv= findViewById(R.id.RV);

        rv_adapter = new RvAdapterSum(my_summaries,this, this);
        rv.setAdapter(rv_adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        userId= (String) getIntent().getExtras().getString("userId");
        FireBaseCallBackSum fbcbSum = list -> rv_adapter.notifyDataSetChanged();
        find_my_sum(userId,fbcbSum);
        rv_adapter.notifyDataSetChanged();
    }


    public void find_my_sum(String userId, FireBaseCallBackSum fbcbSum) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        Query v3 = myRef
                .child("sum").orderByChild("userId").equalTo(userId);
        v3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                my_summaries.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        Summary sum = child.getValue(Summary.class);
                        my_summaries.add(sum);
                    }
                }
                Collections.sort(my_summaries, new Comparator<Summary>() {
                    @Override
                    public int compare(Summary o1, Summary o2) {
                        if(o1.getRank()>o2.getRank()) return 1;
                        else if(o1.getRank()<o2.getRank()) return -1;
                        else return 0;
                    }
                });
                fbcbSum.onCallback(my_summaries);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    interface FireBaseCallBackSum{
        void onCallback(ArrayList<Summary> list);
    }


}