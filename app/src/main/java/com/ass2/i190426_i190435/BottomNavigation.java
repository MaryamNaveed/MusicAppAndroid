package com.ass2.i190426_i190435;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class BottomNavigation extends AppCompatActivity {

    TabLayout tabLayout;
    FrameLayout viewPager;
    BottomNavigationView mNavigationBottom;
    MainPageFragment mainPageFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        mNavigationBottom =findViewById(R.id.mNavigationBottom);
        viewPager=findViewById(R.id.viewerPager);

        mNavigationBottom.getMenu().setGroupCheckable(0, true, false);
        for (int i=0; i<mNavigationBottom.getMenu().size(); i++) {
            mNavigationBottom.getMenu().getItem(i).setChecked(false);
        }
        mNavigationBottom.getMenu().setGroupCheckable(0, true, true);

        mainPageFragment=new MainPageFragment();

        mNavigationBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.page_1:
                        initializeFragment(mainPageFragment);
                        break;
                    case R.id.page_2:

                        break;
                    case R.id.page_3:

                        break;
                }
                return true;
            }
        });

//        mNavigationBottom.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()){
//                    case R.id.page_1:
//                        viewPager.
//                        break;
//                    case R.id.page_2:
//
//                        break;
//                    case R.id.page_3:
//
//                        break;
//                }
//                return true;
//            }
//        });
    }

    private void initializeFragment(Fragment fragment){

        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.viewerPager, fragment);
        fragmentTransaction.commit();

    }
}