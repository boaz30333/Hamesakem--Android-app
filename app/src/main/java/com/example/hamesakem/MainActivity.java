
package com.example.hamesakem;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.example.hamesakem.Result.result;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    public  ListView list_c;
    public  ListView list_u;
    public  ListView list_l;
    public ArrayList<String> c_listItems ;
    public ArrayList<String> u_listItems ;
    public ArrayList<String> l_listItems ;

    public ListAdapter adapter_c;
    public ListAdapter adapter_u;
    public ListAdapter adapter_l;
    int course_num=-1;
    Button course;
    Button university;
    Button lecturer;
    Button search;
    AlertDialog dialog_c;
    AlertDialog dialog_u;
    AlertDialog dialog_l;
    String course_choice="";
    String university_choice="";
    String lecturer_choice="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list_c = new ListView(this);// (ListView) findViewById(R.id.dor);
        list_u = new ListView(this);// (ListView) findViewById(R.id.dor);
        list_l = new ListView(this);// (ListView) findViewById(R.id.dor);
        course = (Button)findViewById(R.id.button3);
        university = (Button)findViewById(R.id.button4);
        lecturer = (Button)findViewById(R.id.button5);
        search = (Button)findViewById(R.id.button9);

        ArrayList<String> c_listItems = new ArrayList<String>();
        ArrayList<String> u_listItems = new ArrayList<String>();
        ArrayList<String> l_listItems = new ArrayList<String>();

        adapter_c= new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_activated_1, c_listItems);
        adapter_u= new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_activated_1, u_listItems);
        adapter_l= new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_activated_1, l_listItems);

        list_c.setAdapter(adapter_c);
        for(int i=0 ; i<15 ;i++) {
            String b = "קורס מספר : "+i;
            c_listItems.add(b);
        }
        list_u.setAdapter(adapter_u);
        for(int i=0 ; i<15 ;i++) {
            String b = "אוניברסיטה מספר : "+i;
            u_listItems.add(b);
        }
        list_l.setAdapter(adapter_l);
        for(int i=0 ; i<15 ;i++) {
            String b = "מרצה מספר : "+i;
            l_listItems.add(b);
        }
        AlertDialog.Builder builder_c = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogCustom);
        builder_c.setCancelable(true);
        builder_c.setTitle("בחר קורס");
        builder_c.setSingleChoiceItems(adapter_c, 1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { // when click on curse
                course_choice=c_listItems.get(which);
//                        dialog.dismiss();
            }
        });
        DialogInterface.OnClickListener course_listener= new DialogInterface.OnClickListener() { //when click ok
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),""+course_choice,  Toast.LENGTH_SHORT).show();
                course.setText(""+course_choice);
                dialog.dismiss();
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
//                        dialog.dismiss();
            }
        });
        DialogInterface.OnClickListener lecturer_listener= new DialogInterface.OnClickListener() { //when click ok
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
        AlertDialog.Builder builder_u = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogCustom);
        builder_u.setCancelable(true);
        builder_u.setTitle("בחר אוניברסיטה");
        builder_u.setSingleChoiceItems(adapter_u, 1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { // when click on curse
                university_choice=u_listItems.get(which);
//                        dialog.dismiss();
            }
        });
        DialogInterface.OnClickListener university_listener= new DialogInterface.OnClickListener() { //when click ok
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),""+university_choice,  Toast.LENGTH_SHORT).show();
                university.setText(""+university_choice);
                dialog.dismiss();
            }
        };
// add OK and Cancel buttons
        builder_u.setPositiveButton("OK", university_listener);
        builder_u.setNegativeButton("Cancel", null);
        dialog_u = builder_u.create();
        ((ArrayAdapter)adapter_u).notifyDataSetChanged();
        //--------------------------------------------------------------------
//        Button btn = new Button(this);
//        btn.setText(R.string.choose_university);
//        LinearLayout ll = (LinearLayout)findViewById(R.id.tableRow);
//        LinearLayout.LayoutParams lp = new  LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        ll.addView(btn, lp);
//        setContentView(R.layout.activity_main);

        OnBtnClick();
    }
    public void onClick(View v)
    {
        Intent intent=new Intent(this,LoadActivity.class);
        startActivity(intent);

    }
    public void OnBtnClick(){
        course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("myTag", "This is my message");
                dialog_c.show();
//                dialog_c.getWindow().setLayout(800, 1000);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

                lp.copyFrom(dialog_c.getWindow().getAttributes());
                lp.width = 750;
                lp.height = 1150;
                lp.x=40;
                lp.y=100;
                dialog_c.getWindow().setAttributes(lp);
            }
        });
        university.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("myTag", "This is my message");
                dialog_u.show();
            }
        });
        lecturer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("myTag", "This is my message");
                dialog_l.show();
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
                startActivity(intent);


            }
        });
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(),(String)adapter.getItem(position),  Toast.LENGTH_SHORT).show();
//                dialog.dismiss();
//            }
//        });
    }
    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();//logout
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }

}