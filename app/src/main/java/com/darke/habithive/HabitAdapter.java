package com.darke.habithive;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.ChronoUnit;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.HabitViewHolder> {

    private static Context context;
    private List<HabitClass> habitList;
    private static LocalDate selectedDate;
    private static Map<String, Map<String, Object>> progress;
    private static List<String> daysStatus;

    public HabitAdapter(List<HabitClass> habitList, Context context) {
        this.habitList = habitList;
        this.context = context;
    }

    @NonNull
    @Override
    public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.habit_card, parent, false);
        return new HabitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitViewHolder holder, int position) {
        HabitClass habit = habitList.get(position);
        holder.bind(habit);
    }

    @Override
    public int getItemCount() {
        if (habitList == null) {
            return 0;
        }
        return habitList.size();
    }

    public void setSelectedDate(LocalDate selectedDate) {
        this.selectedDate = selectedDate;
        notifyDataSetChanged();
    }
    public void setHabits(List<HabitClass> habits) {
        this.habitList = habits;
        notifyDataSetChanged();
    }
    public static void setProgress(Map<String, Map<String, Object>> progress) {
        HabitAdapter.progress = progress;
    }

    public static class HabitViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        private TextView habitName, habitType;
        private View day1, day2, day3, day4, day5, day6, day7;
        private EditText checklistGoal, timerGoal, counterGoal;
        private CheckBox checklistCheckbox;
        private Chronometer timerChronometer;
        private Button incrementCounterButton;
        private LinearLayout checklistLayout, timerLayout, counterLayout;
        private TextView counterValue;
        RecyclerView checklistRecyclerView;

        public HabitViewHolder(@NonNull View itemView) {
            super(itemView);
            habitName = itemView.findViewById(R.id.habit_name);
            habitType = itemView.findViewById(R.id.habit_type);
            day1 = itemView.findViewById(R.id.day1);
            day2 = itemView.findViewById(R.id.day2);
            day3 = itemView.findViewById(R.id.day3);
            day4 = itemView.findViewById(R.id.day4);
            day5 = itemView.findViewById(R.id.day5);
            day6 = itemView.findViewById(R.id.day6);
            day7 = itemView.findViewById(R.id.day7);
            checklistGoal = itemView.findViewById(R.id.checklist_goal);
            checklistCheckbox = itemView.findViewById(R.id.checklist_checkbox);
            timerGoal = itemView.findViewById(R.id.timer_goal);
            timerChronometer = itemView.findViewById(R.id.timer_chronometer);
            counterGoal = itemView.findViewById(R.id.counter_goal);
            counterValue = itemView.findViewById(R.id.counter_value);
            incrementCounterButton = itemView.findViewById(R.id.increment_counter_button);
            checklistLayout = itemView.findViewById(R.id.checklist_layout);
            timerLayout = itemView.findViewById(R.id.timer_layout);
            counterLayout = itemView.findViewById(R.id.counter_layout);
            checklistRecyclerView = itemView.findViewById(R.id.checklist_recycler_view);

            context = itemView.getContext();
        }

        public void bind(HabitClass habit) {
            String userId = Objects.requireNonNull(UserData.getUserData(context)).getUserId();
            habitName.setText(habit.getHabitName());
            habitType.setText(habit.getHabitType());
            progress = habit.getProgress();

            // Handle goals visibility
            switch (habit.getGoalType()) {
                case "Checklist":
                    checklistLayout.setVisibility(View.VISIBLE);
                    timerLayout.setVisibility(View.GONE);
                    counterLayout.setVisibility(View.GONE);
                    List<String> checklistGoals = new ArrayList<>(habit.getChecklistGoal());
                    ChecklistAdapter checklistAdapter = new ChecklistAdapter(checklistGoals, habit, selectedDate, userId);
                    checklistRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                    checklistRecyclerView.setAdapter(checklistAdapter);
//                    checklistCheckbox.setEnabled(true);
                    break;
                case "Timer":
                    checklistLayout.setVisibility(View.GONE);
                    timerLayout.setVisibility(View.VISIBLE);
                    counterLayout.setVisibility(View.GONE);
                    timerGoal.setText(habit.getTimerGoal());
                    // Initialize the chronometer based on saved timer value
                    break;
                case "Counter":
                    checklistLayout.setVisibility(View.GONE);
                    timerLayout.setVisibility(View.GONE);
                    counterLayout.setVisibility(View.VISIBLE);
                    counterGoal.setText(String.valueOf(habit.getCounterGoal()));
                    counterValue.setText(String.valueOf(habit.getCounterValue()));
                    break;
            }

            daysStatus = getLast7DaysStatus(habit);
            updateCalendar(daysStatus);

            // Increment counter value on button click
            incrementCounterButton.setOnClickListener(v -> {
                int currentValue = habit.getCounterValue();
                habit.setCounterValue(currentValue + 1);
                counterValue.setText(String.valueOf(habit.getCounterValue()));
            });
        }

        private void updateCalendar(List<String> last7DaysStatus) {
            View[] days = {day7, day6, day5, day4, day3, day2, day1};
            for (int i = 0; i < 7; i++) {
                try {
                    switch (last7DaysStatus.get(i)) {
                        case "0":
                            days[i].setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_red_light));
                            break;
                        case "1":
                            days[i].setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_orange_light));
                            break;
                        case "2":
                            days[i].setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_green_light));
                            break;
                        case "999":
                            days[i].setBackgroundColor(ContextCompat.getColor(context, android.R.color.darker_gray));
                            break;
                    }
                }catch (Exception e) {
                    days[i].setBackgroundColor(ContextCompat.getColor(context, android.R.color.darker_gray));
                }
            }
        }
    }

    private static List<String> getLast7DaysStatus(HabitClass habit) {

        List<String> goals = habit.getGoalResponse();
        LocalDate createdAt = Instant.ofEpochMilli(habit.getCreatedAt().toDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        Map<String, List<String>> statusMap = new HashMap<>();

        LocalDate currentDate = selectedDate;
        int daysFound = 0;
        while (daysFound < 7) {
            List<String> key = new ArrayList<>();
            String monthKey = currentDate.format(DateTimeFormatter.ofPattern("MMyy"));
            String dateKey = String.valueOf(currentDate.getDayOfMonth());
            if (progress.containsKey(monthKey)) {
                Map<String, Object> progressGoals = (Map<String, Object>) progress.get(monthKey).get("goals");
                Map<String, String> progressGoal = (Map<String, String>) progressGoals.get(goals.get(0));
                if (progressGoal.containsKey(dateKey)) {
                    key.add(dateKey);
                    key.add(monthKey);
                    statusMap.put(String.valueOf(daysFound), key);
                    daysFound++;
                }
            } else if (currentDate.isBefore(createdAt)) {
                break;
            } else if (Integer.parseInt(monthKey) > Integer.parseInt(Collections.max(progress.keySet()))) {
                if (habit.getFrequency().equals("Daily")) {
                    if (habit.getDaysOfWeek().get(currentDate.getDayOfWeek().getValue() - 1) == 1) {
                        key.add("999");
                        key.add("999");
                        statusMap.put(String.valueOf(daysFound), key);
                        daysFound++;
                    }
                }else if (habit.getFrequency().equals("Monthly")) {
                    if (habit.getDaysOfMonth().contains(currentDate.getDayOfMonth())) {
                        key.add("999");
                        key.add("999");
                        statusMap.put(String.valueOf(daysFound), key);
                        daysFound++;
                    }
                }
            }
            currentDate = currentDate.minus(1, ChronoUnit.DAYS); // Move to previous day
        }

        Map<String,List<String>> last7DaysStatus = new HashMap<>();
        for (String goal : goals) {
            last7DaysStatus.put(goal,new ArrayList<>());
            for (String key : statusMap.keySet()) {
                if (statusMap.get(key).get(0) == "999") {
                    last7DaysStatus.get(goal).add("999");
                    continue;
                }
                String dateKey = statusMap.get(key).get(0);
                String month = statusMap.get(key).get(1);
                Map<String, Object> progressGoals = (Map<String, Object>) habit.getProgress().get(month).get("goals");
                Map<String, String> progressGoal = (Map<String, String>) progressGoals.get(goal);
                last7DaysStatus.get(goal).add(progressGoal.get(dateKey));
            }
        }

        List<String> status = new ArrayList<>();
        for(int i=0; i < last7DaysStatus.get(goals.get(0)).size(); i++){
            List<String> progressList = new ArrayList<>();
            for (String goal : goals) {
                progressList.add(last7DaysStatus.get(goal).get(i));
            }
            boolean b = progressList.stream().distinct().count() <= 1;
            if (b) {
                if (progressList.get(0).equals("1")) {
                    status.add("2");
                }else if (progressList.get(0).equals("0")) {
                    status.add("0");
                }else{
                    status.add("999");
                }
            }else {
                status.add("1");
            }
        }
        return status;
    }

    private int getHabitTypeIcon(String habitType) {
        if (habitType == null) {
            return R.drawable.ic_outdoor;
        }
        switch (habitType) {
            case "finance":
                return R.drawable.ic_finance;
            case "health":
                return R.drawable.ic_health;
            case "home":
                return R.drawable.ic_home;
            case "meditation":
                return R.drawable.ic_meditation;
            case "nutrition":
                return R.drawable.ic_nutrition;
            case "outdoor":
                return R.drawable.ic_outdoor;
            case "quit":
                return R.drawable.ic_quit;
            case "run":
                return R.drawable.ic_run;
            case "social":
                return R.drawable.ic_social;
            case "study":
                return R.drawable.ic_study;
            case "work":
                return R.drawable.ic_work;
            default:
                return R.drawable.ic_outdoor;
        }
    }
}

