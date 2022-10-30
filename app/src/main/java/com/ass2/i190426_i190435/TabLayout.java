package com.ass2.i190426_i190435;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class TabLayout extends AppCompatActivity {

    com.google.android.material.tabs.TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_layout);

        FirebaseUser user =FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference();
        Query query = ref.child("user").orderByChild("id").equalTo(user.getUid());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot appleSnapshot: snapshot.getChildren()) {
                    HashMap<String, Object> hashMap=new HashMap<>();
                    hashMap.put("status","online");
                    appleSnapshot.getRef().updateChildren(hashMap);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
            });

        tabLayout=findViewById(R.id.tabLayout);
        viewPager=findViewById(R.id.viewerPager);
        viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.AddItems(new ChatFragment(), "Chat");
        viewPagerAdapter.AddItems(new PeopleFragment(), "Contacts");
        viewPagerAdapter.AddItems(new CallFragment(), "Calls");

        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.options_menu, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onOptionsItemSelected(item);

       /* if (item.getItemId() == R.id.main_logout_option)
        {
            updateUserStatus("offline");
            mAuth.signOut();
            SendUserToLoginActivity();
        }
        if (item.getItemId() == R.id.main_settings_option)
        {
            SendUserToSettingsActivity();
        }
        if (item.getItemId() == R.id.main_create_group_option)
        {
            RequestNewGroup();
        }
        if (item.getItemId() == R.id.main_find_friends_option)
        {
            SendUserToFindFriendsActivity();
        }*/

        return true;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter{

        ArrayList<Fragment> fragments=new ArrayList<>();
        ArrayList<String> titles=new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void AddName(String title){
            titles.add(title);
        }

        public void AddFragment(Fragment fragment){
            fragments.add(fragment);

        }

        public  void AddItems(Fragment fragment, String title){
            AddFragment(fragment);
            AddName(title);

        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position){
            return titles.get(position);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser user =FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference();
        Query query = ref.child("user").orderByChild("id").equalTo(user.getUid());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot appleSnapshot: snapshot.getChildren()) {
                   HashMap<String, Object> hashMap=new HashMap<>();
                   hashMap.put("status","online");
                    appleSnapshot.getRef().updateChildren(hashMap);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onPause() {


        super.onPause();
        Toast.makeText(TabLayout.this,"Pause", Toast.LENGTH_SHORT).show();
        FirebaseUser user =FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference();
        Query query = ref.child("user").orderByChild("id").equalTo(user.getUid());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot appleSnapshot: snapshot.getChildren()) {
                    HashMap<String, Object> hashMap=new HashMap<>();
                    hashMap.put("status","offline");
                    appleSnapshot.getRef().updateChildren(hashMap);
                    HashMap<String, Object> hashMap1=new HashMap<>();
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    String lastSeen="Last Seen "+ now.format(dtf);
                    hashMap1.put("lastSeen",lastSeen);
                    appleSnapshot.getRef().updateChildren(hashMap1);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}