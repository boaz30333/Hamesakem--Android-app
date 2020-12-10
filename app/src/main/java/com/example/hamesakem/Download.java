package com.example.hamesakem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class Download extends AppCompatActivity {

    FirebaseStorage storage;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        storage = FirebaseStorage.getInstance();
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if(!hasPermissions(this, PERMISSIONS)){
            //ask user for granting permissions on api22+
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }
    public void onClick(View v) throws IOException {

        // Create a storage reference from our app
        StorageReference storageRef = storage.getReferenceFromUrl("gs://hamesakem.appspot.com/uploads/a/a/a/2020/a/tNOWcdn9wrUCy5lqiT3e2Zn2uCu1.pdf");
        //displaying a progress dialog while upload is going on
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Downloading");
        progressDialog.show();
        // Create a reference with an initial file path and name
        path = "uploads/a/a/a/2020/a/tNOWcdn9wrUCy5lqiT3e2Zn2uCu1";
//        StorageReference pathReference = storageRef.child(path + ".pdf");
//        File rootPath = new File(Environment.getExternalStorageDirectory(), "Downloads");
//        if (!rootPath.exists()) {
//            rootPath.mkdirs();
//        }
        String key = path.replaceAll("/", "");
        ;
//        StorageReference islandRef = storageRef.child("images/island.jpg");
//        islandRef = storageRef.child("images/island.jpg");
//        File localFile = new File(rootPath, key + ".pdf");

//        pathReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//        storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Local temp file has been created
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "File Download ", Toast.LENGTH_LONG).show();
                String url = uri.toString();
                downloadFile(Download.this, path, ".pdf", DIRECTORY_DOWNLOADS, url);
            }
//        })
//            @Override
//            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                // Local temp file has been created
//                progressDialog.dismiss();
//                Toast.makeText(getApplicationContext(), "File Download ", Toast.LENGTH_LONG).show();
//            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                progressDialog.dismiss();

                //and displaying error message
                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                exception.printStackTrace();
            }

        });
    }
    private void downloadFile(Context context, String path, String s, String directoryDownloads, String url) {
        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, directoryDownloads, path + s);
        dm.enqueue(request);
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        //checaking if the application has permission
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}