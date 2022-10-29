package com.ass2.i190426_i190435;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayMusic extends AppCompatActivity implements Play {

    TextView title;
    ImageView image;
    String songtitle;
    BottomNavigationView mNavigationBottom;
    String songimage;
    String songurl;
    MediaPlayer mediaPlayer = new MediaPlayer();
    SeekBar seekBar;
    ImageView play, next, prev;
    boolean isPause=false;
    int pauseLength=0;
    List<Music> ls;
    int position=0;
    Uri myUri;
    int songDuration;
    private Handler mHandler = new Handler();
    NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);

        title=findViewById(R.id.title);
        image=findViewById(R.id.image);
        seekBar=findViewById(R.id.seekbar);
        play=findViewById(R.id.play);
        next=findViewById(R.id.next);
        prev=findViewById(R.id.prev);

        ls= UtilityClassMusic.getInstance().getList();
        position=getIntent().getIntExtra("position", position);




        playMusic();

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPause){
                    CreateNotificationMusic.createNotification(PlayMusic.this, ls.get(position),
                            R.drawable.pause, position, ls.size()-1);

                    mediaPlayer.seekTo(pauseLength);
                    mediaPlayer.start();
                    isPause=false;
                    play.setImageResource(R.drawable.pause);
                }
                else{
                    CreateNotificationMusic.createNotification(PlayMusic.this, ls.get(position),
                            R.drawable.play, position, ls.size()-1);

                    mediaPlayer.pause();
                    pauseLength=mediaPlayer.getCurrentPosition();
                    isPause=true;
                    play.setImageResource(R.drawable.play);
                }

            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                play.setImageResource(R.drawable.play);
                isPause=true;
                pauseLength=0;
                CreateNotificationMusic.createNotification(PlayMusic.this, ls.get(position),
                        R.drawable.play, position, ls.size()-1);

            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPause=false;
                play.setImageResource(R.drawable.pause);
                position=position-1;
                pauseLength=0;
                if(position<0){
                    position=0;
                }
                playMusic();

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPause=false;
                play.setImageResource(R.drawable.pause);
                position=position+1;
                pauseLength=0;
                if(position>=ls.size()){
                    position=ls.size()-1;
                }
                playMusic();
            }
        });


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




        PlayMusic.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(mediaPlayer != null){
                    songDuration=mediaPlayer.getDuration();
                    seekBar.setMax(songDuration/100);
                    int mCurrentPosition = mediaPlayer.getCurrentPosition() / 100;
                    seekBar.setProgress(mCurrentPosition);
                }
                mHandler.postDelayed(this, 100);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer != null && fromUser){
                    mediaPlayer.seekTo(progress * 1000);
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CreateNotificationMusic.CHANNEL_ID,
                    "Name", NotificationManager.IMPORTANCE_LOW);
            notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null){
                notificationManager.createNotificationChannel(channel);
            }
            registerReceiver(broadcastReceiver, new IntentFilter("TRACKS_TRACKS"));
            startService(new Intent(getBaseContext(), ServiceMusic.class));

        }


    }

    public void playMusic(){

        pauseLength=0;

        mediaPlayer.reset();
        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );

        songtitle=ls.get(position).getTitle();
        songimage = ls.get(position).getImage();
        songurl = ls.get(position).getLink();
        myUri = Uri.parse(songurl);

        Picasso.get().load(songimage).into(image);

        title.setText(songtitle);
        CreateNotificationMusic.createNotification(PlayMusic.this, ls.get(position),
                R.drawable.pause, position, ls.size()-1);


        System.out.println(songurl+ " "+position);

        try {
            mediaPlayer.setDataSource(getApplicationContext(), myUri);

            mediaPlayer.prepare();
            mediaPlayer.start();
            //Toast.makeText(PlayMusic.this, "started", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(PlayMusic.this, "error", Toast.LENGTH_LONG).show();
        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getExtras().getString("actionname");

            switch (action){
                case CreateNotificationMusic.PREV:
                    onPrevious();
                    break;
                case CreateNotificationMusic.PLAY:
                    if (isPause){
                        onPlayMusic();
                    } else {
                        onPauseMusic();

                    }
                    break;
                case CreateNotificationMusic.NEXT:
                    onNext();
                    break;
            }
        }
    };

    @Override
    public void onPrevious() {

        position=position-1;
        CreateNotificationMusic.createNotification(PlayMusic.this, ls.get(position),
                R.drawable.music_note, position, ls.size()-1);
        title.setText(ls.get(position).getTitle());
        playMusic();

    }

    @Override
    public void onPlayMusic() {
        CreateNotificationMusic.createNotification(PlayMusic.this, ls.get(position),
                R.drawable.pause, position, ls.size()-1);
        play.setImageResource(R.drawable.pause);
        title.setText(ls.get(position).getTitle());
        isPause=false;
        mediaPlayer.seekTo(pauseLength);
        mediaPlayer.start();

    }

    @Override
    public void onPauseMusic() {
        CreateNotificationMusic.createNotification(PlayMusic.this, ls.get(position),
                R.drawable.play, position, ls.size()-1);
        play.setImageResource(R.drawable.play);
        title.setText(ls.get(position).getTitle());
        mediaPlayer.pause();
        pauseLength=mediaPlayer.getCurrentPosition();
        isPause=true;

    }

    @Override
    public void onNext() {
        position=position+1;
        CreateNotificationMusic.createNotification(PlayMusic.this, ls.get(position),
                R.drawable.music_note, position, ls.size()-1);
        title.setText(ls.get(position).getTitle());
        playMusic();

    }


}