package com.ass2.i190426_i190435;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditProfile extends AppCompatActivity {

    BottomNavigationView mNavigationBottom;
    FirebaseAuth mAuth;
    EditText newemail, newpassword, prevemail, prevpassword;
    TextView show, save, show1, username;
    boolean showed = false, showed1 = false;
    Uri photoUrl;
    Uri selectedImage;
    ImageView profile;
    boolean changed=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mNavigationBottom=findViewById(R.id.mNavigationBottom);
        mAuth=FirebaseAuth.getInstance();
        newemail=findViewById(R.id.newemail);
        show=findViewById(R.id.show);
        show1=findViewById(R.id.show1);
        save = findViewById(R.id.save);
        profile = findViewById(R.id.profile);
        username=findViewById(R.id.username);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        username.setText(user.getDisplayName());


        newpassword=findViewById(R.id.newpassword);

        if (user != null) {
            photoUrl = user.getProviderData().get(0).getPhotoUrl();
            Picasso.get().load(photoUrl.toString()).into(profile);

        }

        prevemail=findViewById(R.id.prevemail);
        prevpassword=findViewById(R.id.prevpassword);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(
                        Intent.createChooser(i, "Choose your Dp"),
                        200
                );
            }
        });



        mNavigationBottom.getMenu().setGroupCheckable(0, true, false);
        for (int i=0; i<mNavigationBottom.getMenu().size(); i++) {
            mNavigationBottom.getMenu().getItem(i).setChecked(false);
        }
        mNavigationBottom.getMenu().setGroupCheckable(0, true, true);

        mNavigationBottom.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.page_1:
                        startActivity(new Intent(EditProfile.this, LikedMusic.class));
                        break;
                    case R.id.page_2:
                        startActivity(new Intent(EditProfile.this, AddMusic.class));
                        break;
                }
                return true;
            }
        });

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(showed==false){
                    newpassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    showed=true;
                }
                else{
                    newpassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    showed=false;

                }
            }
        });

        show1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(showed1==false){
                    prevpassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    showed1=true;
                }
                else{
                    prevpassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    showed1=false;

                }
            }
        });


        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String em = prevemail.getText().toString().replace(".","");
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                AuthCredential credential = EmailAuthProvider
                        .getCredential(prevemail.getText().toString(), prevpassword.getText().toString());
                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        user.updateEmail(newemail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(EditProfile.this, "Email reset", Toast.LENGTH_SHORT).show();
                                    AuthCredential credential = EmailAuthProvider
                                            .getCredential(newemail.getText().toString(), prevpassword.getText().toString());
                                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            user.updatePassword(newpassword.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(EditProfile.this, "Password reset", Toast.LENGTH_SHORT).show();

                                                                if(changed){


                                                                    FirebaseStorage storage = FirebaseStorage.getInstance();

                                                                    StorageReference ref = storage.getReference().child("Dp/mydp"+mAuth.getCurrentUser()+".jpg");

                                                                    ref.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                        @Override
                                                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                            Toast.makeText(EditProfile.this,
                                                                                    "Pic uploaded to storage",
                                                                                    Toast.LENGTH_LONG).show();
                                                                            Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                                                                            task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                                @Override
                                                                                public void onSuccess(Uri uri) {

                                                                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                                                            .setDisplayName(user.getDisplayName())
                                                                                            .setPhotoUri(Uri.parse(uri.toString()))
                                                                                            .build();

                                                                                    user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            if (task.isSuccessful()) {
                                                                                                DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference();
                                                                                                ref1.child("user").child(em).child("dp")
                                                                                                        .setValue(uri.toString())
                                                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                            @Override
                                                                                                            public void onSuccess(Void unused) {
                                                                                                                DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference();
                                                                                                                ref1.child("user").child(em).child("email")
                                                                                                                        .setValue(newemail.getText().toString())
                                                                                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                    @Override
                                                                                                                                    public void onSuccess(Void unused) {
                                                                                                                                        Toast.makeText(EditProfile.this,
                                                                                                                                                "Profile updated in database",
                                                                                                                                                Toast.LENGTH_LONG).show();
                                                                                                                                        startActivity(new Intent(EditProfile.this,MainPage.class));

                                                                                                                                    }
                                                                                                                                });


                                                                                                            }
                                                                                                        }).addOnFailureListener(new OnFailureListener() {
                                                                                                            @Override
                                                                                                            public void onFailure(@NonNull Exception e) {
                                                                                                                Toast.makeText(EditProfile.this,
                                                                                                                        "Error",
                                                                                                                        Toast.LENGTH_LONG).show();
                                                                                                            }
                                                                                                        });


                                                                                            }
                                                                                        }
                                                                                    });
                                                                                }
                                                                            });
                                                                        }
                                                                    });

                                                                }
                                                                else{
                                                                    DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference();
                                                                    ref1.child("user").child(em).child("email")
                                                                            .setValue(newemail.getText().toString())
                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void unused) {
                                                                                    Toast.makeText(EditProfile.this,
                                                                                            "Profile updated in database",
                                                                                            Toast.LENGTH_LONG).show();
                                                                                    startActivity(new Intent(EditProfile.this,MainPage.class));

                                                                                }
                                                                            });

                                                                }









                                                            }
                                                            else{
                                                                Toast.makeText(EditProfile.this, "Password could not reset", Toast.LENGTH_SHORT).show();

                                                            }
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(EditProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(EditProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                else{
                                    Toast.makeText(EditProfile.this, "Email could not reset", Toast.LENGTH_SHORT).show();

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });




            }
        });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==200 && resultCode==RESULT_OK){
            selectedImage=data.getData();
            profile.setImageURI(selectedImage);
            changed=true;
        }
    }
}