package com.example.hamesakem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.GpsStatus;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.ImageSwitcher;
import android.widget.TextSwitcher;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.prefs.Preferences;

import static android.app.Activity.RESULT_OK;
import static androidx.core.app.ActivityCompat.startActivityForResult;

public class UploadFile  extends Activity {
    static final int PICK_FILE_REQUEST = 101 , RESULT_OK = -1 ,PICK_IMAGE_REQUEST=1;;
    TextSwitcher textView;
    Uri filePath;
    StorageReference storageRef;
    public UploadFile()
    {
//        textView = new TextSwitcher();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        showFileChooser();
        uploadFile();
    }
    private void showFileChooser() {
//        Intent intent = new Intent();
//        intent.setType("text/* application/pdf");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select file"), PICK_FILE_REQUEST);
//        String path = "";
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT), chooser = null;
//        intent.setType("text/* application/pdf");
//        chooser = Intent.createChooser(intent, "Find file to Print");
//        startActivityForResult(chooser, PICK_FILE_REQUEST);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
//        path = pathToFile;
//        return path;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
//            textView.setText(filePath.toString());

        }
    }

    private void uploadFile() {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            StorageReference sRef = storageRef.child("uploads/" + System.currentTimeMillis() + "." + MimeTypeMap.getFileExtensionFromUrl(filePath.toString()));
            sRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//displaying the upload progress
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
    }

}
