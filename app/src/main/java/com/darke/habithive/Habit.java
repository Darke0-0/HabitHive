package com.darke.habithive;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Habit extends AppCompatActivity {

    private HabitClass habit;
    private MaterialCalendarView calendarView;
    private Button editHabitButton;
    private Button deleteHabitButton;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Map<String, String> habitStatusMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        calendarView = findViewById(R.id.calendar_view);
        editHabitButton = findViewById(R.id.edit_habit_button);
        deleteHabitButton = findViewById(R.id.delete_habit_button);

        String habitId = getIntent().getStringExtra("habitId");

        loadHabits(habitId);
        loadHabitStatuses(habitId);

        editHabitButton.setOnClickListener(v -> {
            // Handle edit habit action
            Toast.makeText(this, "Edit Habit clicked", Toast.LENGTH_SHORT).show();
            // Intent to open edit habit activity
        });

        deleteHabitButton.setOnClickListener(v -> {
            deleteHabit(habitId);
        });

        // Highlight calendar dates (this is a placeholder for actual implementation)
        // Set up CalendarView date change listener
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                String dateString = date.getYear() + "-" + (date.getMonth() + 1) + "-" + date.getDay();
                showNoteDialog(dateString, habitId);
            }
        });
    }

    private void loadHabits(String habitId) {
        String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        Toast.makeText(Habit.this, habitId, Toast.LENGTH_SHORT).show();
        db.collection("users").document(userId)
                .collection("habits").document(habitId)
                .get()
                .addOnCompleteListener(habitTask -> {
                    if (habitTask.isSuccessful()) {
                        DocumentSnapshot habitDocument = habitTask.getResult(); // Corrected line
                        if (habitDocument != null && habitDocument.exists()) {
                            habit = habitDocument.toObject(HabitClass.class);
                        }
                    }
                });
    }

    private void loadHabitStatuses(String habitId) {
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("users").document(userId)
                .collection("habits").document(habitId)
                .collection("dates")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String date = document.getId();
                            String status = document.getString("status");
                            habitStatusMap.put(date, status);
                            updateCalendarDate(date, status);
                        }
                    } else {
                        Toast.makeText(Habit.this, "Error getting notes", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateCalendarDate(String date, String status) {
        try {
            String[] dateParts = date.split("-");
            int year = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]) - 1;
            int day = Integer.parseInt(dateParts[2]);

            CalendarDay calendarDay = CalendarDay.from(year, month, day);

            // Add decorator for each calendar day
            calendarView.addDecorator(new CircleDecorator(calendarDay, getColorForStatus(status)));

            Toast.makeText(Habit.this, "Calendar date updated", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(Habit.this, "Error updating calendar date", Toast.LENGTH_SHORT).show();
            Log.e("Habit", "Error updating calendar date", e);
        }
    }

    private int getColorForStatus(String status) {
        int color = Color.GRAY; // Default color

        switch (status) {
            case "complete":
                color = Color.GREEN;
                break;
            case "partial":
                color = Color.YELLOW;
                break;
            case "incomplete":
                color = Color.RED;
                break;
            // Add more cases as needed
        }

        return color;
    }

    // Custom decorator to draw circle around calendar day
    private static class CircleDecorator implements DayViewDecorator {

        private final CalendarDay day;
        private final int color;

        public CircleDecorator(CalendarDay day, int color) {
            this.day = day;
            this.color = color;
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return this.day.equals(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(8, color)); // Adjust dot size and color as needed
        }
    }

    private void showNoteDialog(String date, String habitId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Note for " + date);

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String note = input.getText().toString();
            saveNoteToFirestore(date, note, habitId);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void saveNoteToFirestore(String date, String note, String habitId) {
        String userId = mAuth.getCurrentUser().getUid();
        DocumentReference noteRef = db.collection("users").document(userId)
                .collection("habits").document(habitId)
                .collection("notes").document(date);

        Map<String, Object> noteData = new HashMap<>();
        noteData.put("note", note);
        noteData.put("status", "partial"); // Example status, replace with actual logic

        noteRef.set(noteData)
                .addOnSuccessListener(aVoid -> Toast.makeText(Habit.this, "Note saved", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(Habit.this, "Error saving note", Toast.LENGTH_SHORT).show());
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
