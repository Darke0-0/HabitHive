package com.darke.habithive;

import static com.google.firebase.firestore.DocumentChange.Type.REMOVED;
import static com.google.firebase.firestore.DocumentChange.Type.ADDED;
import static com.google.firebase.firestore.DocumentChange.Type.MODIFIED;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.ViewGroup;
import android.widget.TextView;

import androidx.privacysandbox.ads.adservices.topics.TopicsManagerApi33Ext4Impl;
import androidx.recyclerview.widget.RecyclerView;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import java.util.Locale;
import java.util.Objects;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

public class Dashboard extends AppCompatActivity{

    private HabitAdapter habitAdapter;

    private List<HabitClass> allHabits;
    private ViewPager2 viewPagerWeeks;
    private LocalDate currentDate = LocalDate.now();
    private LocalDate selectedDate;
    private WeekAdapter WeekAdapter;
    private List<LocalDate> weeksList;
    private MaterialButton addHabitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_dashboard), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewPagerWeeks = findViewById(R.id.week_view_pager);
        MaterialButton prevButton = findViewById(R.id.prev_button);
        MaterialButton nextButton = findViewById(R.id.next_button);

        currentDate = LocalDate.now(); // Current date
        weeksList = generateWeeksList(currentDate); // Generate list of weeks

        WeekAdapter = new WeekAdapter(weeksList, currentDate, this::onDateSelected);
        viewPagerWeeks.setAdapter(WeekAdapter);

        viewPagerWeeks.setCurrentItem(51, false);

        prevButton.setOnClickListener(v -> viewPagerWeeks.setCurrentItem(viewPagerWeeks.getCurrentItem() - 1, true));
        nextButton.setOnClickListener(v -> viewPagerWeeks.setCurrentItem(viewPagerWeeks.getCurrentItem() + 1, true));

        habitAdapter = new HabitAdapter(new ArrayList<>(), this);

        RecyclerView habitRecyclerView = findViewById(R.id.habit_list);
        habitRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        habitRecyclerView.setAdapter(habitAdapter);

        allHabits = UserData.getUserData(this).getHabits();
        List<HabitClass> habitsDueOnDate = filterHabitsDueOnDate(currentDate);
        habitAdapter.setSelectedDate(currentDate);
        habitAdapter.setHabits(habitsDueOnDate);

        addHabitButton = findViewById(R.id.add_habit_button);
        addHabitButton.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard.this, Creation.class);
            startActivity(intent);
        });

        listenForHabitChanges();
    }

    private List<LocalDate> generateWeeksList(LocalDate currentDate) {
        List<LocalDate> weeksList = new ArrayList<>();
        LocalDate startOfWeek = currentDate.minusWeeks(52).with(DayOfWeek.SUNDAY); // 52 weeks before current date
        LocalDate endOfWeek = currentDate.plusWeeks(52).with(DayOfWeek.SATURDAY); // 52 weeks ahead of current date

        LocalDate dateIterator = startOfWeek;
        while (!dateIterator.isAfter(endOfWeek)) {
            weeksList.add(dateIterator);
            dateIterator = dateIterator.plusWeeks(1);
        }
        return weeksList;
    }

    private void onDateSelected(LocalDate date) {
        // Filter habits due on selected date
        List<HabitClass> habitsDueOnDate = filterHabitsDueOnDate(date);
        habitAdapter.setSelectedDate(date);
        habitAdapter.setHabits(habitsDueOnDate);
    }

    private List<HabitClass> filterHabitsDueOnDate(LocalDate selectedDate) {
        List<HabitClass> habitsDueOnDate = new ArrayList<>();
        if (allHabits == null) {
            return habitsDueOnDate;
        }
        for (HabitClass habit : allHabits) {
            if (habit.isDueOnDate(selectedDate)) {
                habitsDueOnDate.add(habit);
            }
        }
        return habitsDueOnDate;
    }

    private void listenForHabitChanges() {
        String userId = UserData.getUserData(this).getUserId();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("user").document(userId).collection("habits").addSnapshotListener((snapshots, e) -> {
            if (e != null) {
                Log.w("Dashboard", "Listen failed.", e);
                return;
            }

            if (snapshots != null) {
                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                        case MODIFIED:
                        case REMOVED:
                            UserData.loadUserDataAndSaveToPrefs(this);
                            break;
                    }
                }
            }
        });
    }
}
