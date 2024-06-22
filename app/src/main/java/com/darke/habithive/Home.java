package com.darke.habithive;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager2 viewPager = findViewById(R.id.view_pager);
        FloatingActionButton fabAdd = findViewById(R.id.fab_add);
        TextView welcomeTextView = findViewById(R.id.welcome_text_view);

        // Retrieve user details from intent
        String email = getIntent().getStringExtra("email");
        String name = getIntent().getStringExtra("name");

        // Set up ViewPager with an adapter
        HomeViewPagerAdapter adapter = new HomeViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // Set up TabLayout with ViewPager
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(position == 0 ? "Habits" : "Tasks")
        ).attach();

        // Floating action button click listener
        fabAdd.setOnClickListener(v -> {
            // Handle adding new habit or task
            // For example, you could start a new activity for adding a habit/task
             Intent intent = new Intent(Home.this, Creation.class);
             intent.putExtra("email", email);
             intent.putExtra("name", name);
             startActivity(intent);
        });
    }
}