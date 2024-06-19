package com.darke.habithive;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Creation extends AppCompatActivity {

    private static final int TAB_HABIT = 0;
    private static final int TAB_TASK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_habit_creation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager2 viewPager = findViewById(R.id.view_pager);

        // Set up ViewPager with an adapter
        CreationViewPagerAdapter adapter = new CreationViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // Set up TabLayout with ViewPager
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(position == TAB_HABIT ? getString(R.string.tab_habits) : getString(R.string.tab_tasks))
        ).attach();

    }
}