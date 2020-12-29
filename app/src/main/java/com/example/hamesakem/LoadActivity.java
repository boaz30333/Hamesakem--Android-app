package com.example.hamesakem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoadActivity extends MenuApp implements View.OnClickListener {
//    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
//    StorageReference photoRef;
    private StorageReference mStorageRef;
    //a constant to track the file chooser intent
//    private static final int PICK_IMAGE_REQUEST = 234;
    private static final int PICK_PDF_REQUEST = 2342;
    //Buttons
//    private Button buttonChoose;
    private Button buttonUpload;
    private Button email;
    String course, teacher, year, semester, university;
//    TextView course;
//    TextView teacher;
//    TextView year;
//    TextView simester;
//    TextView university;
    //ImageView
    private ImageView imageView;
    Summary sum;

    //a Uri object to store file path
    private Uri filePath;
    static final int PICK_FILE_REQUEST = 101 , RESULT_OK = -1;
//    TextSwitcher textView;
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_load);
    MenuApp menu = new MenuApp();
    mStorageRef = FirebaseStorage.getInstance().getReference();

    //getting views from layout
//        buttonChoose = (Button) findViewById(R.id.buttonChoose);
    buttonUpload = (Button) findViewById(R.id.bsave);
    email = (Button) findViewById(R.id.email);

//        imageView = (ImageView) findViewById(R.id.imageView);

    //attaching listener
//        buttonChoose.setOnClickListener(this);
//        buttonUpload.setOnClickListener(this);
}

    public void onClick(View v){
        course = editInput(((TextView)findViewById(R.id.tecourse)).getText().toString());
        teacher = editInput(((TextView)findViewById(R.id.teteacher)).getText().toString());
        year = ((TextView)findViewById(R.id.teyear)).getText().toString().replaceAll(" ", "");
        semester = ((TextView)findViewById(R.id.tesimester)).getText().toString().toLowerCase().replaceAll(" ", "");
        university = editInput(((TextView)findViewById(R.id.teuniversity)).getText().toString());;
//        Toast.makeText(this, "course: "+ course.getText().toString(), Toast.LENGTH_LONG).show();
        if(checkInput())
            showFileChooser();
//        uploadFile();
    }

    private boolean checkInput() {
        if(university.length()==0 || university.equals("-") || course.length()==0 || course.equals("-") || teacher.length()==0 || teacher.equals("-") || year.length()==0 || semester.length()==0 || course.length()==0){
            Toast.makeText(this, "שגיאה: דרוש למלא את כל השדות ובעברית!", Toast.LENGTH_LONG).show();
            return false;
        }
//        String yr = year.replaceAll(" ", "");;
//        String sem = semester.toLowerCase().replaceAll(" ", "");

        if(year.length()!=4){
            Toast.makeText(this, " שגיאה: השנה לא תקינה!"+ year , Toast.LENGTH_LONG).show();
            return false;
        }
        for(int i=0; i<year.length(); i++) {
            if (year.charAt(i) < '0' ||  year.charAt(i) > '9') {
                Toast.makeText(this, " שגיאה: השנה לא תקינה!" + year, Toast.LENGTH_LONG).show();
                return false;
            }
        }
//        if(semester.length() != 1 || (!semester.equals("a") && !semester.equals("b") && !semester.equals("s") && !semester.equals("א") && !semester.equals("ב") && !semester.equals("ק"))){
        if(semester.length() != 1 ||  (!semester.equals("א") && !semester.equals("ב") && !semester.equals("ק"))){
            Toast.makeText(this, " שגיאה: הסימסטר לא תקין! " + semester, Toast.LENGTH_LONG).show();
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
            String userId =fAuth.getCurrentUser().getUid();
            String path = "uploads"+ "/" +university + "/" +course + "/" + teacher+ "/" + year+ "/" + semester + "/" + userId;
//            String path = "uploads"+ "/" +editInput(university.getText().toString()) + "/" +editInput(course.getText().toString()) + "/" +
//                    editInput(teacher.getText().toString()) + "/" + year.getText().toString() + "/" + simester.getText().toString().toLowerCase() + "/" + userId;

//            MimeTypeMap.getFileExtensionFromUrl(filePath.toString())
//            StorageReference riversRef = mStorageRef.child("uploads").child(university.toString()).child(teacher.toString()).child(course.toString()).child(year.toString()).child(simester.toString()).child(MimeTypeMap.getFileExtensionFromUrl(filePath.toString()));
            StorageReference riversRef = mStorageRef.child(path+".pdf" );
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();
                            updateRealTimeDB(userId, path);

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
            Toast.makeText(getApplicationContext(), "File not found ", Toast.LENGTH_LONG).show();        }
    }

    private void updateRealTimeDB(String userId, String path) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("sum");

        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        final User[] user = new User[1];
        DocumentReference docRef = fStore.collection("users").document(userId);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
//                    User user = User.getUser(sum_array.get(position).userId);
                    user[0] = document.toObject(User.class);
                    user[0].setNum_of_sum(user[0].getNum_of_sum()+1);
//
//                    user[0].setNum_of_rates(user[0].getNum_of_rates()+1);
//                    user[0].setRank(user[0].getRank()+rating);
                    user[0].updateFirestore();
//                    name_from_id[0] = (String) document.getData().get("fullName");
                    Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                } else {
                    Log.d("TAG", "No such document");
                }
            } else {
                Log.d("TAG", "get failed with ", task.getException());
            }
        });

        sum = new Summary(teacher, semester, course, university, path+ ".pdf", userId);
//        Summary sum = new Summary(teacher.getText().toString(), simester.getText().toString(), course.getText().toString(), university.getText().toString(), path+"." +MimeTypeMap.getFileExtensionFromUrl(filePath.toString()), userId);
        final String keyName = path.replaceAll("/", "")+ "-" +userId;
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(keyName)) {
                    myRef.child(keyName).setValue(sum);
                }
                else {
                    myRef.child(keyName).setValue(sum);
                    DatabaseReference db = database.getReference();
                    updateValue(db, "universities", university);
                    updateValue(db, "courses", course);
                    updateValue(db, "lecturer", teacher);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        db.child("universities").child(university.getText().toString()).setValue(university.getText().toString());
//        db.child("courses").child(course.getText().toString()).setValue(course.getText().toString());
//        db.child("lecturer").child(teacher.getText().toString()).setValue(teacher.getText().toString());
    }
    //could be a bug when uploads summary with the same path, its update value again, but has just one summary.
    private void updateValue( DatabaseReference db,  String parent, String child) {
        db.child(parent).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(child)) {
                    db.child(parent).child(child).setValue((Long)(snapshot.child(child).getValue())+1);
                }
                else
                    db.child(parent).child(child).setValue(1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String editInput(String str) {
        str = str.toLowerCase();
        for(int i = 0; i < str.length(); i++){
//            if(!(str.charAt(i) >= 'a' && str.charAt(i) <= 'z') && !(str.charAt(i) >= 'א' && str.charAt(i) <= 'ת') && !(str.charAt(i) != ' ' || (str.charAt(i) != '-') )) {
            if(!(str.charAt(i) >= 'א' && str.charAt(i) <= 'ת')) {
                if((str.charAt(i) == ' ' || (str.charAt(i) == '-')) && i!=0 && (str.charAt(i-1) >= 'א' && str.charAt(i-1) <= 'ת') && i!=str.length()-1 && (str.charAt(i+1) >= 'א' && str.charAt(i+1) <= 'ת') ) {
                    str = str.replaceFirst(""+str.charAt(i), "-");
                }
                else {
                    str = str.replaceFirst(""+str.charAt(i), "");
                    i--;
                    }
            }
            System.out.println("str: "+ str);
        }
//        return str.replaceAll(" ", "-");
        return str;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu, (android.view.Menu) menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.setting:
//                setting();
//                return true;
//            case R.id.search:
//                search();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    public void onClick2(View v){
//        Intent intent=new Intent(this,DownloadFile.class);
//        startActivity(intent);
        SendEmail se =new SendEmail(LoadActivity.this);
        //send to..
        se.send("itamarzo0@gmail.com");
    }
}
