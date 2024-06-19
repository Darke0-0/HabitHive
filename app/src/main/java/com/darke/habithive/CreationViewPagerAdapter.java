package com.darke.habithive;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class CreationViewPagerAdapter extends FragmentStateAdapter {

    private static final int TAB_HABIT = 0;
    private static final int TAB_TASK = 1;
    private static final int TOTAL_TABS = 2;

    public CreationViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case TAB_HABIT:
                return new CreationHabitFragment();
            case TAB_TASK:
                return new CreationTaskFragment();
            default:
                throw new IllegalArgumentException("Invalid tab position: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return TOTAL_TABS;
    }
}