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

public class Search extends AppCompatActivity {

    RecyclerView rv;
    List<Music> ls;
    List<Music> temp;
    MyAdapterMusic adapter;
    EditText search;
    BottomNavigationView mNavigationBottom;
    String searchText="";
    ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        rv=findViewById(R.id.rv);
        search=findViewById(R.id.search);
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
                     ls.add(value);
                 }

                 databaseReference.child("music").removeEventListener(valueEventListener);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        databaseReference.child("music").addValueEventListener(valueEventListener);


        adapter=new MyAdapterMusic(ls, Search.this);
        rv.setAdapter(adapter);
        RecyclerView.LayoutManager lm=new LinearLayoutManager(Search.this);
        rv.setLayoutManager(lm);

        starting();


        mNavigationBottom.setSelectedItemId(R.id.page_3);

        mNavigationBottom.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.page_1:
                        startActivity(new Intent(Search.this, LikedMusic.class));
                        break;
                    case R.id.page_2:
                        startActivity(new Intent(Search.this, AddMusic.class));
                        break;
                    case R.id.page_4:
                        startActivity(new Intent(Search.this, ListenLaterMusic.class));
                        break;

                }
                return true;
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchText=search.getText().toString();

                List<Music> mylist=new ArrayList<>();


                for( Music m: temp){
                    System.out.println(m.title.toLowerCase()+ "       "+ searchText.toLowerCase(Locale.ROOT));

                    if(m.title.toLowerCase().contains(searchText.toLowerCase()) ){
                        System.out.println("contain");

                        mylist.add(m);

                    }
                    else if(m.genre.toLowerCase().contains(searchText.toLowerCase())){

                        mylist.add(m);

                    }
                }
                ls=mylist;

                adapter=new MyAdapterMusic(ls, Search.this);
                adapter.notifyDataSetChanged();

                 rv.setAdapter(adapter);
                 RecyclerView.LayoutManager lm=new LinearLayoutManager(Search.this);
                 rv.setLayoutManager(lm);
                 System.out.println(ls.size());
                System.out.println(adapter.getItemCount());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    public void starting() {

        List<Music> mylist=new ArrayList<>();
        ls = new ArrayList<>();

        for(Music m: temp){
            if(m.title.toLowerCase().contains(searchText.toLowerCase()) ){
                System.out.println("contain");

                mylist.add(m);
                ls.add(m);

            }
            else if(m.genre.toLowerCase().contains(searchText.toLowerCase())){

                mylist.add(m);
                ls.add(m);

            }
        }



        adapter.notifyDataSetChanged();

       /* rv.setAdapter(adapter);
        RecyclerView.LayoutManager lm=new LinearLayoutManager(Search.this);
        rv.setLayoutManager(lm);
        System.out.println(ls.size());
        System.out.println(adapter.getItemCount());*/


    }

    @Override
    protected void onResume() {
        super.onResume();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                temp=new ArrayList<>();
                for (DataSnapshot child: children) {
                    Music value = child.getValue(Music.class);
                    temp.add(value);
                }

                databaseReference.child("music").removeEventListener(valueEventListener);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        databaseReference.child("music").addValueEventListener(valueEventListener);
    }
}