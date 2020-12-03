
package com.example.hamesakem;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    public  ListView list;
    public ArrayList<String> listItems ;
    public ListAdapter adapter;
    int course_num=-1;
    Button course;
    AlertDialog dialog;
    String course_choice="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = new ListView(this);// (ListView) findViewById(R.id.dor);
        course = (Button)findViewById(R.id.button3) ;
        ArrayList<String> listItems = new ArrayList<String>();
        adapter= new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_activated_1, listItems);
        list.setAdapter(adapter);
        EditText editText = new EditText(this);
        editText.setText("END");
        list.addFooterView(editText);

        listItems.add("my string");
        for(int i=0 ; i<15 ;i++) {
            String b = "קורס מספר : "+i;
            listItems.add(b);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogCustom);
        builder.setCancelable(true);
        builder.setTitle("בחר קורס");
        builder.setSingleChoiceItems(adapter, 1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                course_choice=listItems.get(which);
//                        dialog.dismiss();
            }
        });
        DialogInterface.OnClickListener foo= new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),""+course_choice,  Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        };
// add OK and Cancel buttons
        builder.setPositiveButton("OK", foo);
        builder.setNegativeButton("Cancel", null);
        dialog = builder.create();
        ((ArrayAdapter)adapter).notifyDataSetChanged();
        Button btn = new Button(this);
        btn.setText(R.string.choose_university);
        LinearLayout ll = (LinearLayout)findViewById(R.id.tableRow);
        LinearLayout.LayoutParams lp = new  LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.addView(btn, lp);
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
                dialog.show();
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