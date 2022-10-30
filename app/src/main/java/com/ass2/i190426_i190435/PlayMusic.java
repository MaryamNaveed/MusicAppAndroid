package com.ass2.i190426_i190435;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ImageViewCompat;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayMusic extends AppCompatActivity implements Play {


    int position=0;
    TextView title;
    ImageView image, listenLater, like;
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
    Uri myUri;
    int songDuration;
    private Handler mHandler = new Handler();
    NotificationManager notificationManager;
    boolean foundLike, foundListen = false;




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
        like=findViewById(R.id.like);
        listenLater=findViewById(R.id.listenLater);


        ls= UtilityClassMusic.getInstance().getList();
        position=getIntent().getIntExtra("position", position);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();


        playMusic();


        Query query = ref.child("like").orderByChild("link").equalTo(ls.get(position).getLink());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot appleSnapshot: snapshot.getChildren()) {
                    foundLike = true;
                    like.setColorFilter(R.color.grey);


                }

                if(foundLike==false) {
                    like.setColorFilter(Color.rgb(255,255,255));

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Query query1 = ref.child("listenLater").orderByChild("link").equalTo(ls.get(position).getLink());

        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot appleSnapshot: snapshot.getChildren()) {
                    foundListen = true;
                    listenLater.setColorFilter(R.color.grey);


                }

                if(foundListen==false) {
                    listenLater.setColorFilter(Color.rgb(255,255,255));

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {

//                ImageViewCompat.setImageTintList(like, ColorStateList.valueOf(R.color.white));



                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                String link=ls.get(position).getLink();

                Query query = ref.child("like").orderByChild("link").equalTo(ls.get(position).getLink());

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot appleSnapshot: snapshot.getChildren()) {

                            foundLike = true;
                            like.setColorFilter(Color.rgb(255,255,255));
                            appleSnapshot.getRef().removeValue();
                            Toast.makeText(PlayMusic.this, "Removed Successfully from Like", Toast.LENGTH_SHORT).show();


                        }

                        if(foundLike==false){
                            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("like");
                            like.setColorFilter(R.color.grey);
                            Music m = new Music(
                                    ls.get(position).getTitle(),
                                    ls.get(position).getGenre(),
                                    ls.get(position).getDescription(),
                                    ls.get(position).getLink(),
                                    ls.get(position).getImage()
                            );

                            DatabaseReference abc = myRef.push();
                            abc.setValue(m);
                            Toast.makeText(PlayMusic.this, "Added Successfully to Like", Toast.LENGTH_SHORT).show();

                        }
                        else{
                            foundLike=false;
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(PlayMusic.this, "error", Toast.LENGTH_SHORT).show();
                    }
                });





            }
        });
        listenLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Query query1 = ref.child("listenLater").orderByChild("link").equalTo(ls.get(position).getLink());

                query1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot appleSnapshot: snapshot.getChildren()) {
                            foundListen = true;
                            listenLater.setColorFilter(Color.rgb(255, 255, 255));
                            appleSnapshot.getRef().removeValue();
                            Toast.makeText(PlayMusic.this, "Removed Successfully from Listen Later", Toast.LENGTH_SHORT).show();


                        }

                        if(foundListen==false) {
                            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("listenLater");

                            Music m = new Music(
                                    ls.get(position).getTitle(),
                                    ls.get(position).getGenre(),
                                    ls.get(position).getDescription(),
                                    ls.get(position).getLink(),
                                    ls.get(position).getImage()
                            );

                            DatabaseReference abc = myRef.push();
                            abc.setValue(m);
                            Toast.makeText(PlayMusic.this, "Added Successfully to Listen Later", Toast.LENGTH_SHORT).show();

                            listenLater.setColorFilter(R.color.grey);

                        }
                        else {
                            foundListen=false;
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });





        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPause){
                    onPlayMusic();
//                    CreateNotificationMusic.createNotification(PlayMusic.this, ls.get(position),
//                            R.drawable.pause, position, ls.size()-1);
//
//                    mediaPlayer.seekTo(pauseLength);
//                    mediaPlayer.start();
//                    isPause=false;
//                    play.setImageResource(R.drawable.pause);
                }
                else{
                    onPauseMusic();
//                    CreateNotificationMusic.createNotification(PlayMusic.this, ls.get(position),
//                            R.drawable.play, position, ls.size()-1);
//
//                    mediaPlayer.pause();
//                    pauseLength=mediaPlayer.getCurrentPosition();
//                    isPause=true;
//                    play.setImageResource(R.drawable.play);
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
                onPrevious();
//                isPause=false;
//                play.setImageResource(R.drawable.pause);
//                position=position-1;
//                if(position<0){
//                    position=0;
//                }
//                pauseLength=0;
//                /*CreateNotificationMusic.createNotification(PlayMusic.this, ls.get(position),
//                        R.drawable.music_note, position, ls.size()-1);*/
//
//                playMusic();

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNext();
//                isPause=false;
//                play.setImageResource(R.drawable.pause);
//                position=position+1;
//                if(position>=ls.size()){
//                    position=ls.size()-1;
//                }
//                pauseLength=0;
//                /*CreateNotificationMusic.createNotification(PlayMusic.this, ls.get(position),
//                        R.drawable.music_note, position, ls.size()-1);*/
//
//                playMusic();
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
                        startActivity(new Intent(PlayMusic.this, LikedMusic.class));
                        break;
                    case R.id.page_2:
                        startActivity(new Intent(PlayMusic.this, AddMusic.class));
                        break;
                    case R.id.page_3:
                        startActivity(new Intent(PlayMusic.this, Search.class));
                        break;
                    case R.id.page_4:
                        startActivity(new Intent(PlayMusic.this, ListenLaterMusic.class));
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

        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }



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
        play.setImageResource(R.drawable.pause);
        if(position<0){
            position=0;
        }
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
        play.setImageResource(R.drawable.pause);
        if(position>=ls.size()){
            position=ls.size()-1;
        }
        CreateNotificationMusic.createNotification(PlayMusic.this, ls.get(position),
                R.drawable.music_note, position, ls.size()-1);
        title.setText(ls.get(position).getTitle());
        playMusic();

    }

    @Override
    protected void onResume() {
        super.onResume();

        mNavigationBottom.getMenu().setGroupCheckable(0, true, false);
        for (int i=0; i<mNavigationBottom.getMenu().size(); i++) {
            mNavigationBottom.getMenu().getItem(i).setChecked(false);
        }
        mNavigationBottom.getMenu().setGroupCheckable(0, true, true);

    }
}