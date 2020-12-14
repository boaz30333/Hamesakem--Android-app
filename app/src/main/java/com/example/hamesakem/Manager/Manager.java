package com.example.hamesakem.Manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.hamesakem.MainActivity;
import com.example.hamesakem.MySummaries.RvAdapterSum;
import com.example.hamesakem.R;
import com.example.hamesakem.Result.Summary;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Manager extends AppCompatActivity {

    RecyclerView rv ;
    RvAdapterMan rv_adapter;
    ArrayList<String> keys_sum = new ArrayList<>();
    ArrayList<Summary> sum_array_check= new ArrayList<>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);


        FireBaseCallBackSum fbcbSum = new FireBaseCallBackSum() {
            @Override
            public void onCallback(ArrayList<Summary> list) {
                rv_adapter.notifyDataSetChanged();

            }
        };
        FireBaseCallBackString fbcbString = new FireBaseCallBackString() {
            @Override
            public void onCallback(ArrayList<String> list) {
                getSummary(fbcbSum);

            }
        };
        sum_to_check(fbcbString);
        rv= findViewById(R.id.RV);
        rv_adapter = new RvAdapterMan(sum_array_check,this, this);
        rv.setAdapter(rv_adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));


    }
    public void sum_to_check(FireBaseCallBackString fbcb_s){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        Query v3 = myRef
                .child("summariesToManager");
        v3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                keys_sum.clear();
                for(DataSnapshot child : snapshot.getChildren()){
                    keys_sum.add(child.getKey());
                }
                fbcb_s.onCallback(keys_sum);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private void getSummary(FireBaseCallBackSum fbcbSum) {
        sum_array_check.clear();
        final Summary[] sum = new Summary[1];
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        Query v3 = myRef
                .child("sum");
        v3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for(String key : keys_sum) {
                        Log.d("key", "" + key);
                        sum[0] = snapshot.child(key).getValue(Summary.class);
                        System.out.println("sum.userId:" + sum[0].userId);
                        sum_array_check.add(sum[0]);
                    }
                }
                else{}
//                    else {
//                        myRef.child(keyName).setValue(sum[0]);
//                        DatabaseReference db = database.getReference();
//                        updateValue(db, "universities", university);
//                        updateValue(db, "courses", course);
//                        updateValue(db, "lecturer", teacher);
//                    }
                fbcbSum.onCallback(sum_array_check);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("cancel", "cancel");            }
        });
//        System.out.println("sum.userId:" + sum[0].userId);
//        return sum[0];
    }


    interface FireBaseCallBackSum{
        void onCallback(ArrayList<Summary> list);
    }
    interface FireBaseCallBackString{
        void onCallback(ArrayList<String> list);
    }


}