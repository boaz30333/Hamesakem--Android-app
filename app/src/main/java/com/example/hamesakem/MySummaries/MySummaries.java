package com.example.hamesakem.MySummaries;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;

import com.example.hamesakem.R;
import com.example.hamesakem.Result.RvAdapter;
import com.example.hamesakem.Result.Summary;

import java.util.ArrayList;

public class MySummaries extends AppCompatActivity {

    String course_choice="";
    String university_choice="";
    String lecturer_choice="";
    ArrayList<Summary> sum_array;
    RecyclerView rv ;
    RvAdapterSum rv_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_summaries);

        sum_array= new ArrayList<>();
        rv= findViewById(R.id.RV);
        rv_adapter = new RvAdapterSum(sum_array,this);
        rv.setAdapter(rv_adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }

    public  void delay(int secs){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, secs * 1000); // afterDelay will be executed after (secs*1000) milliseconds.
    }
}