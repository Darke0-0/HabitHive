package com.darke.habithive;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class HabitClass implements Parcelable {
    private Timestamp createdAt;
    private ArrayList<Integer> daysOfWeek;
    private ArrayList<Integer> dayOfMonth;
    private String frequency;
    private ArrayList<String> goalResponse;
    private String goalType;
    private String habitName;
    private String habitType;
    private ArrayList<String> reminders;
    private String userId;

    // Required empty constructor
    public HabitClass() {}

    // Constructor with all fields
    public HabitClass(String habitName,String userId ,Timestamp createdAt, String habitType, String frequency,
                      ArrayList<Integer> daysOfWeek, ArrayList<Integer> dayOfMonth, ArrayList<String> reminders,
                      String goalType, ArrayList<String> goalResponse) {
        this.habitName = habitName;
        this.userId = userId;
        this.createdAt = createdAt;
        this.habitType = habitType;
        this.frequency = frequency;
        this.daysOfWeek = daysOfWeek;
        this.dayOfMonth = dayOfMonth;
        this.reminders = reminders;
        this.goalType = goalType;
        this.goalResponse = goalResponse;
    }

    // Parcelable implementation
    protected HabitClass(Parcel in) {
        habitName = in.readString();
        userId = in.readString();
        createdAt = in.readParcelable(Timestamp.class.getClassLoader());
        habitType = in.readString();
        frequency = in.readString();
        goalType = in.readString();
        if (in.readByte() == 0x01) {
            daysOfWeek = new ArrayList<>();
            in.readList(daysOfWeek, Integer.class.getClassLoader());
        } else {
            daysOfWeek = null;
        }
        if (in.readByte() == 0x01) {
            dayOfMonth = new ArrayList<>();
            in.readList(dayOfMonth, Integer.class.getClassLoader());
        } else {
            dayOfMonth = null;
        }
        if (in.readByte() == 0x01) {
            reminders = new ArrayList<>();
            in.readList(reminders, String.class.getClassLoader());
        } else {
            reminders = null;
        }
        if (in.readByte() == 0x01) {
            goalResponse = new ArrayList<>();
            in.readList(goalResponse, String.class.getClassLoader());
        } else {
            goalResponse = null;
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(habitName);
        dest.writeString(userId);
        dest.writeParcelable(createdAt, flags);
        dest.writeString(habitType);
        dest.writeString(frequency);
        if (daysOfWeek == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(daysOfWeek);
        }
        if (dayOfMonth == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(dayOfMonth);
        }
        if (reminders == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(reminders);
        }
        if (goalResponse == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(goalResponse);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<HabitClass> CREATOR = new Parcelable.Creator<HabitClass>() {
        @Override
        public HabitClass createFromParcel(Parcel in) {
            return new HabitClass(in);
        }

        @Override
        public HabitClass[] newArray(int size) {
            return new HabitClass[size];
        }
    };

    // Getters and setters
    public String getHabitName() {
        return habitName;
    }

    public void setHabitName(String habitName) {
        this.habitName = habitName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
}