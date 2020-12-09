package com.example.hamesakem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hamesakem.Result.Summary;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.okhttp.internal.DiskLruCache;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;


public class LoadActivity extends AppCompatActivity implements View.OnClickListener {
//    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
//    StorageReference photoRef;
    private StorageReference mStorageRef;
    //a constant to track the file chooser intent
    private static final int PICK_IMAGE_REQUEST = 234;
    private static final int PICK_PDF_REQUEST = 2342;
    //Buttons
    private Button buttonChoose;
    private Button buttonUpload;
    TextView course;
    TextView teacher;
    TextView year;
    TextView simester;
    TextView university;
    //ImageView
    private ImageView imageView;

    //a Uri object to store file path
    private Uri filePath;
    static final int PICK_FILE_REQUEST = 101 , RESULT_OK = -1;
    TextSwitcher textView;
    public void onClick(View v){
        course = (TextView)findViewById(R.id.tecourse);
        teacher = (TextView)findViewById(R.id.teteacher);
        year = (TextView)findViewById(R.id.teyear);
        simester = (TextView)findViewById(R.id.tesimester);
        university = (TextView)findViewById(R.id.teuniversity);
//        Toast.makeText(this, "course: "+ course.getText().toString(), Toast.LENGTH_LONG).show();
        if(checkInput())
            showFileChooser();
//        uploadFile();
    }

    private boolean checkInput() {
        if(university.getText().toString().length()==0 ||course.getText().toString().length()==0 || teacher.getText().toString().length()==0 || year.getText().toString().length()==0 || simester.getText().toString().length()==0 || course.getText().toString().length()==0){
            Toast.makeText(this, "שגיאה: דרוש למלא את כל השדות!", Toast.LENGTH_LONG).show();
            return false;
        }
        String yr = year.getText().toString().replaceAll(" ", "");;
        String sim = simester.getText().toString().toLowerCase().replaceAll(" ", "");

        if(yr.length()!=4){
            Toast.makeText(this, " שגיאה: השנה לא תקינה!"+ yr , Toast.LENGTH_LONG).show();
            return false;
        }
        for(int i=0; i<yr.length(); i++) {
            if (yr.charAt(i) < '0' || year.getText().toString().charAt(i) > '9') {
                Toast.makeText(this, " שגיאה: השנה לא תקינה!" + yr, Toast.LENGTH_LONG).show();
                return false;
            }
        }
        if(sim.length() != 1 || (!sim.equals("a") && !sim.equals("b") && !sim.equals("s") && !sim.equals("א") && !sim.equals("ב") && !sim.equals("ק"))){
            Toast.makeText(this, " שגיאה: הסימסטר לא תקין!" + sim, Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select pdf file"), PICK_PDF_REQUEST);
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        //getting views from layout
//        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonUpload = (Button) findViewById(R.id.bsave);

        imageView = (ImageView) findViewById(R.id.imageView);

        //attaching listener
//        buttonChoose.setOnClickListener(this);
//        buttonUpload.setOnClickListener(this);
    }
    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            uploadFile();
        }
    }
    //this method will upload the file
    private void uploadFile() {
        //if there is a file to upload
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            FirebaseAuth fAuth;
            fAuth = FirebaseAuth.getInstance();
            String user =fAuth.getCurrentUser().getUid();
            String path = "uploads"+ "/" +university.getText().toString().toLowerCase() + "/" +teacher.getText().toString().toLowerCase() + "/" +
                    course.getText().toString().toLowerCase() + "/" + year.getText().toString() + "/" + simester.getText().toString().toLowerCase() + "/" + user +MimeTypeMap.getFileExtensionFromUrl(filePath.toString());
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("sum");
            Summary sum = new Summary(teacher.getText().toString(), simester.getText().toString(), course.getText().toString(), university.getText().toString(), path, user);
            myRef.push().setValue(sum);
            DatabaseReference db = database.getReference();
            db.child("universities").child(university.getText().toString()).setValue(university.getText().toString());
            db.child("courses").child(course.getText().toString()).setValue(course.getText().toString());

//            StorageReference riversRef = mStorageRef.child("uploads").child(university.toString()).child(teacher.toString()).child(course.toString()).child(year.toString()).child(simester.toString()).child(MimeTypeMap.getFileExtensionFromUrl(filePath.toString()));
            StorageReference riversRef = mStorageRef.child(path);
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            Toast.makeText(getApplicationContext(), "File null ", Toast.LENGTH_LONG).show();        }
    }
}
