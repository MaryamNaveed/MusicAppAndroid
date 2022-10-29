package com.ass2.i190426_i190435;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class PlayMusic extends AppCompatActivity {

    TextView title;
    ImageView image;
    String songtitle;
    BottomNavigationView mNavigationBottom;
    String songimage;
    String songurl;
    MediaPlayer mediaPlayer = new MediaPlayer();
    SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);

        title=findViewById(R.id.title);
        image=findViewById(R.id.image);
        seekBar=findViewById(R.id.seekbar);

        songtitle=getIntent().getStringExtra("title");
        songimage = getIntent().getStringExtra("image");
        songurl = getIntent().getStringExtra("link");


        Picasso.get().load(songimage).into(image);

        title.setText(songtitle);

        Uri myUri = Uri.parse(songurl); // initialize Uri here

        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );
        try {
            mediaPlayer.setDataSource(getApplicationContext(), myUri);
            mediaPlayer.prepare();
            mediaPlayer.start();
            Toast.makeText(PlayMusic.this, "started", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(PlayMusic.this, "error", Toast.LENGTH_LONG).show();
        }



        mNavigationBottom=findViewById(R.id.mNavigationBottom);

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
                        startActivity(new Intent(PlayMusic.this, MainActivity2.class));
                        break;
                    case R.id.page_2:
                        startActivity(new Intent(PlayMusic.this, AddMusic.class));
                        break;
                    case R.id.page_3:
                        startActivity(new Intent(PlayMusic.this, Search.class));
                        break;
                }
                return true;
            }
        });
    }
}