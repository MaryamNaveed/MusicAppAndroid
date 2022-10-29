package com.ass2.i190426_i190435;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListenLaterMusic extends AppCompatActivity {

    RecyclerView rv;
    List<Music> ls;
    List<Music> temp;
    MyAdapterMusic adapter;
    BottomNavigationView mNavigationBottom;

    ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        rv=findViewById(R.id.rv);

        ls=new ArrayList<>();
        temp=new ArrayList<>();
        mNavigationBottom=findViewById(R.id.mNavigationBottom);


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        System.out.println(databaseReference);



        valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                temp=new ArrayList<>();
                for (DataSnapshot child: children) {
                    Music value = child.getValue(Music.class);
                    temp.add(value);
                }

                databaseReference.child("listenLater").removeEventListener(valueEventListener);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        databaseReference.child("listenLater").addValueEventListener(valueEventListener);


        adapter=new MyAdapterMusic(ls, ListenLaterMusic.this);
        rv.setAdapter(adapter);
        RecyclerView.LayoutManager lm=new LinearLayoutManager(ListenLaterMusic.this);
        rv.setLayoutManager(lm);

        starting();


        mNavigationBottom.setSelectedItemId(R.id.page_4);

        mNavigationBottom.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.page_1:
                        startActivity(new Intent(ListenLaterMusic.this, LikedMusic.class));
                        break;
                    case R.id.page_2:
                        startActivity(new Intent(ListenLaterMusic.this, AddMusic.class));
                        break;
                    case R.id.page_3:
                        startActivity(new Intent(ListenLaterMusic.this, Search.class));
                        break;



                }
                return true;
            }
        });




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

                adapter=new MyAdapterMusic(ls, ListenLaterMusic.this);
                adapter.notifyDataSetChanged();

                rv.setAdapter(adapter);
                RecyclerView.LayoutManager lm=new LinearLayoutManager(ListenLaterMusic.this, LinearLayoutManager.HORIZONTAL, true);
                rv.setLayoutManager(lm);
                System.out.println("Size is: "+ls.size());
//                Toast.makeText(MainPage.this, ls.size()+"", Toast.LENGTH_SHORT).show();
                System.out.println(adapter.getItemCount());

                databaseReference.child("listenLater").removeEventListener(valueEventListener);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        databaseReference.child("listenLater").addValueEventListener(valueEventListener);


    }

    @Override
    protected void onResume() {
        super.onResume();

        mNavigationBottom.setSelectedItemId(R.id.page_4);

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

                adapter=new MyAdapterMusic(ls, ListenLaterMusic.this);
                adapter.notifyDataSetChanged();

                rv.setAdapter(adapter);
                RecyclerView.LayoutManager lm=new LinearLayoutManager(ListenLaterMusic.this, LinearLayoutManager.HORIZONTAL, true);
                rv.setLayoutManager(lm);
                System.out.println("Size is: "+ls.size());
//                Toast.makeText(MainPage.this, ls.size()+"", Toast.LENGTH_SHORT).show();
                System.out.println(adapter.getItemCount());

                databaseReference.child("listenLater").removeEventListener(valueEventListener);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        databaseReference.child("listenLater").addValueEventListener(valueEventListener);
    }
}