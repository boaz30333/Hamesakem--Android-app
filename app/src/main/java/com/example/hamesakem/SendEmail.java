package com.example.hamesakem;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class SendEmail {
    Activity act;
    public SendEmail(Activity act){
           this.act = act;
    }
    @SuppressLint("LongLogTag")
    public void send( String to_email){
        Log.i("Send email", "");

        String[] TO = {to_email};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

        try {
            act.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            act.finish();
            Log.i("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(act,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
