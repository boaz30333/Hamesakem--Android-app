package com.example.hamesakem.MySummaries;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.hamesakem.R;
import com.example.hamesakem.Result.Summary;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MySummaries extends AppCompatActivity {
    String course_choice="";
    String university_choice="";
    String lecturer_choice="";
    ArrayList<Summary> sum_array;
    RecyclerView rv ;
    RvAdapterSum rv_adapter;
    ArrayList<Summary> sum_list = new ArrayList<Summary>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_summaries);

        sum_array= (ArrayList<Summary>) getIntent().getSerializableExtra("sum_result");
        rv= findViewById(R.id.RV);
        rv_adapter = new RvAdapterSum(sum_array,this);
        rv.setAdapter(rv_adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
//        course_choice=getIntent().getExtras().getString("course_choice");
//        university_choice=getIntent().getExtras().getString("university_choice");;
//        lecturer_choice=getIntent().getExtras().getString("lecturer_choice");;
    }
}