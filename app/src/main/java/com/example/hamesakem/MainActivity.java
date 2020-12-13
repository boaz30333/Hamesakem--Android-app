
package com.example.hamesakem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.hamesakem.Result.Summary;
import com.example.hamesakem.Result.result;
import com.google.android.gms.tasks.OnCompleteListener;
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

import java.util.ArrayList;
import java.util.Iterator;


public class MainActivity extends AppCompatActivity {
    public  ListView list_c;
    public  ListView list_u;
    public  ListView list_l;
    public  ListView list_my_sum;
    public ArrayList<String> c_listItems = new ArrayList<String>();
    public ArrayList<String> u_listItems = new ArrayList<String>();
    public ArrayList<String> l_listItems = new ArrayList<String>(); ;
    public ArrayList<String> my_sum_listItems = new ArrayList<String>(); ;

    public ArrayList<Summary> sum_list;
    public ArrayList<Summary> sum_result_after_l;
    public ArrayList<Summary> sum_result_after_c;
    public ArrayList<Summary> my_summaries;
    ArrayList<Summary> sum_array_check=new ArrayList<>();;

    public ListAdapter adapter_c;
    public ListAdapter adapter_u;
    public ListAdapter adapter_l;
    public ListAdapter adapter_my_sum;
    int course_num=-1;
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
    AlertDialog choice_p_c;
    AlertDialog choice_p_l;
    String userId;
    String course_choice="";
    String university_choice="";
    String lecturer_choice="";
    boolean choose_c=false;

    boolean choose_l=false;

    boolean choose_u=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        upload = (Button)findViewById(R.id.button2);
        course = (Button)findViewById(R.id.button3);
        university = (Button)findViewById(R.id.button4);
        lecturer = (Button)findViewById(R.id.button5);
        search = (Button)findViewById(R.id.button9);
        my_sum = (Button)findViewById(R.id.button6);
        sum_to_manager = (Button)findViewById(R.id.button7);

        list_c = new ListView(this);// (ListView) findViewById(R.id.dor);
        list_u = new ListView(this);// (ListView) findViewById(R.id.dor);
        list_l = new ListView(this);// (ListView) findViewById(R.id.dor);
        list_my_sum = new ListView(this);



        adapter_c= new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_activated_1, c_listItems);
        adapter_u= new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_activated_1, u_listItems);
        adapter_l= new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_activated_1, l_listItems);
        adapter_my_sum= new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_activated_1, my_sum_listItems);

        list_u.setAdapter(adapter_u);
        list_c.setAdapter(adapter_c);
        list_l.setAdapter(adapter_l);
        list_my_sum.setAdapter(adapter_my_sum);
        sum_list = new ArrayList<Summary>();
        sum_result_after_l = new ArrayList<Summary>();
        sum_result_after_c = new ArrayList<Summary>();
        my_summaries = new ArrayList<>();

        FirebaseAuth fAuth;
        fAuth = FirebaseAuth.getInstance();
         userId =fAuth.getCurrentUser().getUid();
        find_my_sum(userId);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        myRef.child("sum").orderByChild("userId").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    my_sum.setVisibility(View.VISIBLE);
                }else{
                    my_sum.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        checkAdmin();

        Query vv = myRef
                .child("universities");
        vv.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                u_listItems.clear();
                if(snapshot.exists()){
                    for(DataSnapshot child: snapshot.getChildren()){
                        String u = (String) child.getKey();
                        if(!u_listItems.contains(u))
                        u_listItems.add(u);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Query v3 = myRef
                .child("sum").orderByChild("userId").equalTo(userId);
        v3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                my_summaries.clear();
                if(snapshot.exists()){
                    for(DataSnapshot child: snapshot.getChildren()){
                        Summary sum = child.getValue(Summary.class);
                        my_sum_listItems.add(sum.topic);
                        my_summaries.add(sum);
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
        choice_problem2.setMessage("בחר אוניברסיטה ומרצה ולאחר מכן נסה שוב!").setCancelable(false);
        choice_p_l = choice_problem2.create();
        choice_p_l.setCanceledOnTouchOutside(true);






//---------------------------------------
        AlertDialog.Builder builder_u = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogCustom);
        builder_u.setCancelable(true);
        builder_u.setTitle("בחר אוניברסיטה");
        builder_u.setSingleChoiceItems(adapter_u, 1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { // when click on curse
                university_choice=u_listItems.get(which);
                choose_u=true;
//                        dialog.dismiss();
            }
        });
        DialogInterface.OnClickListener university_listener= new DialogInterface.OnClickListener() { //when click ok
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sum_list.clear();
                c_listItems.clear();
                    course.setText("בחר קורס");
                    course_choice="";
                choose_c=false;
                    l_listItems.clear();
                    lecturer.setText("בחר מרצה");
                    lecturer_choice="";
                choose_l=false;
                if(u_listItems.isEmpty()) return;

                if(choose_u==false) {
                    Toast.makeText(getApplicationContext(),"no item chosen"+ "\n"+" please click one option before press ok",  Toast.LENGTH_LONG).show();

                    return;// university_choice=u_listItems.get(list_u.getSelectedItemPosition());
                }
                Toast.makeText(getApplicationContext(),""+university_choice,  Toast.LENGTH_SHORT).show();
                university.setText(""+university_choice);
                Query v2 = myRef
                        .child("sum").orderByChild("university").equalTo(university_choice+"");
                v2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        sum_list.clear();
                        if(snapshot.exists()){
                            for(DataSnapshot child: snapshot.getChildren()){
                                Summary sum = child.getValue(Summary.class);
                                if(!c_listItems.contains(sum.topic))
                                    c_listItems.add(sum.topic);
                                sum_list.add(sum);
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
//                dialog.dismiss();


            }
        };
// add OK and Cancel buttons
        builder_u.setPositiveButton("OK", university_listener);
        builder_u.setNegativeButton("Cancel", null);
        dialog_u = builder_u.create();
        ((ArrayAdapter)adapter_u).notifyDataSetChanged();
        //--------------------------------------------------------------------

        AlertDialog.Builder builder_c = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogCustom);
        builder_c.setCancelable(true);
        builder_c.setTitle("בחר קורס");

        builder_c.setSingleChoiceItems(adapter_c, 1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { // when click on curse
                course_choice=c_listItems.get(which);
                choose_c=true;

//                        dialog.dismiss();
            }
        });
        DialogInterface.OnClickListener course_listener= new DialogInterface.OnClickListener() { //when click ok
            @Override
            public void onClick(DialogInterface dialog, int which) {


                    choose_l=false;
                    l_listItems.clear();
                    lecturer.setText("בחר מרצה");
                    lecturer_choice="";
sum_result_after_c.clear();
sum_result_after_c.addAll(sum_list);
                if(c_listItems.isEmpty()) return;
                if(choose_c==false) {
                    Toast.makeText(getApplicationContext(),"no item chosen "+ "\n"+" please click one option before press ok",  Toast.LENGTH_LONG).show();

                    return;// university_choice=u_listItems.get(list_u.getSelectedItemPosition());
                }
                Toast.makeText(getApplicationContext(),""+course_choice,  Toast.LENGTH_SHORT).show();
                course.setText(""+course_choice);
                Iterator<Summary> iter = sum_result_after_c.iterator();
                while(iter.hasNext()){
                    Summary s = iter.next();
                    if(s.topic==course_choice&&!l_listItems.contains(s.lecturer)) l_listItems.add(s.lecturer);
                    else iter.remove();
                }
//                dialog.dismiss();

            }
        };
// add OK and Cancel buttons
        builder_c.setPositiveButton("OK", course_listener);
        builder_c.setNegativeButton("Cancel", null);
        dialog_c = builder_c.create();
        ((ArrayAdapter)adapter_c).notifyDataSetChanged();


        //---------------------------------------------------------------------
        AlertDialog.Builder builder_l = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogCustom);
        builder_l.setCancelable(true);
        builder_l.setTitle("בחר מרצה");

        builder_l.setSingleChoiceItems(adapter_l, 1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { // when click on curse
                lecturer_choice=l_listItems.get(which);
                choose_l=true;
                sum_result_after_l.clear();
                sum_result_after_l.addAll(sum_result_after_c);
                Iterator<Summary> iter = sum_result_after_c.iterator();
                while(iter.hasNext()){
                    Summary s = iter.next();
                    if(s.lecturer!=lecturer_choice) iter.remove();
                }
//                        dialog.dismiss();
            }
        });
        DialogInterface.OnClickListener lecturer_listener= new DialogInterface.OnClickListener() { //when click ok
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(l_listItems.isEmpty()) return;
                if(choose_l==false) {
                    Toast.makeText(getApplicationContext(),"no item chosen"+ "\n"+" please click one option before press ok",  Toast.LENGTH_LONG).show();

                    return;// university_choice=u_listItems.get(list_u.getSelectedItemPosition());
                }
                Toast.makeText(getApplicationContext(),""+lecturer_choice,  Toast.LENGTH_SHORT).show();
                lecturer.setText(""+lecturer_choice);
                dialog.dismiss();
            }
        };
// add OK and Cancel buttons
        builder_l.setPositiveButton("OK", lecturer_listener);
        builder_l.setNegativeButton("Cancel", null);
        dialog_l = builder_l.create();
        ((ArrayAdapter)adapter_l).notifyDataSetChanged();
        //----------------------------------------------------------------------

//        Button btn = new Button(this);
//        btn.setText(R.string.choose_university);
//        LinearLayout ll = (LinearLayout)findViewById(R.id.tableRow);
//        LinearLayout.LayoutParams lp = new  LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        ll.addView(btn, lp);
//        setContentView(R.layout.activity_main);

        OnBtnClick();

        my_sum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FirebaseAuth fAuth;
//                fAuth = FirebaseAuth.getInstance();
//                String userId =fAuth.getCurrentUser().getUid();

                Intent intent= new Intent(MainActivity.this,MySummaries.class);
                intent.putExtra("sum_result",my_summaries);
                startActivity(intent);
                find_my_sum(userId);
            }
        });
    }

    private void checkAdmin() {
        ArrayList<String> admins = new ArrayList<String>();
        admins.add("boas30333@gmail.com");
        admins.add("bhorib@gmail.com");
        admins.add("itamarzo0@gmail.com");


    }

    public void onClick(View v)
    {
        if(upload == v){
            Intent intent=new Intent(this,LoadActivity.class);
            startActivity(intent);
        }
        if(sum_to_manager == v){
            sum_to_check();
            Intent intent=new Intent(this, Manager.class);
            intent.putExtra("sum_array",sum_array_check);
            startActivity(intent);
        }


    }
    public void OnBtnClick(){
        course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(c_listItems==null||c_listItems.isEmpty())
                    choice_p_c.show();
else
                dialog_c.show();
//                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//
//                lp.copyFrom(dialog_c.getWindow().getAttributes());
//                lp.width = 750;
//                lp.height = 1150;
//                lp.x=40;
//                lp.y=100;
//                dialog_c.getWindow().setAttributes(lp);
            }
        });
        university.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("myTag", "This is my message");
                dialog_u.show();
                list_u.getRootView().performClick();

//                list_u.
            }
        });
        lecturer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                disableItemSelection(list_l);
                if(l_listItems==null||l_listItems.isEmpty())     {
                    choice_p_l.show();
                }
                else {
                    dialog_l.show();
                }
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"-- "+course_choice+" --"+university_choice+" --"+lecturer_choice,  Toast.LENGTH_LONG).show();
                Intent intent= new Intent(MainActivity.this, result.class);
                intent.putExtra("course_choice",course_choice);
                intent.putExtra("university_choice",university_choice);
                intent.putExtra("lecturer_choice",lecturer_choice);
                intent.putExtra("sum_result",sum_result_after_l);
                startActivity(intent);


            }
        });


    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();//logout
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }

public void sum_to_check(){
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
//        myRef.child("test").setValue(new Summary("a","a","a","a","a","a"));
    Query v3 = myRef
            .child("summariesToManager");
   v3.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot snapshot) {
//                Log.e("Count ", "" + snapshot.getChildrenCount());
            for (DataSnapshot child : snapshot.getChildren()) {

                getSummary(child.getKey());

            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
}
    private void getSummary(String key) {
        final Summary[] sum = new Summary[1];
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("sum");
        Query v3 = myRef
                .child(key);
        v3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.d("key", ""+key);
                    sum[0] = snapshot.getValue(Summary.class);
                    System.out.println("sum.userId:" + sum[0].userId);
                    sum_array_check.add(sum[0]);
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
//        return sum[0];
    }
    public void find_my_sum(String userId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        Query v3 = myRef
                .child("sum").orderByChild("userId").equalTo(userId);
        v3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                my_summaries.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        Summary sum = child.getValue(Summary.class);
                        my_sum_listItems.add(sum.topic);
                        my_summaries.add(sum);
                    }
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}