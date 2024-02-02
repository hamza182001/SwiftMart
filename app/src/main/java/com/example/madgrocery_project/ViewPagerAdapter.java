package com.example.madgrocery_project;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {


    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new MainActivityFragment();
            case 1:
                return new ExploreFragment();
            case 2:
                return new FavouritesFragment();
            case 3:
                return new CartFragment();
            case 4:
                return new MyAccountFragment();
            default:
                throw new IllegalArgumentException("Invalid tab position");
        }
    }


    @Override
    public int getItemCount() {
        return 5;
    }
}
