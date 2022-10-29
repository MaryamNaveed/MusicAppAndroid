package com.ass2.i190426_i190435;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class RecordMusic extends AppCompatActivity {

    BottomNavigationView mNavigationBottom;
    ImageView recording, image;
    MediaRecorder recorder;
    String fileName;
    boolean start=false;
    FirebaseAuth mAuth;
    Uri selectedImage=null;
    String img, audio;
    TextView recorded;
    ImageView pause;
    boolean isPause=true;
    int pauseLength=0;
    MediaPlayer mediaPlayer = new MediaPlayer();
    boolean firstTime = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_music);

        mAuth=FirebaseAuth.getInstance();
        mNavigationBottom=findViewById(R.id.mNavigationBottom);
        recorded=findViewById(R.id.recorded);
        pause=findViewById(R.id.pause);

        mNavigationBottom.setSelectedItemId(R.id.page_2);

        mNavigationBottom.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.page_1:
                        startActivity(new Intent(RecordMusic.this, MainActivity2.class));
                        break;
                    case R.id.page_2:
                        startActivity(new Intent(RecordMusic.this, AddMusic.class));
                        break;
                    case R.id.page_3:
                        startActivity(new Intent(RecordMusic.this, Search.class));
                        break;
                }
                return true;
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                pause.setImageResource(R.drawable.play);
                isPause=true;
                pauseLength=0;

            }
        });

        recording=findViewById(R.id.recording);

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File music = cw.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        Calendar c=Calendar.getInstance();
        File file = new File(music, "audio"+c.getTimeInMillis()+".mp3");
        fileName=file.getPath();

        image=findViewById(R.id.image);
        img = getIntent().getStringExtra("image");
        selectedImage=Uri.parse(img);

        System.out.println("String image: "+img);

        image.setImageURI(selectedImage);



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

        recorded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recorded.getText().toString()=="upload"){
                    uploadToFirebase();
                }
                else{

                }

            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recorded.getText().toString()=="upload"){
                    if(firstTime){
                        firstTime=false;
                        playMusic();
                    }
                    else{
                        if(isPause){
                            mediaPlayer.seekTo(pauseLength);
                            mediaPlayer.start();
                            isPause=false;
                            pause.setImageResource(R.drawable.pause);
                        }
                        else{
                            mediaPlayer.pause();
                            pauseLength=mediaPlayer.getCurrentPosition();
                            isPause=true;
                            pause.setImageResource(R.drawable.play);
                        }
                    }


                }


            }
        });


    }
    private void startRecording() {
        recorded.setText("");
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
        recorded.setText("upload");
        pause.setImageResource(R.drawable.play);

    }

    public void uploadToFirebase(){

        Uri uri1 = Uri.fromFile(new File(fileName));
        FirebaseStorage storage = FirebaseStorage.getInstance();
        Calendar c=Calendar.getInstance();
        StorageReference ref = storage.getReference().child("Music/audio"+mAuth.getCurrentUser().getUid()+c.getTimeInMillis()+".mp3");
        ref.putFile(uri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(
                        RecordMusic.this,
                        "Uploaded Song to storage",
                        Toast.LENGTH_LONG
                ).show();
                Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                        task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                audio=uri.toString();

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference("music");
                                FirebaseStorage storage = FirebaseStorage.getInstance();
                                Calendar c = Calendar.getInstance();

                                StorageReference ref = storage.getReference().child("MusicImages/image"+mAuth.getCurrentUser()+c.getTimeInMillis()+".jpg");

                                ref.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Toast.makeText(
                                                RecordMusic.this,
                                                "Uploaded Image to storage",
                                                Toast.LENGTH_LONG
                                        ).show();
                                        Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                                        task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                Intent intent=getIntent();
                                                String title = intent.getStringExtra("title");
                                                String genre = intent.getStringExtra("genre");
                                                String description = intent.getStringExtra("description");
                                                Toast.makeText(
                                                        RecordMusic.this,
                                                        "Uploading to database",
                                                        Toast.LENGTH_LONG
                                                ).show();

                                                Music m = new Music(
                                                        title,
                                                        genre,
                                                        description,
                                                        audio,
                                                        uri.toString()
                                                );

                                                DatabaseReference abc = myRef.push();
                                                abc.setValue(m);
                                                Toast.makeText(
                                                        RecordMusic.this,
                                                        "Added to database",
                                                        Toast.LENGTH_LONG
                                                ).show();
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
                                });;



                            }
                        });
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

    public void playMusic(){

        pause.setImageResource(R.drawable.pause);

        pauseLength=0;
        mediaPlayer.reset();
        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );


        Uri myUri = Uri.fromFile(new File(fileName));


        try {
            mediaPlayer.setDataSource(getApplicationContext(), myUri);
            mediaPlayer.prepare();
            mediaPlayer.start();
//            Toast.makeText(RecordMusic.this, "started", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(RecordMusic.this, "error", Toast.LENGTH_LONG).show();
        }
    }

}