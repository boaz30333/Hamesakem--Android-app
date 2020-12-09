package com.example.hamesakem.Result;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.hamesakem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class result extends AppCompatActivity {
    String course_choice="";
    String university_choice="";
    String lecturer_choice="";
    ArrayList<Summary> sum_array;
RecyclerView rv ;
RvAdapter rv_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        sum_array= new ArrayList<>();
       rv= findViewById(R.id.RV);
        rv_adapter = new RvAdapter(sum_array,this);
        rv.setAdapter(rv_adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
//         course_choice=getIntent().getExtras().getString("course_choice");
//         university_choice=getIntent().getExtras().getString("university_choice");;
//         lecturer_choice=getIntent().getExtras().getString("lecturer_choice");;
//
//        Toast.makeText(getApplicationContext(),"-- "+course_choice+" --"+university_choice+" --"+lecturer_choice,  Toast.LENGTH_LONG).show();
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("sum");
//        Query vv = myRef
//                .orderByChild("lecturer").equalTo("boaz");
//        vv.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    for(DataSnapshot child: snapshot.getChildren()){
//                        Summary sum = child.getValue(Summary.class);
//                        sum_array.add(sum);
//                        Toast.makeText(getApplicationContext(),sum.lecturer,  Toast.LENGTH_LONG).show();
//                        delay(2);
//                    }
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

//        myRef.child("userId").setValue("Boaz");


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