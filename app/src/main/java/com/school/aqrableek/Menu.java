package com.school.aqrableek;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.aqrableek.R;
import com.google.android.material.tabs.TabLayout;

public class Menu extends AppCompatActivity {
TabLayout tabLayout;
ViewPager2 pager2;
Fragment_All adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        tabLayout =findViewById(R.id.tab_menu);
        pager2 =findViewById(R.id.view_page2);


        FragmentManager fm= getSupportFragmentManager();
        adapter=new Fragment_All(fm, getLifecycle());
        pager2.setAdapter(adapter);

        tabLayout.addTab(tabLayout.newTab().setText("Country"));
        tabLayout.addTab(tabLayout.newTab().setText("City"));
        tabLayout.addTab(tabLayout.newTab().setText("District"));
        tabLayout.addTab(tabLayout.newTab().setText("Profession"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });

        pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });



    }
}