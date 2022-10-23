package com.ass2.i190426_i190435;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class RecordMusic extends AppCompatActivity {

    BottomNavigationView mNavigationBottom;
    ImageView recording;
    MediaRecorder recorder;
    String fileName;
    boolean start=false;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_music);

        mAuth=FirebaseAuth.getInstance();
        mNavigationBottom=findViewById(R.id.mNavigationBottom);

        mNavigationBottom.setSelectedItemId(R.id.page_2);

        recording=findViewById(R.id.recording);

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File music = cw.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        Calendar c=Calendar.getInstance();
        File file = new File(music, "audio"+c.getTimeInMillis()+".mp3");
        fileName=file.getPath();


//        fileName = getExternalCacheDir().getAbsolutePath();
//        fileName += "/audiorecord.mp3";

        recording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(start==false){
                    start=true;

                    if (ActivityCompat.checkSelfPermission(RecordMusic.this, Manifest.permission.RECORD_AUDIO)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(RecordMusic.this, new String[] { Manifest.permission.RECORD_AUDIO },
                                10);
                    } else {
                        startRecording();
                    }
                    Toast.makeText(RecordMusic.this, "Recording started", Toast.LENGTH_LONG).show();
                }
                else{
                    start=false;
                    stopRecording();
                    Toast.makeText(RecordMusic.this, "Recording stopped", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
    private void startRecording() {
        System.out.println(fileName);
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
            //Toast.makeText(RecordMusic.this, fileName, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(RecordMusic.this, "Failed", Toast.LENGTH_LONG).show();
        }

        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;

        uploadToFirebase();
    }

    public void uploadToFirebase(){



        Uri uri = Uri.fromFile(new File(fileName));
        FirebaseStorage storage = FirebaseStorage.getInstance();
        Calendar c=Calendar.getInstance();
        StorageReference ref = storage.getReference().child("Music/audio"+mAuth.getCurrentUser().getUid()+c.getTimeInMillis()+".mp3");
        ref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(
                        RecordMusic.this,
                        "Uploaded to storage",
                        Toast.LENGTH_LONG
                ).show();
                Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {


                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(
                        RecordMusic.this,
                        "Failed",
                        Toast.LENGTH_LONG
                ).show();
            }
        });
//        ref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
//                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        tv.setText(uri.toString());
//                        Picasso.get().load(uri.toString()).into(dp);
//                    }
//                });
//                Toast.makeText(
//                        MainActivity.this,
//                        "Success",
//                        Toast.LENGTH_LONG
//                ).show();;
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//            }
//        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startRecording();
            }else{
                //User denied Permission.
            }
        }
    }

    //    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                                     @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 10) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                recordAudio();
//            }else{
//                //User denied Permission.
//            }
//        }
//    }
}