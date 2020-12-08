package com.example.hamesakem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import java.util.Calendar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
public class result extends AppCompatActivity {
    String course_choice="";
    String university_choice="";
    String lecturer_choice="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
         course_choice=getIntent().getExtras().getString("course_choice");
         university_choice=getIntent().getExtras().getString("university_choice");;
         lecturer_choice=getIntent().getExtras().getString("lecturer_choice");;
        Toast.makeText(getApplicationContext(),"-- "+course_choice+" --"+university_choice+" --"+lecturer_choice,  Toast.LENGTH_LONG).show();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("sum");
        Query vv = myRef
                .orderByChild("lecturer").equalTo("boaz");
        vv.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot child: snapshot.getChildren()){
                        Summary sum = child.getValue(Summary.class);
                        Toast.makeText(getApplicationContext(),sum.lecturer,  Toast.LENGTH_LONG).show();
                        delay(2);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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