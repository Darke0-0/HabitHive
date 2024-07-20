package com.darke.habithive;
import static androidx.test.InstrumentationRegistry.getContext;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;
import org.threeten.bp.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class ChecklistAdapter extends RecyclerView.Adapter<ChecklistAdapter.ChecklistViewHolder> {

    private List<String> checklistGoals;
    private static HabitClass habit; // Store habit ID for Firestore updates
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static LocalDate selectedDate;
    private static String userId;

    public ChecklistAdapter(List<String> checklistGoals, HabitClass habit, LocalDate selectedDate, String userId) {
        this.checklistGoals = checklistGoals;
        ChecklistAdapter.habit = habit;
        ChecklistAdapter.selectedDate = selectedDate; //TODO
        ChecklistAdapter.userId = userId;
    }

    @NonNull
    @Override
    public ChecklistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.habit_card_checklist, parent, false);
        return new ChecklistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChecklistViewHolder holder, int position) {
        String goal = checklistGoals.get(position);
        holder.checklistGoal.setText(goal);
        String selectedDay = String.valueOf(selectedDate.getDayOfMonth());
        Map<String, String> goalStatusList = getSelectedDateProgress(selectedDate, habit, goal);
        Log.d("ChecklistAdapter", "Selected date: " + " " + selectedDay + " " + goalStatusList);
        String selectedDateStatus;
        try {
            selectedDateStatus = goalStatusList.get(selectedDay);
        } catch (Exception e){
            selectedDateStatus = "0";
        }
        Log.d("ChecklistAdapter", "Selected date status: " + selectedDateStatus);

        if (Objects.equals(selectedDateStatus, "1")) {
            holder.checklistCheckbox.setChecked(true);
        } else if (Objects.equals(selectedDateStatus, "0")) {
            holder.checklistCheckbox.setChecked(false);
        }

        holder.checklistCheckbox.setEnabled(false);

        habit.setChecklistCompleted(true);
        if (Objects.equals(selectedDate, LocalDate.now())) {
            holder.checklistCheckbox.setEnabled(true);
        }
        holder.checklistCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (Objects.equals(selectedDate, LocalDate.now())) {
                updateFirestore(habit, goal, isChecked, selectedDay);
            }
        });
        holder.bind();

    }

    @Override
    public int getItemCount() {
        return checklistGoals.size();
    }

    public static class ChecklistViewHolder extends RecyclerView.ViewHolder {
        EditText checklistGoal;
        CheckBox checklistCheckbox;

        public ChecklistViewHolder(@NonNull View itemView) {
            super(itemView);
            checklistGoal = itemView.findViewById(R.id.checklist_goal);
            checklistCheckbox = itemView.findViewById(R.id.checklist_checkbox);
        }
        public void bind() {
        }
    }

    private static Map<String, String> getSelectedDateProgress(LocalDate selectedDate, HabitClass habit, String goal){
        Map<String, Map<String, Object>> progress = habit.getProgress();
        String dateKey = selectedDate.format(DateTimeFormatter.ofPattern("MMyy"));
        if (progress != null) {
            Map<String, Object> progressData = progress.get(dateKey);
            if (progressData != null) {
                Map<String, Map<String, String>> checklistStatus = (Map<String, Map<String, String>>) progressData.get("goals");
                return checklistStatus.get(goal);
            }
        }
        return null;
    }

    private static void updateFirestore(HabitClass habit, String goal, boolean isChecked, String selectedDay) {
        String habitId = habit.getHabitId();
        String monthKey = LocalDate.now().format(DateTimeFormatter.ofPattern("MMyy"));
        String check;
        if (isChecked) {
            check = "1";
        } else {
            check = "0";
        }
        Log.d("ChecklistAdapter", "Check: " + check + " " + selectedDate + " " + goal + " " + habitId + " " + ChecklistAdapter.userId + " " + monthKey);
        db.collection("users").document(userId)
                .collection("habits").document(habitId)
                .collection("progress").document(monthKey)
                .update("goals." + goal + "." + selectedDay, check)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Progress updated"))
                .addOnFailureListener(e -> Log.w("Firestore", "Error updating progress", e));

        HabitAdapter.updateHabitStatus();
    }

}
