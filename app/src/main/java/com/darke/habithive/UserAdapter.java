package com.darke.habithive;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class UserAdapter extends FragmentStateAdapter {


    public UserAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return position == 0 ? new UserLoginFragment() : new UserSignupFragment();
    }

    @Override
    public int getItemCount() {
        return 2; // We have two tabs
    }
}
