package com.example.hamesakem;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class DownloadFile {

    FirebaseStorage storage;
    String path, pdf;
    Activity act;

   public DownloadFile(Activity act, String path) {
        storage = FirebaseStorage.getInstance();
        this.act = act;
        String[] fullPath = path.split("\\.");
        this.path = fullPath[0];
        this.pdf = "." + fullPath[1];
        permissions();

    }


    private void permissions() {
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!hasPermissions(act, PERMISSIONS)) {
            //ask user for granting permissions on api22+
            ActivityCompat.requestPermissions(act, PERMISSIONS, PERMISSION_ALL);
        }
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
    public void down(){
            // Create a storage reference from our app
            StorageReference storageRef = storage.getReferenceFromUrl("gs://hamesakem.appspot.com/"+ path + pdf);
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(act);
            progressDialog.setTitle("Downloading");
            progressDialog.show();
            String key = path.replaceAll("/", "");
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Local temp file has been created
                progressDialog.dismiss();
                Toast.makeText(act.getApplicationContext(), "File Download ", Toast.LENGTH_LONG).show();
                String url = uri.toString();
                downloadFile(act, path, pdf, DIRECTORY_DOWNLOADS, url);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                progressDialog.dismiss();

                //and displaying error message
                Toast.makeText(act.getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
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
}
