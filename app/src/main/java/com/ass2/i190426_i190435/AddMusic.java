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
    boolean selected = false, isSelected=false;
    Uri selectedImage=null;
    String audio, image;

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

                    case R.id.page_3:
                        startActivity(new Intent(AddMusic.this, Search.class));
                        break;
                }
                return true;
            }
        });
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isSelected==false){
                    Toast.makeText(AddMusic.this, "Please select image", Toast.LENGTH_LONG).show();
                }
                else{
                    Intent intent=new Intent(AddMusic.this, RecordMusic.class);
                    intent.putExtra("title", title.getText().toString());
                    intent.putExtra("genre", genre.getText().toString());
                    intent.putExtra("description", description.getText().toString());
                    intent.putExtra("image", selectedImage.toString()
                    );
                    startActivity(intent);
                }

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
                else if(isSelected==false){
                    Toast.makeText(AddMusic.this, "Please select image", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(
                            AddMusic.this,
                            "Uploading Started",
                            Toast.LENGTH_LONG
                    ).show();
                    Calendar c=Calendar.getInstance();
                    StorageReference ref = storage.getReference().child("Music/music"+mAuth.getCurrentUser().getUid()+c.getTimeInMillis()+".mp3");


                    ref.putFile(music).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(
                                    AddMusic.this,
                                    "Uploaded Song to storage",
                                    Toast.LENGTH_LONG
                            ).show();
                            Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                            task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    audio = uri.toString();


                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference myRef = database.getReference("music");
                                    FirebaseStorage storage = FirebaseStorage.getInstance();
                                    Calendar c = Calendar.getInstance();

                                    StorageReference ref = storage.getReference().child("MusicImages/image"+mAuth.getCurrentUser()+c.getTimeInMillis()+".jpg");

                                    ref.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            Toast.makeText(
                                                    AddMusic.this,
                                                    "Uploaded Image to storage",
                                                    Toast.LENGTH_LONG
                                            ).show();
                                            Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                                            task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    Toast.makeText(
                                                            AddMusic.this,
                                                            "Uploading to database",
                                                            Toast.LENGTH_LONG
                                                    ).show();
                                                    image=uri.toString();
                                                    Music m = new Music(
                                                            title.getText().toString(),
                                                            genre.getText().toString(),
                                                            description.getText().toString(),
                                                            audio,
                                                            image
                                                    );

                                                    DatabaseReference abc = myRef.push();
                                                    abc.setValue(m);
                                                    Toast.makeText(
                                                            AddMusic.this,
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
                                                    AddMusic.this,
                                                    "Failed",
                                                    Toast.LENGTH_LONG
                                            ).show();
                                        }
                                    });;





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

        linktext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/*");
                startActivityForResult(Intent.createChooser(intent, "Select Audio"), 20);

            }
        });

        link.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Audio"), 100);

            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==20 & resultCode==RESULT_OK){

            music = data.getData();
            linktext.setText(music.toString());
            selected=true;


        }
        if(requestCode==100 & resultCode==RESULT_OK){
            selectedImage=data.getData();
            link.setImageURI(selectedImage);
            isSelected=true;
        }
    }
}