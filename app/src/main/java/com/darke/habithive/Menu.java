package com.darke.habithive;

import android.content.Context;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class Menu {
    private Context context;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    public Menu(Context context, DrawerLayout drawerLayout, NavigationView navigationView) {
        this.context = context;
        this.drawerLayout = drawerLayout;
        this.navigationView = navigationView;

        setupDrawer();
    }

    private void setupDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                (AppCompatActivity) context, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                // Handle Home click
                Toast.makeText(context, "Home Clicked", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_habits) {
                // Handle Habits click or start new activity
                Toast.makeText(context, "Habits Clicked", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_tasks) {
                // Handle Tasks click or start new activity
                Toast.makeText(context, "Tasks Clicked", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_settings) {
                // Handle Settings click or start new activity
                Toast.makeText(context, "Settings Clicked", Toast.LENGTH_SHORT).show();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    // Method to handle back press if drawer is open
    public boolean handleDrawerBackPress() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return true; // Drawer was open, handled back press
        }
        return false; // Drawer was not open, continue with default back press handling
    }
}
