package com.example.hamesakem;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hamesakem.Manager.Manager;
import com.example.hamesakem.MySummaries.MySummaries;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MenuApp extends AppCompatActivity {

    final int MANAGER = 3 , MYSUMMARIES = 2;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        //hide manager button
        ArrayList<String> admins = new ArrayList<String>();
        admins.add("boaz30333@gmail.com");
        admins.add("bhorib@gmail.com");
        admins.add("itamarzo0@gmail.com");
        FirebaseAuth fAuth;
        fAuth = FirebaseAuth.getInstance();
        String email = fAuth.getCurrentUser().getEmail();
        if (!admins.contains(email))
            menu.getItem(MANAGER).setVisible(false);
        else
            menu.getItem(MANAGER).setVisible(true);

        //hide my_summaries button
        String userId = fAuth.getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("sum").orderByChild("userId").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!snapshot.exists())
                    menu.getItem(MYSUMMARIES).setVisible(false);
                else
                    menu.getItem(MYSUMMARIES).setVisible(true);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                search();
                return true;
            case R.id.upload:
                upload();
                return true;
            case R.id.my_summaries:
                my_summaries();
                return true;
            case R.id.manager:
                manager();
                return true;
            case R.id.logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }

    private void manager() {
        Intent i = new Intent(getApplicationContext(), Manager.class);
        startActivity(i);
    }

    private void my_summaries() {
        FirebaseAuth fAuth;
        fAuth = FirebaseAuth.getInstance();
        String userId = fAuth.getCurrentUser().getUid();
        Intent intent = new Intent(MenuApp.this, MySummaries.class);
        intent.putExtra("userId", userId);
        startActivityForResult(intent, 1);
    }

    private void upload() {
        Intent i = new Intent(getApplicationContext(),LoadActivity.class);
        startActivity(i);
    }

    private void search() {
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
    }

}
