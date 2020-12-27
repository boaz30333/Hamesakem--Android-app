package com.example.hamesakem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

import androidx.annotation.NonNull;

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
    Summary sum_;
    String sum_key;
    final Summary[] sum = new Summary[1];
    public Delete(Activity act, Summary sum) {
        storage = FirebaseStorage.getInstance();
        this.act = act;
        this.path = sum.uri;
        this.sum_=sum;
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
                sum_key = fullPath[0];
                sum_key = sum_key.replaceAll("/", "")+ "-" +userId;
                
//                myRef.child(sum_key).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if(snapshot.exists()){
//                            sum_ = snapshot.getValue(Summary.class);
//                        }
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
                myRef.child(sum_key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DatabaseReference db = database.getReference();
                        updateValue(db, "universities", sum_.university);
                        updateValue(db, "courses", sum_.topic);
                        updateValue(db, "lecturer", sum_.lecturer);
                        updateValue(db, "summariesToManager", sum_key );

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
        db.child(parent).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(child)) {
                    if((Long)(snapshot.child(child).getValue())>1&& !parent.equals("summariesToManager")) {
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
