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

    ArrayList<Summary> sum_array;;
    RecyclerView rv ;
    RvAdapterMan rv_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);


//        sum_array = new ArrayList<String>();

//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
////                Log.e("The read failed: " );
//            }
        sum_array= (ArrayList<Summary>) getIntent().getSerializableExtra("sum_array");

        rv= findViewById(R.id.RV);
        rv_adapter = new RvAdapterMan(sum_array,this, this);
        rv.setAdapter(rv_adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        rv_adapter.notifyDataSetChanged();

    }


}