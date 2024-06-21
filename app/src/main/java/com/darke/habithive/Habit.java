package com.darke.habithive;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;

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

        habitNameTextView = findViewById(R.id.habit_name);
        habitTypeIconImageView = findViewById(R.id.habit_type_icon);
        calendarView = findViewById(R.id.calendar_view);
        editHabitButton = findViewById(R.id.edit_habit_button);
        deleteHabitButton = findViewById(R.id.delete_habit_button);
        db = FirebaseFirestore.getInstance();

        // Retrieve habit details passed via Intent
        String habitId = getIntent().getStringExtra("HABIT_ID");
        if (habitId != null) {
            loadHabitDetails(habitId);
        }

        editHabitButton.setOnClickListener(v -> {
            // Handle edit habit action
            Toast.makeText(this, "Edit Habit clicked", Toast.LENGTH_SHORT).show();
            // Intent to open edit habit activity
        });

        deleteHabitButton.setOnClickListener(v -> {
            // Handle delete habit action
            deleteHabit(habitId);
        });

        // Highlight calendar dates (this is a placeholder for actual implementation)
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // Handle date selection, e.g., display task completion status
        });
    }

    private void loadHabitDetails(String habitId) {
        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("habits").document(habitId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String habitName = documentSnapshot.getString("name");
                        String habitType = documentSnapshot.getString("type");
                        habitNameTextView.setText(habitName);

                        // Set habit type icon based on habit type
                        if (habitType != null) {
                            switch (habitType) {
                                case "Type 1":
                                    habitTypeIconImageView.setImageResource(R.drawable.icon_type1);
                                    break;
                                case "Type 2":
                                    habitTypeIconImageView.setImageResource(R.drawable.icon_type2);
                                    break;
                                // Add more cases for other habit types
                            }
                        }

                        // Load and highlight the calendar with progress data
                        // Placeholder: highlight dates based on user progress
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(Habit.this, "Failed to load habit details", Toast.LENGTH_SHORT).show());
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