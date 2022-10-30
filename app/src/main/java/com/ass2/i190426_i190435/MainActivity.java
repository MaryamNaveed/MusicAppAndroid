package com.ass2.i190426_i190435;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    Handler handler;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth=FirebaseAuth.getInstance();
        setContentView(R.layout.activity_main);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if(mAuth.getCurrentUser()!=null){
                    startActivity(new Intent(MainActivity.this, MainPage.class));
                }
                else{
                    Intent intent= new Intent(MainActivity.this, CreateAccount.class);
                    startActivity(intent);
                }

                finish();
            }
        }, 5000);
    }


}