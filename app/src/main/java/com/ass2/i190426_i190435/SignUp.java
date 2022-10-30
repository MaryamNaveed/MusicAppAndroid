package com.ass2.i190426_i190435;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.lang.annotation.Documented;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUp extends AppCompatActivity {

    TextView signin, show;
    String gender="";
    ImageView male,female,prefernottosay;
    EditText name,email, password, phone;
    Button signup;
    FirebaseAuth mAuth;
    Boolean showed = false;
    CircleImageView dp;
    Uri selectedImage= null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth=FirebaseAuth.getInstance();
        setContentView(R.layout.activity_sign_up);

        signin=findViewById(R.id.signin);
        signup=findViewById(R.id.signup);
        show=findViewById(R.id.show);
        male=findViewById(R.id.male);
        female=findViewById(R.id.female);
        prefernottosay=findViewById(R.id.prefernottosay);
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        dp = findViewById(R.id.dp);
        phone = findViewById(R.id.phone);

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gender="male";
                male.setImageResource(R.drawable.male);
                female.setImageResource(R.drawable.female1);
                prefernottosay.setImageResource(R.drawable.nottosay1);
            }
        });
        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gender="female";
                male.setImageResource(R.drawable.male1);
                female.setImageResource(R.drawable.female);
                prefernottosay.setImageResource(R.drawable.nottosay1);
            }
        });
        prefernottosay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gender="";
                male.setImageResource(R.drawable.male1);
                female.setImageResource(R.drawable.female1);
                prefernottosay.setImageResource(R.drawable.nottosay);
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(SignUp.this, SignIn.class);
                startActivity(intent);
            }
        });

        dp.setOnClickListener(new View.OnClickListener() {
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

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedImage!=null){
                    mAuth.createUserWithEmailAndPassword(
                            email.getText().toString(),
                            password.getText().toString()
                    ).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(SignUp.this,
                                    "User created",
                                    Toast.LENGTH_LONG).show();
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("user");
                            FirebaseStorage storage = FirebaseStorage.getInstance();

                            StorageReference ref = storage.getReference().child("Dp/mydp"+mAuth.getCurrentUser()+".jpg");

                            ref.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                                    task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {

                                            Toast.makeText(SignUp.this,mAuth.getCurrentUser().getUid(),Toast.LENGTH_LONG).show();

                                          User m = new User(mAuth.getCurrentUser().getUid(), name.getText().toString(), uri.toString(), email.getText().toString(), gender, phone.getText().toString());


                                            DatabaseReference abc = myRef.push();
                                            abc.setValue(m);

                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(name.getText().toString())
                                                    .setPhotoUri(Uri.parse(uri.toString()))
                                                    .build();

                                            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(SignUp.this,
                                                                "Profile done",
                                                                Toast.LENGTH_LONG).show();
                                                        startActivity(new Intent(SignUp.this, MainPage.class));
                                                    }
                                                }
                                            });

                                        }
                                    });



                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SignUp.this,
                                            "Failed to create Profile",
                                            Toast.LENGTH_LONG).show();
                                }
                            });







                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignUp.this,
                                    "Failed",
                                    Toast.LENGTH_LONG).show();

                            Toast.makeText(SignUp.this,
                                    e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else{
                    Toast.makeText(SignUp.this,
                            "Please select dp image",
                            Toast.LENGTH_LONG).show();
                }

            }
        });

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(showed==false){
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    showed=true;
                }
                else{
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    showed=false;

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==200 && resultCode==RESULT_OK){
            selectedImage=data.getData();
            dp.setImageURI(selectedImage);
        }
    }
}