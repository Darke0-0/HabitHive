package com.darke.habithive;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class CreationViewPagerAdapter extends FragmentStateAdapter {

    public CreationViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    // Default constructor
    public CreationViewPagerAdapter() {
        super(null); // or provide an appropriate default FragmentActivity instance
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return position == 0 ? new CreationHabitFragment() : new CreationTaskFragment();
    }

    @Override
    public int getItemCount() {
        return 2; // We have two tabs
    }
}