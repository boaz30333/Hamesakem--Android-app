package com.example.hamesakem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Delete {
    FirebaseStorage storage;
    String path;
    Activity act;

    public Delete(Activity act, String path) {
        storage = FirebaseStorage.getInstance();
        this.act = act;
        this.path = path;
//        String[] fullPath = path.split("\\.");

//        this.path = fullPath[0];
//        this.pdf = "." + fullPath[1];
//        StorageReference storageRef = storage.getReference();
//        storageRef.child(path).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Toast.makeText(act.getApplicationContext(), "File Deleted ", Toast.LENGTH_LONG).show();
//            }
//        });
//        permissions();

    }

    public void del(){
        StorageReference storageRef = storage.getReference();
        final ProgressDialog progressDialog = new ProgressDialog(act);
        progressDialog.setTitle("Deleting file");
        progressDialog.show();
        storageRef.child(path).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressDialog.dismiss();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("sum");
                FirebaseAuth fAuth = FirebaseAuth.getInstance();
                String userId =fAuth.getCurrentUser().getUid();
                String[] fullPath = path.split("\\.");
                String path_ = fullPath[0];
                path_ = path_.replaceAll("/", "")+ "-" +userId;
                myRef.child(path_).removeValue();
                Toast.makeText(act.getApplicationContext(), "File Deleted ", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();

                //and displaying error message
                Toast.makeText(act.getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        });
    }
}
