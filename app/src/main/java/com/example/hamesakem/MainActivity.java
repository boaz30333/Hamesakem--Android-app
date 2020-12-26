package com.example.hamesakem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hamesakem.Manager.Manager;
import com.example.hamesakem.MySummaries.MySummaries;
import com.example.hamesakem.Result.result;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    public ListView list_c;
    public ListView list_u;
    public ListView list_l;
    public ListView list_my_sum;
    public ArrayList<String> c_listItems = new ArrayList<>();
    public ArrayList<String> u_listItems = new ArrayList<>();
    public ArrayList<String> l_listItems = new ArrayList<>();
    public ArrayList<Summary> sum_list;
    public ArrayList<Summary> sum_result_after_l;
    public ArrayList<Summary> sum_result_after_c;
    public ArrayList<Summary> my_summaries;
    public ListAdapter adapter_c;
    public ListAdapter adapter_u;
    public ListAdapter adapter_l;
    Button upload;
    Button course;
    Button university;
    Button lecturer;
    Button search;
    Button my_sum;
    Button sum_to_manager;
    AlertDialog dialog_c;
    AlertDialog dialog_u;
    AlertDialog dialog_l;
    AlertDialog choice_p_u;
    AlertDialog choice_p_c;
    AlertDialog choice_p_l;
    String userId;
    String course_choice = "";
    String university_choice = "";
    String lecturer_choice = "";
    boolean choose_c = false;
    boolean choose_l = false;
    boolean choose_u = false;
    private User current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        upload = (Button) findViewById(R.id.button2);
        course = (Button) findViewById(R.id.button3);
        university = (Button) findViewById(R.id.button4);
        lecturer = (Button) findViewById(R.id.button5);
        search = (Button) findViewById(R.id.button9);
        my_sum = (Button) findViewById(R.id.button6);
        sum_to_manager = (Button) findViewById(R.id.button7);

        list_c = new ListView(this);
        list_u = new ListView(this);
        list_l = new ListView(this);
        list_my_sum = new ListView(this);

        adapter_c = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_activated_1, c_listItems);
        adapter_u = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_activated_1, u_listItems);
        adapter_l = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_activated_1, l_listItems);

        list_u.setAdapter(adapter_u);
        list_c.setAdapter(adapter_c);
        list_l.setAdapter(adapter_l);

        sum_list = new ArrayList<>();
        sum_result_after_l = new ArrayList<>();
        sum_result_after_c = new ArrayList<>();

        FirebaseAuth fAuth;
        fAuth = FirebaseAuth.getInstance();
        userId = fAuth.getCurrentUser().getUid();

        getUser(userId, user -> {
                    current_user = user;
                    OnBtnClick2();
                });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        myRef.child("sum").orderByChild("userId").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println("5");
                if (snapshot.exists()) {
                    my_sum.setVisibility(View.VISIBLE);
                } else {
                    my_sum.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        checkAdmin();
        Query vv = myRef.child("universities");
        vv.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println("6");

                u_listItems.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        String u = (String) child.getKey();
                        if (!u_listItems.contains(u)) u_listItems.add(u);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        //--------------------------------------------------
        AlertDialog.Builder choice_problem1 = new AlertDialog.Builder(this);
        choice_problem1.setTitle("אין אפשרויות לבחירה");
        choice_problem1.setMessage("בחר אוניברסיטה ולאחר מכן נסה שוב!").setCancelable(false);
        choice_p_c = choice_problem1.create();
        choice_p_c.setCanceledOnTouchOutside(true);
        AlertDialog.Builder choice_problem2 = new AlertDialog.Builder(this);
        choice_problem2.setTitle("אין אפשרויות לבחירה");
        choice_problem2.setMessage("בחר אוניברסיטה וקורס ולאחר מכן נסה שוב!").setCancelable(false);
        choice_p_l = choice_problem2.create();
        choice_p_l.setCanceledOnTouchOutside(true);
        AlertDialog.Builder choice_problem3 = new AlertDialog.Builder(this);
        choice_problem3.setTitle("אין אפשרויות לבחירה");
        choice_problem3.setMessage("ברוכים הבאים לאפליקציה! כרגע אין קורסים במערכת, נסו שוב במועד מאוחר יותר").setCancelable(false);
        choice_p_u = choice_problem1.create();
        choice_p_u.setCanceledOnTouchOutside(true);
        //---------------------------------------
        AlertDialog.Builder builder_u = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogCustom);
        builder_u.setCancelable(true);
        builder_u.setTitle("בחר אוניברסיטה");
        builder_u.setSingleChoiceItems(adapter_u, 0, (dialog, which) -> { // when click on curse
            university_choice = u_listItems.get(which);
            choose_u = true;
        }); //when click ok
        DialogInterface.OnClickListener university_listener = (dialog, which) -> {

//            sum_list.clear();
//            c_listItems.clear();
//            course.setText("בחר קורס");
//            course_choice = "";
//            choose_c = false;
//            l_listItems.clear();
//            lecturer.setText("בחר מרצה");
//            lecturer_choice = "";
//            choose_l = false;
            if (u_listItems.isEmpty()) return;
            clearAllOption();
            if (choose_u == false) {
//                Toast.makeText(getApplicationContext(), "no item chosen" + "\n" + " please click one option before press ok", Toast.LENGTH_LONG).show();
//                return;
                university_choice = u_listItems.get(0);
            }
            Toast.makeText(getApplicationContext(), "" + university_choice, Toast.LENGTH_SHORT).show();
            university.setText("" + university_choice);
            Query v2 = myRef.child("sum").orderByChild("university").equalTo(university_choice + "");
            v2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    sum_list.clear();
                    if (snapshot.exists()) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            Summary sum = child.getValue(Summary.class);
                            if (!c_listItems.contains(sum.topic)) c_listItems.add(sum.topic);
                            sum_list.add(sum);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        };
        builder_u.setPositiveButton("OK", university_listener);
        builder_u.setNegativeButton("Cancel", null);
        dialog_u = builder_u.create();
        ((ArrayAdapter) adapter_u).notifyDataSetChanged();
        //--------------------------------------------------------------------
        AlertDialog.Builder builder_c = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogCustom);
        builder_c.setCancelable(true);
        builder_c.setTitle("בחר קורס");
        builder_c.setSingleChoiceItems(adapter_c, 0, (dialog, which) -> {
            // when click on curse
            course_choice = c_listItems.get(which);
            choose_c = true;
        }); //when click ok
        DialogInterface.OnClickListener course_listener = (dialog, which) -> {
//            choose_l = false;
//            l_listItems.clear();
//            lecturer.setText("בחר מרצה");
//            lecturer_choice = "";
//            sum_result_after_c.clear();
//            sum_result_after_c.addAll(sum_list);
            clearLecturerOption();
            if (c_listItems.isEmpty()) return;
            if (choose_c == false) {
                course_choice = c_listItems.get(0);
//                Toast.makeText(getApplicationContext(), "no item chosen " + "\n" + " please click one option before press ok", Toast.LENGTH_LONG).show();
//                return;
            }
            Toast.makeText(getApplicationContext(), "" + course_choice, Toast.LENGTH_SHORT).show();
            course.setText("" + course_choice);
            Iterator<Summary> iter = sum_result_after_c.iterator();
            while (iter.hasNext()) {
                Summary s = iter.next();
                if (s.topic == course_choice && !l_listItems.contains(s.lecturer))
                    l_listItems.add(s.lecturer);
                else iter.remove();
            }
        };
        builder_c.setPositiveButton("OK", course_listener);
        builder_c.setNegativeButton("Cancel", null);
        dialog_c = builder_c.create();
        ((ArrayAdapter) adapter_c).notifyDataSetChanged();
        //---------------------------------------------------------------------
        AlertDialog.Builder builder_l = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogCustom);
        builder_l.setCancelable(true);
        builder_l.setTitle("בחר מרצה");
        builder_l.setSingleChoiceItems(adapter_l, 0, (dialog, which) -> {
            // when click on curse
            lecturer_choice = l_listItems.get(which);
            choose_l = true;

        });
        //when click ok
        DialogInterface.OnClickListener lecturer_listener = (dialog, which) -> {
            if (l_listItems.isEmpty()) return;
            if (choose_l == false) {

                lecturer_choice = l_listItems.get(0);
//                Toast.makeText(getApplicationContext(), "no item chosen" + "\n" + " please click one option before press ok", Toast.LENGTH_LONG).show();

//                return;
            }
            sum_result_after_l.clear();
            sum_result_after_l.addAll(sum_result_after_c);
            Iterator<Summary> iter = sum_result_after_c.iterator();
            while (iter.hasNext()) {
                Summary s = iter.next();
                if (s.lecturer != lecturer_choice) iter.remove();
            }
            Toast.makeText(getApplicationContext(), "" + lecturer_choice, Toast.LENGTH_SHORT).show();
            lecturer.setText("" + lecturer_choice);
            dialog.dismiss();
        };
        builder_l.setPositiveButton("OK", lecturer_listener);
        builder_l.setNegativeButton("Cancel", null);
        dialog_l = builder_l.create();
        ((ArrayAdapter) adapter_l).notifyDataSetChanged();
        //----------------------------------------------------------------------
        OnBtnClick();

    }

    public void OnBtnClick() {
        course.setOnClickListener(v -> {
            clearLecturerOption();
            if (c_listItems == null || c_listItems.isEmpty()) {
                choice_p_c.show();
            } else {
                dialog_c.show();
            }
        });
        university.setOnClickListener(v -> {
            clearAllOption();
            if (u_listItems.isEmpty()) {
                choice_p_u.show();
            } else {
                dialog_u.show();
            }
        });
        lecturer.setOnClickListener(v -> {
            if (l_listItems == null || l_listItems.isEmpty()) {
                choice_p_l.show();
            } else {
                dialog_l.show();
            }
        });

    }

    private void OnBtnClick2() {
        my_sum.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MySummaries.class);
            intent.putExtra("userId", userId);
            startActivityForResult(intent, 1);
        });

        search.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, result.class);
            intent.putExtra("sum_result", sum_result_after_l);
            startActivityForResult(intent, 2);
        });

        upload.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoadActivity.class);
            startActivityForResult(intent, 3);
        });

        sum_to_manager.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Manager.class);
            startActivityForResult(intent, 4);
        });
    }

    private void getUser(String userId, FireBaseCallBack fireBaseCallBack) {
        final User[] user = new User[1];
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        DocumentReference docRef = fStore.collection("users").document(userId);

        docRef.get().addOnCompleteListener(task -> {

            user[0] = null;
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();

                if (document.exists()) {
                    if (document.contains("type") && ((String) document.getData().get("type")).equals("mesakem")) {
                        user[0] = document.toObject(Mesakem.class);
                    } else {
                        try {
                            user[0] = document.toObject(User.class);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                } else {
                    Log.d("TAG", "No such document");
                }
            } else {
                Log.d("TAG", "get failed with ", task.getException());
            }

            fireBaseCallBack.onCallback(user[0]);
        });

    }

    private void checkAdmin() {
        ArrayList<String> admins = new ArrayList<String>();
        admins.add("boaz30333@gmail.com");
        admins.add("bhorib@gmail.com");
        admins.add("itamarzo0@gmail.com");
        FirebaseAuth fAuth;
        fAuth = FirebaseAuth.getInstance();
        String email = fAuth.getCurrentUser().getEmail();
        if (admins.contains(email)) sum_to_manager.setVisibility(View.VISIBLE);
        else sum_to_manager.setVisibility(View.INVISIBLE);
    }



    private void clearLecturerOption() {
        choose_l = false;
        l_listItems.clear();
        lecturer.setText("בחר מרצה");
        lecturer_choice = "";
        sum_result_after_c.clear();
        sum_result_after_c.addAll(sum_list);
    }

    private void clearAllOption() {
        sum_list.clear();
        sum_result_after_c.clear();
        sum_result_after_l.clear();
        university.setText("בחר אוניברסיטה");
        c_listItems.clear();
        course.setText("בחר קורס");
        course_choice = "";
        choose_c = false;
        l_listItems.clear();
        lecturer.setText("בחר מרצה");
        lecturer_choice = "";
        choose_l = false;
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();//logout
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }

    interface FireBaseCallBack {
        void onCallback(User user);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        clearAllOption();
    }
}