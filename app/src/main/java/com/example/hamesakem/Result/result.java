package com.example.hamesakem.Result;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.hamesakem.MenuApp;
import com.example.hamesakem.R;
import com.example.hamesakem.Summary;
import com.example.hamesakem.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class result extends MenuApp {

    ArrayList<Summary> sum_array;
    RecyclerView rv ;
    RvAdapter rv_adapter;
User current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        current_user= (User)getIntent().getSerializableExtra("current_user");
        sum_array= (ArrayList<Summary>) getIntent().getSerializableExtra("sum_result");
        rv= findViewById(R.id.RV);
        rv_adapter = new RvAdapter(sum_array,this,this,current_user);
        rv.setAdapter(rv_adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

    }
}
