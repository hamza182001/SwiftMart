package com.example.madgrocery_project;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;



import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;



public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager2 viewPager2 = findViewById(R.id.viewPagerTabs);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Home");
                            tab.setIcon(getTabIcon(R.drawable.home_tabitem, R.drawable.home_tabitem_slected, position, tabLayout));
                            break;
                        case 1:
                            tab.setText("Explore");
                            tab.setIcon(getTabIcon(R.drawable.search_icon, R.drawable.explore_tabitem_selected, position, tabLayout));
                            break;
                        case 2:
                            tab.setText("Liked");
                            tab.setIcon(getTabIcon(R.drawable.favorite_tabitem, R.drawable.favourite_tabitem_selected, position, tabLayout));
                            break;
                        case 3:
                            tab.setText("Cart");
                            tab.setIcon(getTabIcon(R.drawable.cart_tabitem, R.drawable.cart_tabitem_selected, position, tabLayout));
                            break;
                        case 4:
                            tab.setText("Account");
                            tab.setIcon(getTabIcon(R.drawable.account_tabitem, R.drawable.account_tabitem_selected, position, tabLayout));
                            break;
                    }
                }
        ).attach();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(getResources().getColor(R.color.light_green), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private Drawable getTabIcon(int defaultIconResId, int selectedIconResId, int position, TabLayout tabLayout) {
        if (position == tabLayout.getSelectedTabPosition()) {

            return AppCompatResources.getDrawable(this, selectedIconResId);
        } else {

            return AppCompatResources.getDrawable(this, defaultIconResId);
        }
    }
}











