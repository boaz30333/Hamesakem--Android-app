package com.example.hamesakem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.hamesakem.Result.Summary;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
                final Summary[] sum = new Summary[1];
                myRef.child(path_).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            sum[0] = snapshot.getValue(Summary.class);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                myRef.child(path_).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DatabaseReference db = database.getReference();
                        updateValue(db, "universities", sum[0].university);
                        updateValue(db, "courses", sum[0].topic);
                        updateValue(db, "lecturer", sum[0].lecturer);
                    }
                });
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

    //could be a bug when uploads summary with the same path, its update value again, but has just one summary.
    private void updateValue( DatabaseReference db,  String parent, String child) {
        db.child(parent).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(child)) {
                    if((Long)(snapshot.child(child).getValue())>0) {
                        db.child(parent).child(child).setValue((Long) (snapshot.child(child).getValue()) - 1);
                    }else{
                        db.child(parent).child(child).removeValue();
                    }
                }
//                else
//                    db.child(parent).child(child).setValue(1);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
