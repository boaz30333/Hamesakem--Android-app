package com.example.hamesakem.Manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.hamesakem.MySummaries.RvAdapterSum;
import com.example.hamesakem.R;
import com.example.hamesakem.Result.Summary;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Manager extends AppCompatActivity {

    static ArrayList<Summary> sum_array = new ArrayList<Summary>();
    RecyclerView rv ;
    RvAdapterMan rv_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

//        sum_array= (ArrayList<Summary>) getIntent().getSerializableExtra("sum_result");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
//        myRef.child("test").setValue(new Summary("a","a","a","a","a","a"));
        myRef.child("summariesToManager").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
//                Log.e("Count ", "" + snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Log.e("Get Data", postSnapshot.getKey());
                    Summary sum = getSummary(postSnapshot.getKey());
                    sum_array.add(sum);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
////                Log.e("The read failed: " );
//            }
        if(sum_array.size() > 0)
            rv();


    }

    private void rv() {
        rv= findViewById(R.id.RV);
        rv_adapter = new RvAdapterMan(sum_array,this, this);
        rv.setAdapter(rv_adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        rv_adapter.notifyDataSetChanged();
        sum_array.clear();
    }

    private Summary getSummary(String key) {
        final Summary[] sum = new Summary[1];
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("sum");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(key)) {
                    Log.d("key", ""+key);
                    sum_array.add( snapshot.child(key).getValue(Summary.class));
                    System.out.println("sum.userId:" + sum[0].userId);
                }
                else
                    Log.d("key else", ""+key);
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
}