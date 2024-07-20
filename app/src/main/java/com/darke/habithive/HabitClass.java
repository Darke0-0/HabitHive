package com.darke.habithive;

import static androidx.test.InstrumentationRegistry.getContext;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.Timestamp;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;
import org.threeten.bp.ZoneId;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class HabitClass{
    private Timestamp createdAt;
    private ArrayList<Integer> daysOfWeek;
    private ArrayList<Integer> daysOfMonth;
    private String frequency;
    private ArrayList<String> goalResponse;
    private String goalType;
    private String habitName;
    private String habitType;
    private ArrayList<String> reminders;
    private String habitId;
    private List<LocalDate> scheduledDates;
    private Map<String,Map<String, Object>> progress;

    // Required empty constructor
    public HabitClass() {}

    // Constructor with all fields
    public HabitClass(String habitName ,Timestamp createdAt, String habitType, String frequency,
                      ArrayList<Integer> daysOfWeek, ArrayList<Integer> daysOfMonth,
                      ArrayList<String> reminders, String goalType, ArrayList<String> goalResponse,
                      String habitId, Map<String, Map<String, Object>> progress) {
        this.habitName = habitName;
        this.createdAt = createdAt;
        this.habitType = habitType;
        this.frequency = frequency;
        this.daysOfWeek = daysOfWeek;
        this.daysOfMonth = daysOfMonth;
        this.reminders = reminders;
        this.goalType = goalType;
        this.goalResponse = goalResponse;
        this.habitId = habitId;
        this.progress = progress;
    }

    // Getters and setters
    public String getHabitName() {
        return habitName;
    }

    public void setHabitName(String habitName) {
        this.habitName = habitName;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getHabitType() {
        return habitType;
    }

    public void setHabitType(String habitType) {
        this.habitType = habitType;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public ArrayList<Integer> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(ArrayList<Integer> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
        this.daysOfMonth = null;
    }

    public ArrayList<Integer> getDaysOfMonth() {
        return daysOfMonth;
    }

    public void setDaysOfMonth(ArrayList<Integer> daysOfMonth) {
        this.daysOfMonth = daysOfMonth;
        this.daysOfWeek = null;
    }

    public ArrayList<String> getReminders() {
        return reminders;
    }

    public void setReminders(ArrayList<String> reminders) {
        this.reminders = reminders;
    }

    public String getGoalType() {
        return goalType;
    }

    public void setGoalType(String goalType) {
        this.goalType = goalType;
    }

    public ArrayList<String> getGoalResponse() {
        return goalResponse;
    }

    public void setGoalResponse(ArrayList<String> goalResponse) {
        this.goalResponse = goalResponse;
    }

    public String getHabitId() {
        return habitId;
    }

    public void setHabitId(String habitId) {
        this.habitId = habitId;
    }


    public Map<String,Map<String, Object>>  getProgress() {
        return progress;
    }

    public void setProgress(Map<String,Map<String, Object>>  progress) {
        this.progress = progress;
    }

    public int getCompletionRate() {
        return 0;
    }
    public String getStreakInfo() {
        return "0-day streak";
    }

    public List<String> getChecklistGoal() {
        if (goalType.equals("Checklist")) {
            return goalResponse;
        } else {
            return null;
        }
    }

    public boolean isChecklistCompleted() {
        return false;
    }

    public String getTimerGoal() {
        return "1314";
    }

    public int getCounterGoal() {
        return 0;
    }

    public int getCounterValue() {
        return 0;
    }

    public void setCounterValue(int counterValue) {
    }

    public void setChecklistCompleted(boolean checklistCompleted) {
    }

    public boolean isDueOnDate(LocalDate date) {

        LocalDate createdAtDate = Instant.ofEpochMilli(this.createdAt.toDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();

        if (frequency.equals("Daily")) {
            int current = date.getDayOfWeek().getValue() - 1;
            return daysOfWeek.get(current) == 1 && date.isAfter(createdAtDate);
        }else if (frequency.equals("Monthly")) {
            return daysOfMonth.contains(date.getDayOfMonth());
        } else {
            return false;
        }
    }
}