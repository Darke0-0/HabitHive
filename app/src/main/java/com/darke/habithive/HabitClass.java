package com.darke.habithive;

import java.util.ArrayList;
import java.util.Date;

public class HabitClass {
    private String name;
    private String userId;
    private Date createdAt;
    private String habitType;
    private String frequency;
    private ArrayList<Integer> daysOfWeek;
    private ArrayList<Integer> dayOfMonth;
    private ArrayList<String> reminders;

    // Required empty constructor
    public HabitClass() {}

    // Constructor with all fields
    public HabitClass(String name,String userId ,Date createdAt, String habitType, String frequency, ArrayList<Integer> daysOfWeek, ArrayList<Integer> dayOfMonth, ArrayList<String> reminders) {
        this.name = name;
        this.userId = userId;
        this.createdAt = createdAt;
        this.habitType = habitType;
        this.frequency = frequency;
        this.daysOfWeek = daysOfWeek;
        this.dayOfMonth = dayOfMonth;
        this.reminders = reminders;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
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
        this.dayOfMonth = null;
    }

    public ArrayList<Integer> getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(ArrayList<Integer> dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
        this.daysOfWeek = null;
    }

    public ArrayList<String> getReminders() {
        return reminders;
    }

    public void setReminders(ArrayList<String> reminders) {
        this.reminders = reminders;
    }
}