package com.example.hamesakem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import java.util.Calendar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
        myRef.child("userId").setValue("Boaz");

    }
}