package com.ass2.i190426_i190435;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainPage extends AppCompatActivity {

    TextView logout;
    BottomNavigationView mNavigationBottom;
    FirebaseAuth mAuth;
    DrawerLayout drawer;
    ImageView menu;
    TextView username;
    CircleImageView dp;
    TextView edit;
    Uri photoUrl;
    RecyclerView rv;
    List<Music> ls;
    List<Music> temp;
    MyAdapterHorizontalMusic adapterHorizontalMusic;
    ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        logout=findViewById(R.id.logout);
        mNavigationBottom=findViewById(R.id.mNavigationBottom);
        mAuth=FirebaseAuth.getInstance();

        menu=findViewById(R.id.menu);
        drawer=findViewById(R.id.drawer);
        username=findViewById(R.id.userName);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        username.setText(user.getDisplayName());
        dp=findViewById(R.id.dp);
        edit=findViewById(R.id.edit);
        rv = findViewById(R.id.rv);
        ls=new ArrayList<>();
        temp=new ArrayList<>();


        adapterHorizontalMusic=new MyAdapterHorizontalMusic(ls, MainPage.this);
        rv.setAdapter(adapterHorizontalMusic);
        RecyclerView.LayoutManager lm=new LinearLayoutManager(MainPage.this, LinearLayoutManager.HORIZONTAL, false);
        rv.setLayoutManager(lm);

        starting();


        if (user != null) {
            photoUrl = user.getProviderData().get(0).getPhotoUrl();
            Picasso.get().load(photoUrl.toString()).into(dp);
        }

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawer.isDrawerOpen(Gravity.LEFT)){
                    drawer.closeDrawer(Gravity.LEFT);
                }
                else{
                    drawer.openDrawer(Gravity.LEFT);
                }
            }
        });

//        edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(MainPage.this, EditProfile.class);
//                startActivity(intent);
//            }
//        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                        startActivity(new Intent(MainPage.this, MainActivity2.class));
                        break;
                    case R.id.page_2:
                        startActivity(new Intent(MainPage.this, AddMusic.class));
                        break;
                    case R.id.page_3:
                        startActivity(new Intent(MainPage.this, Search.class));
                        break;
                }
                return true;
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        mNavigationBottom.getMenu().setGroupCheckable(0, true, false);
        for (int i=0; i<mNavigationBottom.getMenu().size(); i++) {
            mNavigationBottom.getMenu().getItem(i).setChecked(false);
        }
        mNavigationBottom.getMenu().setGroupCheckable(0, true, true);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                ls=new ArrayList<>();
                for (DataSnapshot child: children) {
                    Music value = child.getValue(Music.class);
                    ls.add(value);

                }

                adapterHorizontalMusic=new MyAdapterHorizontalMusic(ls, MainPage.this);
                adapterHorizontalMusic.notifyDataSetChanged();

                rv.setAdapter(adapterHorizontalMusic);
                RecyclerView.LayoutManager lm=new LinearLayoutManager(MainPage.this, LinearLayoutManager.HORIZONTAL, true);
                rv.setLayoutManager(lm);
                System.out.println("Size is: "+ls.size());
//                Toast.makeText(MainPage.this, ls.size()+"", Toast.LENGTH_SHORT).show();
                System.out.println(adapterHorizontalMusic.getItemCount());

                databaseReference.child("music").removeEventListener(valueEventListener);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        databaseReference.child("music").addValueEventListener(valueEventListener);
    }


    public void starting() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                ls=new ArrayList<>();
                for (DataSnapshot child: children) {
                    Music value = child.getValue(Music.class);
                    ls.add(value);

                }

                adapterHorizontalMusic=new MyAdapterHorizontalMusic(ls, MainPage.this);
                adapterHorizontalMusic.notifyDataSetChanged();

                rv.setAdapter(adapterHorizontalMusic);
                RecyclerView.LayoutManager lm=new LinearLayoutManager(MainPage.this, LinearLayoutManager.HORIZONTAL, true);
                rv.setLayoutManager(lm);
                System.out.println("Size is: "+ls.size());
//                Toast.makeText(MainPage.this, ls.size()+"", Toast.LENGTH_SHORT).show();
                System.out.println(adapterHorizontalMusic.getItemCount());

                databaseReference.child("music").removeEventListener(valueEventListener);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        databaseReference.child("music").addValueEventListener(valueEventListener);



    }


}