package com.ass2.i190426_i190435;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.Calendar;

public class AddMusic extends AppCompatActivity {
    BottomNavigationView mNavigationBottom;
    TextView record, linktext;
    EditText title, genre, description;
    ImageView link;
    Button upload;
    Uri music;
    FirebaseAuth mAuth;
    boolean selected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_music);

        mAuth=FirebaseAuth.getInstance();

        mNavigationBottom=findViewById(R.id.mNavigationBottom);

        record = findViewById(R.id.record);
        linktext=findViewById(R.id.linktext);
        title=findViewById(R.id.title);
        genre=findViewById(R.id.genre);
        description=findViewById(R.id.description);
        link=findViewById(R.id.link);
        upload=findViewById(R.id.upload);

        mNavigationBottom.setSelectedItemId(R.id.page_2);

        mNavigationBottom.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.page_1:
                        startActivity(new Intent(AddMusic.this, MainActivity2.class));
                        break;
                }
                return true;
            }
        });
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddMusic.this, RecordMusic.class));
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("music");
        FirebaseStorage storage = FirebaseStorage.getInstance();

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selected==false){
                    Toast.makeText(AddMusic.this, "Please select music", Toast.LENGTH_LONG).show();
                }
                else{
                    Calendar c=Calendar.getInstance();
                    StorageReference ref = storage.getReference().child("Music/music"+mAuth.getCurrentUser().getUid()+c.getTimeInMillis()+".mp3");


                    ref.putFile(music).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(
                                    AddMusic.this,
                                    "Uploaded to storage",
                                    Toast.LENGTH_LONG
                            ).show();
                            Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                            task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    Music m = new Music(
                                            title.getText().toString(),
                                            genre.getText().toString(),
                                            description.getText().toString(),
                                            music.toString()
                                    );


                                    DatabaseReference abc = myRef.push();
                                    abc.setValue(m);

                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(
                                    AddMusic.this,
                                    "Failed",
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    });



                }
            }
        });

        link.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/*");
                startActivityForResult(Intent.createChooser(intent, "Select Audio"), 20);

            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("Reached");
        if (requestCode==20 & resultCode==RESULT_OK){

            music = data.getData();
            linktext.setText(music.toString());
            selected=true;

//            dp.setImageURI(image);
//            Calendar c=Calendar.getInstance();
//            FirebaseStorage storage = FirebaseStorage.getInstance();
//            System.out.println("Reached 2");

//            StorageReference ref = storage.getReference().child("Music/mymusic"+mAuth.getCurrentUser().getUid()+c.getTimeInMillis()+".mp3");
//            System.out.println(ref);
//            ref.putFile(music).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(
//                            AddMusic.this,
//                            "Uploaded",
//                            Toast.LENGTH_LONG
//                    ).show();
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(
//                            AddMusic.this,
//                            "Failed",
//                            Toast.LENGTH_LONG
//                    ).show();
//                }
//            });
//            ref.putFile(music).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                    Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
//                    task.addOnSuccessListener(new OnSuccessListener<Uri>() {
//
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            System.out.println("Reached 4");
//
//                            linktext.setText(linktext.getText().toString()+uri.toString());
//
//                            //Pi.get().load(uri.toString()).into(music);
//                        }
//                    });
//
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    System.out.println("Reached 5");
//                    Toast.makeText(
//                            AddMusic.this,
//                            "Failed",
//                            Toast.LENGTH_LONG
//                    ).show();
//                }
//            });
        }
    }
}