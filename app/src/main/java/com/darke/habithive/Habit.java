package com.darke.habithive;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Habit extends AppCompatActivity {

    private TextView habitNameTextView;
    private ImageView habitTypeIconImageView;
    private CalendarView calendarView;
    private Button editHabitButton;
    private Button deleteHabitButton;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        habitNameTextView = findViewById(R.id.habit_name);
        habitTypeIconImageView = findViewById(R.id.habit_type_icon);
        calendarView = findViewById(R.id.calendar_view);
        editHabitButton = findViewById(R.id.edit_habit_button);
        deleteHabitButton = findViewById(R.id.delete_habit_button);
        db = FirebaseFirestore.getInstance();

        // Retrieve the HabitClass object from the Intent
        HabitClass habit = getIntent().getParcelableExtra("habit");
        if (habit != null) {
            // Use the habit object
            habitNameTextView.setText(habit.getHabitName());
        }

        editHabitButton.setOnClickListener(v -> {
            // Handle edit habit action
            Toast.makeText(this, "Edit Habit clicked", Toast.LENGTH_SHORT).show();
            // Intent to open edit habit activity
        });

        deleteHabitButton.setOnClickListener(v -> {
            assert habit != null;
            deleteHabit(habit.getHabitId());
        });

        // Highlight calendar dates (this is a placeholder for actual implementation)
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // Handle date selection, e.g., display task completion status
        });
    }

    private void deleteHabit(String habitId) {
        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("habits").document(habitId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(Habit.this, "Habit deleted successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity after deletion
                })
                .addOnFailureListener(e -> Toast.makeText(Habit.this, "Failed to delete habit", Toast.LENGTH_SHORT).show());
    }
}
